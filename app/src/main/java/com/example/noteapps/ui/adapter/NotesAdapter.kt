package com.example.noteapps.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapps.R
import com.example.noteapps.databinding.ItemRvNoteBinding
import com.example.noteapps.local.entity.Notes

class NotesAdapter(private var arrList: List<Notes>) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRvNoteBinding.inflate(inflater, parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bindItem(arrList[position])
    }

    override fun getItemCount(): Int = arrList.size


    class NotesViewHolder(private val binding: ItemRvNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Notes) {
            binding.apply {
                tvTitle.text = item.title
                tvDesc.text = item.noteText
                tvDateTime.text = item.dateTime
                if (item.color != null) {
                    cardViewItem.setBackgroundColor(Color.parseColor(item.color))
                } else {
                    cardViewItem.setBackgroundColor(Color.parseColor(R.color.colorLightBlack.toString()))
                }
            }
        }
    }
}