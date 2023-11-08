package com.example.noteapps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.noteapps.databinding.FragmentCreateNoteBinding
import com.example.noteapps.databinding.FragmentHomeBinding


class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()

    }
}