package com.example.noteapps.ui.createnote

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapps.R
import com.example.noteapps.databinding.FragmentCreateNoteBinding
import com.example.noteapps.util.Const.SELECTED_COLOR
import com.example.noteapps.util.clearFields
import com.example.noteapps.util.goneLayout
import com.example.noteapps.util.showToast
import com.example.noteapps.util.visibleLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class CreateNoteFragment : Fragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var navController: NavController
    private val viewModel: CreateNoteViewModel by viewModels()
    private var currentDate: String? = null
    var selectedColor = "#171C26"
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var isEdit = ""
    private var noteId = -1
    private val args: CreateNoteFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        with(binding) {

            LocalBroadcastManager.getInstance(requireContext())
                .registerReceiver(broadcastReceiver, IntentFilter("bottom_sheet_action"))
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            currentDate = sdf.format(Date())
            colorView.setBackgroundColor(Color.parseColor(selectedColor))
            tvDateTime.text = currentDate

            imgDone.setOnClickListener {
                if (args.noteId != -1) {
                    updateNote2()
                    Log.e(TAG, "updateNote2: ${args.noteId}")
                } else {
                    saveNote2()
                    Log.e(TAG, "saveNote2: ${args.noteId}")
                }
                navController.popBackStack()
            }

            imgBack.setOnClickListener {
//                navController.navigate(R.id.action_createNoteFragment_to_homeFragment)
                navController.popBackStack()
            }

            imgMore.setOnClickListener {
                val action = CreateNoteFragmentDirections.actionCreateNoteFragmentToNoteBottomSheetFragment(args.noteId)
                navController.navigate(action)
            }

            btnOk.setOnClickListener {
                if (etWebLink.text.toString().trim().isNotEmpty()) {
                    checkWebUrl()
                } else {
                    showToast("Url is Required")
                }
            }

            imgDelete.setOnClickListener {
                selectedImagePath = ""
                layoutImage.visibility = View.GONE
            }

            btnCancel.setOnClickListener {
                if (args.noteId != -1) {
                    tvWebLink.visibility = View.VISIBLE
                    layoutWebUrl.visibility = View.GONE
                } else {
                    selectedImagePath = ""
                    layoutWebUrl.visibility = View.GONE
                }
            }

            imgUrlDelete.setOnClickListener {
                webLink = ""
                goneLayout(tvWebLink, imgUrlDelete)
            }

            imgUrlCancel.setOnClickListener {
                webLink = ""
                goneLayout(tvWebLink, imgUrlCancel, layoutImage)
            }

            tvWebLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(etWebLink.text.toString()))
                startActivity(intent)
            }
        }
    }

    private fun deleteNote2() {
        viewModel.deleteSpecificNote(args.noteId)
    }


    /*private fun updateNote() =
        launch {
            with(binding) {
                context?.let {
                    val notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(args.noteId)
                    notes.title = etNoteTitle.text.toString()
                    notes.subTitle = etNoteSubTitle.text.toString()
                    notes.noteText = etNoteDesc.text.toString()
                    notes.dateTime = currentDate
                    notes.color = selectedColor
                    notes.imgPath = selectedImagePath
                    notes.webLink = webLink
                    NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                    //خالی کردن ادیت تکست ها بعد از وارد کردن متن هامون
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")
                    imgNote.visibility = View.GONE
                    tvWebLink.visibility = View.GONE
                    layoutImage.visibility = View.GONE
                }
            }
        }*/

    /*    private fun saveNote() {
            binding.apply {
                if (etNoteTitle.text.isNullOrEmpty()) {
                    showToast("Note Title is Required")
                } else if (etNoteSubTitle.text.isNullOrEmpty()) {
                    showToast("Note Sub Title is Required")
                } else if (etNoteDesc.text.isNullOrEmpty()) {
                    showToast("Note Description is Required")
                } else {
                    launch {
                        val notes = Notes()
                        notes.title = etNoteTitle.text.toString()
                        notes.subTitle = etNoteSubTitle.text.toString()
                        notes.noteText = etNoteDesc.text.toString()
                        notes.dateTime = currentDate
                        notes.color = selectedColor
                        notes.imgPath = selectedImagePath
                        notes.webLink = webLink
                        context?.let {
                            NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                            //خالی کردن ادیت تکست ها بعد از وارد کردن متن هامون
                            etNoteTitle.setText("")
                            etNoteSubTitle.setText("")
                            etNoteDesc.setText("")
                            imgNote.visibility = View.GONE
                            tvWebLink.visibility = View.GONE
                            layoutImage.visibility = View.GONE

                        }
                    }
                }
            }
        }*/

    private fun checkWebUrl() {
        if (Patterns.WEB_URL.matcher(binding.etWebLink.text.toString()).matches()) {
            binding.layoutWebUrl.visibility = View.GONE
            binding.etWebLink.isEnabled = false
            webLink = binding.etWebLink.text.toString()
            binding.tvWebLink.visibility = View.VISIBLE
            binding.tvWebLink.text = binding.etWebLink.text.toString()
        } else {
            showToast("Url is not valid")
        }
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            with(binding) {
                val actionColor = p1!!.getStringExtra("action")

                when (actionColor!!) {

                    "Blue" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Yellow" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Purple" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Green" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Orange" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Black" -> {
                        getStringAndSetBackground(p1, colorView)
                    }

                    "Image" -> {
                        readStorageTask()
                        goneLayout(layoutWebUrl)
                    }

                    "WebUrl" -> {
                        visibleLayout(layoutWebUrl)
                    }

                    "DeleteNote" -> {
                        deleteNote2()
                        //TODO -> Crash
//                        navController.navigate(R.id.action_createNoteFragment_to_homeFragment)
                        navController.popBackStack()
                    }

                    else -> {
                        getStringAndSetBackground(p1, colorView)
                        goneLayout(imgNote, layoutWebUrl, layoutImage)
                    }
                }
            }
        }
    }

    private fun getStringAndSetBackground(intent: Intent, view: View) {
        selectedColor = intent.getStringExtra(SELECTED_COLOR).toString()
        view.setBackgroundColor(Color.parseColor(selectedColor))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /**
     * میگه اگر برناممون پرمیژن داشت که بیاد یه کاری بکنه اما اگر نداشت بیاد پرمیژن رو درخواست کنه
     */
    private fun readStorageTask() {
        if (hasReadStoragePerm()) {
            showToast("Permission Granted")
            pickImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                val selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imgNote.setImageBitmap(bitmap)
                        binding.imgNote.visibility = View.VISIBLE
                        binding.layoutImage.visibility = View.VISIBLE
                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    } catch (e: Exception) {
                        showToast(e.message!!)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, requireActivity())
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

    private fun specificNote() {
        viewModel.specificNote(args.noteId)
        if (args.noteId != -1) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.specificNote.collect { notes ->
                        with(binding) {
                            colorView.setBackgroundColor(Color.parseColor(notes.color))
                            clearFields(etNoteTitle, etNoteSubTitle, etNoteDesc)
                            if (!notes.imgPath.isNullOrEmpty() || !notes.webLink.isNullOrEmpty()) {
                                selectedImagePath = notes.imgPath!!
                                imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.imgPath))
                                webLink = notes.webLink!!
                                tvWebLink.text = notes.webLink
                                etWebLink.setText(notes.webLink)
                                visibleLayout(layoutImage, imgNote, imgDelete, layoutWebUrl, imgUrlCancel)
                            } else {
                                goneLayout(layoutImage, imgNote, imgDelete, layoutWebUrl, imgUrlCancel)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateNote2(): Job {
//        specificNote()
        return viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.specificNote.collect { notes ->
                    specificNote()
                    with(binding) {
                        notes.title = etNoteTitle.text.toString()
                        notes.subTitle = etNoteSubTitle.text.toString()
                        notes.noteText = etNoteDesc.text.toString()
                        notes.dateTime = currentDate
                        notes.color = selectedColor
                        notes.imgPath = selectedImagePath
                        notes.webLink = webLink
                        viewModel!!.updateNote(notes)
                        //خالی کردن ادیت تکست ها بعد از وارد کردن متن هامون
                        clearFields(etNoteTitle, etNoteSubTitle, etNoteDesc)
                        goneLayout(imgNote, tvWebLink, layoutImage)
                    }
                }
            }
        }
    }

    private fun saveNote2() {
        with(binding) {
            if (!viewModel!!.check()) {
                showToast("لطفا تمام مقادیر را پر کنید")
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel!!.specificNote.collect { notes ->
                            viewModel!!.insertNotes(notes)
                            notes.title = etNoteTitle.text.toString()
                            notes.subTitle = etNoteSubTitle.text.toString()
                            notes.noteText = etNoteDesc.text.toString()
                            Log.e(TAG, "T=${notes.title} ")
                            Log.e(TAG, "S=${notes.subTitle} ")
                            Log.e(TAG, "D=${notes.noteText} ")
                            notes.dateTime = currentDate
                            notes.color = selectedColor
                            notes.imgPath = selectedImagePath
                            notes.webLink = webLink
                            clearFields(etNoteTitle, etNoteSubTitle, etNoteDesc)
                            goneLayout(imgNote, tvWebLink, layoutImage)
                        }
                    }
                }
            }

        }
    }
}