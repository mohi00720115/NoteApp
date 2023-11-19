package com.example.noteapps.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapps.R
import com.example.noteapps.data.local.entity.Notes
import com.example.noteapps.databinding.FragmentHomeBinding
import com.example.noteapps.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var notesAdapter: NotesAdapter
    private val viewModel: HomeViewModel by viewModels()
    var arrNotes = ArrayList<Notes>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!
        navController = findNavController()
        notesAdapter = NotesAdapter()

        binding.fabCreateNote.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createNoteFragment)
        }

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        getAllNotes()

        notesAdapter.setOnClickListener(onClicked)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean = true

            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<Notes>()
                for (arr in arrNotes) {
                    if (arr.title!!.toLowerCase(Locale.getDefault()).contains(p0.toString())) {
                        tempArr.add(arr)
                    }
                }
                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                //بستن کیبورد زمانی که کاربر روی سرچ کلیک کرد
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                return true
            }
        })
    }

    private val onClicked = object : NotesAdapter.OnItemClickListener {
        override fun onClicked(notesId: Int) {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(notesId)
            navController.navigate(action)
        }

    }

    private fun getAllNotes() {
        viewModel.getAllNotes()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllNote.collect {
                    notesAdapter.setData(it)
                    binding.recyclerView.adapter = notesAdapter
                }
            }
        }
    }

}