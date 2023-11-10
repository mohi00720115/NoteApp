package com.example.noteapps.ui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.noteapps.R
import com.example.noteapps.databinding.FragmentCreateNoteBinding
import com.example.noteapps.local.db.NotesDatabase
import com.example.noteapps.local.entity.Notes
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class CreateNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var navController: NavController
    private var currentDate: String? = null
    var selectedColor = "#171C26"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()
        with(binding) {
            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(broadcastReceiver, IntentFilter("bottom_sheet_action"))
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            currentDate = sdf.format(Date())
            colorView.setBackgroundColor(Color.parseColor(selectedColor))
            tvDateTime.text = currentDate
            imgDone.setOnClickListener {
                saveNote()
            }
            imgBack.setOnClickListener {
                navController.navigate(R.id.action_createNoteFragment_to_homeFragment)
            }
            imgMore.setOnClickListener {
                navController.navigate(R.id.action_createNoteFragment_to_noteBottomSheetFragment)
            }


        }

    }

    private fun saveNote() {
        binding.apply {
            if (etNoteTitle.text.isNullOrEmpty()) {
                showToast("Note Title is Required")
            }
            if (etNoteSubTitle.text.isNullOrEmpty()) {
                showToast("Note Sub Title is Required")
            }
            if (etNoteDesc.text.isNullOrEmpty()) {
                showToast("Note Description is Required")
            }
            launch {
                val notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subTitle = etNoteSubTitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
                context?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    //خالی کردن ادیت تکست ها بعد از وارد کردن متن هامون
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")
                }
            }

        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val actionColor = p1!!.getStringExtra("actionColor")

            when (actionColor!!) {
                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

                else -> {}
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun showToast(toastText: String) = Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
}