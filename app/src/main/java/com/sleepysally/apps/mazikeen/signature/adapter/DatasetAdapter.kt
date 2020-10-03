package com.sleepysally.apps.mazikeen.signature.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sleepysally.apps.mazikeen.databinding.ListItemDatasetBinding
import com.sleepysally.apps.mazikeen.signature.model.DatasetModel

class DatasetAdapter(val datasets: List<DatasetModel>, var onItemClick: ((item: DatasetModel, position: Int) -> Unit)? = null): RecyclerView.Adapter<DatasetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(parent.context)
        return ViewHolder(
            ListItemDatasetBinding.inflate(li, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val item = datasets[i]
        holder.binding.tvDatasetName.text = item.name
        holder.binding.tvDatasetId.text = item.id
        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(item, i)
        }
    }

    override fun getItemCount(): Int = datasets.size

    class ViewHolder(var binding: ListItemDatasetBinding): RecyclerView.ViewHolder(binding.root) {
        // ...
    }
}