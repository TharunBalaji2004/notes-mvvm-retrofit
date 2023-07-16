package com.tharunbalaji.mvvm_notes_app.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import com.tharunbalaji.mvvm_notes_app.R
import com.tharunbalaji.mvvm_notes_app.adapters.NoteAdapter
import com.tharunbalaji.mvvm_notes_app.api.NotesAPI
import com.tharunbalaji.mvvm_notes_app.databinding.FragmentMainBinding
import com.tharunbalaji.mvvm_notes_app.models.NoteResponse
import com.tharunbalaji.mvvm_notes_app.utils.NetworkResult
import com.tharunbalaji.mvvm_notes_app.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    @Inject
    lateinit var notesAPI: NotesAPI

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        noteAdapter = NoteAdapter(::onNoteClicked)  // Passing function as argument
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()

        noteViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = noteAdapter

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    private fun bindObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when(it) {
                is NetworkResult.Success -> {
                    noteAdapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun onNoteClicked(noteResponse: NoteResponse) {
        // Redirect fragment
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(noteResponse))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}