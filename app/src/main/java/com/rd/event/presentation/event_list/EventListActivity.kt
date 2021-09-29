package com.rd.event.presentation.event_list

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rd.event.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventListActivity : AppCompatActivity() {

    private val model: EventListViewModel by viewModel()
    private lateinit var pbSpinner: ProgressBar
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        pbSpinner = findViewById(R.id.pb_spinner)

        model.error.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
        model.events.observe(this, { events ->
            if (events.isEmpty()) {
                Toast.makeText(this, "EMPTY", Toast.LENGTH_LONG).show()
            } else {
                val adapter = events?.let { EventListAdapter(it) }
                recyclerView.adapter = adapter
            }
        })
        model.loading.observe(this, { loading ->
            pbSpinner.visibility = if (loading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }
}