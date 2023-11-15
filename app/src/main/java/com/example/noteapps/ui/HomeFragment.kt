package com.example.noteapps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapps.R
import com.example.noteapps.databinding.FragmentHomeBinding
import com.example.noteapps.local.db.NotesDatabase
import com.example.noteapps.local.entity.Notes
import com.example.noteapps.ui.adapter.NotesAdapter
import com.example.noteapps.ui.adapter.NotesAdapter.OnItemClickListener
import kotlinx.coroutines.launch
import java.util.Locale


class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var notesAdapter: NotesAdapter
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

        launch {
            context?.let {
                val notes = NotesDatabase.getDatabase(it).noteDao().getAllNotes()
                binding.recyclerView.adapter = notesAdapter
                notesAdapter.setData(notes)
                arrNotes = notes as ArrayList<Notes>
            }
        }

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
                return true
            }
        })
    }

    fun setOnClickListener(listener1: OnItemClickListener) {

    }

    private val onClicked = object : NotesAdapter.OnItemClickListener {
        override fun onClicked(notesId: Int) {
            val action = HomeFragmentDirections.actionHomeFragmentToCreateNoteFragment(notesId)
            navController.navigate(action)
        }

    }

}