package com.rd.event.presentation.event_detail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rd.event.R
import com.rd.event.domain.model.Event
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mImage: ImageView
    private lateinit var mTitle: TextView
    private lateinit var mDescription: TextView
    private lateinit var mPrice: TextView
    private lateinit var mDate: TextView
    private lateinit var mMap: GoogleMap
    private lateinit var pbSpinner: ProgressBar
    private lateinit var mEvent: Event
    private lateinit var btnCheckIn: Button
    private var eventId: String? = null

    private val model: EventDetailViewModel by viewModel()

    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        loadEvent()
        getElements()
        setObservers()

        btnCheckIn.setOnClickListener {
            createAlertDialog()
        }
    }

    private fun loadEvent() {
        eventId = intent.getStringExtra("id")

        if (eventId.isNullOrEmpty()) {
            Toast.makeText(
                this,
                getString(R.string.even_detail_error),
                Toast.LENGTH_LONG
            ).show()
            finish()
        }

        model.loadActivityEvent(eventId!!)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val marker = LatLng(mEvent.latitude, mEvent.longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(marker)
                .title(mEvent.title)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))
    }

    private fun setObservers() {
        model.error.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })

        model.event.observe(this, { event ->
            mEvent = event
            mImage.load(event.image) {
                crossfade(true)
                placeholder(R.drawable.image)
                error(R.drawable.image)
            }
            mTitle.text = event.title
            mDescription.text = event.description
            mPrice.text = if (event.price.isNotBlank()) {
                getString(R.string.event_detail_price, event.price)
            } else {
                getString(R.string.event_price_free)
            }
            mDate.text = getString(R.string.event_date, event.date)

            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        })

        model.loading.observe(this, { loading ->
            pbSpinner.visibility = if (loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        model.alertDialog.observe(this, {
            Toast.makeText(this, getString(R.string.event_check_in_success), Toast.LENGTH_LONG)
                .show()
            dialog.cancel()
        })
    }

    private fun getElements() {
        pbSpinner = findViewById(R.id.pb_spinner)
        mImage = findViewById(R.id.iv_image)
        mTitle = findViewById(R.id.tv_title)
        mDescription = findViewById(R.id.tv_description)
        mPrice = findViewById(R.id.tv_price)
        mDate = findViewById(R.id.tv_date)
        btnCheckIn = findViewById(R.id.btn_check_in)
    }

    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.event_dialog_question))
            .setPositiveButton(getString(R.string.event_dialog_yes), DialogInterface.OnClickListener { _, _ ->
                val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return@OnClickListener
                val userName = sharedPref.getString(getString(R.string.user_name), "")
                val userEmail = sharedPref.getString(getString(R.string.user_email), "")

                model.doCheckIn(eventId!!, userName!!, userEmail!!)
            })
            .setNegativeButton(getString(R.string.event_dialog_cancel),
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

        dialog = builder.create()
        dialog.show()
    }
}