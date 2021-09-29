package com.rd.event.presentation.event_list

import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.rd.event.R
import com.rd.event.domain.model.Event
import com.rd.event.presentation.event_detail.EventDetailActivity

class EventListAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImage: ImageView = view.findViewById(R.id.iv_image)
        val mTitle: TextView = view.findViewById(R.id.tv_title)
        val mDate: TextView = view.findViewById(R.id.tv_date)
        val mPrice: TextView = view.findViewById(R.id.tv_price)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.event_list_item, viewGroup, false)
        context = viewGroup.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (events[position].image.isNotBlank()) {
            viewHolder.mImage.load(events[position].image) {
                crossfade(true)
                placeholder(R.drawable.image)
                transformations(CircleCropTransformation())
                error(R.drawable.image)
            }
        }
        viewHolder.mTitle.text = events[position].title
        viewHolder.mDate.text = context.getString(R.string.event_date, events[position].date)
        viewHolder.mPrice.text = if (events[position].price.isNotBlank()) {
            context.getString(R.string.event_price, events[position].price)
        } else {
            context.getString(R.string.event_price_free)
        }

        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.mTitle.context
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("id", events[position].id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = events.size
}