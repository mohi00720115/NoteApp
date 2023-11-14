package com.example.noteapps.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapps.R
import com.example.noteapps.databinding.FragmentCreateNoteBinding
import com.example.noteapps.local.db.NotesDatabase
import com.example.noteapps.local.entity.Notes
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.Date


class CreateNoteFragment : BaseFragment(), EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var navController: NavController
    private var currentDate: String? = null
    var selectedColor = "#171C26"
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var isEdit = ""
    private var noteId = -1
    val args: CreateNoteFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()
        with(binding) {


            if (args.noteId != -1) {
                launch {
                    context?.let {
                        var notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(args.noteId)
                        colorView.setBackgroundColor(Color.parseColor(notes.color))
                        etNoteTitle.setText(notes.title)
                        etNoteSubTitle.setText(notes.subTitle)
                        etNoteDesc.setText(notes.noteText)
                        if (!notes.imgPath.isNullOrEmpty()) {
                            imgNote.setImageBitmap(BitmapFactory.decodeFile(notes.imgPath))
                            imgNote.visibility = View.VISIBLE
                        } else {
                            imgNote.visibility = View.GONE
                        }

                        if (!notes.webLink.isNullOrEmpty()) {
                            tvWebLink.text = notes.webLink
                            tvWebLink.visibility = View.VISIBLE
                        } else {
                            tvWebLink.visibility = View.GONE
                        }
                    }
                }
            }


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

            btnOk.setOnClickListener {
                if (etWebLink.text.toString().trim().isNotEmpty()) {
                    checkWebUrl()
                } else {
                    showToast("Url is Required")
                }
            }

            btnCancel.setOnClickListener {
                layoutWebUrl.visibility = View.GONE
            }

            tvWebLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(etWebLink.text.toString()))
                startActivity(intent)
            }

        }

    }

    private fun saveNote() {
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

                    }
                }
            }
        }
    }

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
            val actionColor = p1!!.getStringExtra("action")

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

                "Image" -> {
                    readStorageTask()
                    binding.layoutWebUrl.visibility = View.GONE
                }

                "WebUrl" -> {
                    binding.layoutWebUrl.visibility = View.VISIBLE
                }

                else -> {
                    binding.imgNote.visibility = View.GONE
                    binding.layoutWebUrl.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    binding.colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun showToast(toastText: String) = Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()

    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasWriteStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                var selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imgNote.setImageBitmap(bitmap)
                        binding.imgNote.visibility = View.VISIBLE

//                        binding.ll1.visibility = View.VISIBLE
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
}