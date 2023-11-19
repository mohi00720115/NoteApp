package com.example.noteapps.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapps.R
import com.example.noteapps.databinding.FragmentNotesBottomSheetBinding
import com.example.noteapps.util.Const.ACTION
import com.example.noteapps.util.Const.BOTTOM_SHEET_ACTION
import com.example.noteapps.util.Const.SELECTED_COLOR
import com.example.noteapps.util.goneLayout
import com.example.noteapps.util.imgSetImageResource
import com.example.noteapps.util.visibleLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotesBottomSheetBinding
    private lateinit var navController: NavController
    private val args: NoteBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet, container, false)
    }

    private var selectedColor = "#171C26"

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet, null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior
        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    TODO("Note yet impl")
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }

                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }

                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()
        binding.apply {
            if (args.noteId != -1) {
                visibleLayout(layoutDeleteNote)
            } else {
                goneLayout(layoutDeleteNote)
            }
        }
        setListener()

    }

    private fun setListener() {
        with(binding) {
            fNote1.setOnClickListener {
                imgSetImageResource(imgNote2, imgNote4, imgNote5, imgNote6, imgNote7)
                imgNote1.setImageResource(R.drawable.ic_tick)
                selectedColor = "#4E33FF"
                intent(valAction = "Blue", valSelected = selectedColor)
            }

            fNote2.setOnClickListener {
                imgSetImageResource(imgNote1, imgNote4, imgNote5, imgNote6, imgNote7)
                imgNote2.setImageResource(R.drawable.ic_tick)
                selectedColor = "#FFD633"
                intent(valAction = "Yellow", valSelected = selectedColor)
            }

            fNote4.setOnClickListener {
                imgSetImageResource(imgNote1, imgNote2, imgNote5, imgNote6, imgNote7)
                imgNote4.setImageResource(R.drawable.ic_tick)
                selectedColor = "#AE3B76"
                intent(valAction = "Purple", valSelected = selectedColor)
            }

            fNote5.setOnClickListener {
                imgSetImageResource(imgNote1, imgNote2, imgNote4, imgNote6, imgNote7)
                imgNote5.setImageResource(R.drawable.ic_tick)
                selectedColor = "#0AEBAF"
                intent(valAction = "Green", valSelected = selectedColor)
            }

            fNote6.setOnClickListener {
                imgSetImageResource(imgNote1, imgNote2, imgNote4, imgNote5, imgNote7)
                imgNote6.setImageResource(R.drawable.ic_tick)
                selectedColor = "#FF7746"
                intent(valAction = "Orange", valSelected = selectedColor)
            }

            fNote7.setOnClickListener {
                imgSetImageResource(imgNote1, imgNote2, imgNote4, imgNote5, imgNote6)
                imgNote7.setImageResource(R.drawable.ic_tick)
                selectedColor = "#202734"
                intent(valAction = "Black", valSelected = selectedColor)
            }

            layoutImage.setOnClickListener {
                intent(valAction = "Image")
            }

            layoutWebUrl.setOnClickListener {
                intent(valAction = "WebUrl")
            }

            layoutDeleteNote.setOnClickListener {
                intent(valAction = "DeleteNote")
            }
        }
    }
}

private fun BottomSheetDialogFragment.intent(
    keyAction: String = ACTION,
    keySelectedColor: String = SELECTED_COLOR,
    valAction: String,
    valSelected: String = ""
) {
    val intent = Intent(BOTTOM_SHEET_ACTION)
    intent.putExtra(keyAction, valAction)
    intent.putExtra(keySelectedColor, valSelected)
    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
    dismiss()
}