package com.example.noteapps.ui.home

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapps.databinding.ItemRvNoteBinding
import com.example.noteapps.data.local.entity.Notes

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listener: OnItemClickListener? = null
    var arrList = listOf<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRvNoteBinding.inflate(inflater, parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bindItem(arrList[position])
    }

    override fun getItemCount(): Int = arrList.size

    fun setData(arrNotesList: List<Notes>) {
        arrList = arrNotesList
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    inner class NotesViewHolder(private val binding: ItemRvNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Notes) {
            binding.apply {
                tvTitle.text = item.title
                tvDesc.text = item.noteText
                tvDateTime.text = item.dateTime
                if (item.color != null) {
                    cardViewItem.setBackgroundColor(Color.parseColor(item.color))
                } else {
                    cardViewItem.setBackgroundColor(Color.parseColor("#171c26"))
                }

                if (item.imgPath != null) {
                    imgNote.setImageBitmap(BitmapFactory.decodeFile(item.imgPath))
                    imgNote.visibility = View.VISIBLE
                } else {
                    imgNote.visibility = View.GONE
                }

                if (item.webLink != null) {
                    tvWebLink.text = item.webLink
                    tvWebLink.visibility = View.VISIBLE
                } else {
                    tvWebLink.visibility = View.GONE
                }

                cardViewItem.setOnClickListener {
                    listener?.onClicked(item.id!!)
                }

            }
        }
    }

    interface OnItemClickListener {
        fun onClicked(notesId: Int)
    }
}