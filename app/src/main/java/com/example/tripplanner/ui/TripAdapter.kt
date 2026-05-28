package com.example.tripplanner.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tripplanner.R
import com.example.tripplanner.data.Trip

class TripAdapter(
    private val onItemClick: (Trip) -> Unit,
    private val onDeleteClick: (Trip) -> Unit
) : ListAdapter<Trip, TripAdapter.TripViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemClick, onDeleteClick)
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDestination: TextView = itemView.findViewById(R.id.textViewDestination)
        private val tvDates: TextView = itemView.findViewById(R.id.textViewDates)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(trip: Trip, clickListener: (Trip) -> Unit, deleteListener: (Trip) -> Unit) {
            tvDestination.text = trip.destination
            tvDates.text = "${trip.startDate} - ${trip.endDate}"

            itemView.setOnClickListener { clickListener(trip) }
            btnDelete.setOnClickListener { deleteListener(trip) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean = oldItem == newItem
    }
}