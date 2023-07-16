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
import com.google.gson.Gson
import com.tharunbalaji.mvvm_notes_app.R
import com.tharunbalaji.mvvm_notes_app.databinding.FragmentNoteBinding
import com.tharunbalaji.mvvm_notes_app.models.NoteRequest
import com.tharunbalaji.mvvm_notes_app.models.NoteResponse
import com.tharunbalaji.mvvm_notes_app.utils.NetworkResult
import com.tharunbalaji.mvvm_notes_app.viewmodels.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NoteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            when(it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            if (note != null){
                noteViewModel.deleteNote(note!!._id)
            }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(description, title)

            if (note == null){  // New note creation
                noteViewModel.createNote(noteRequest)
            } else {  // Update existing note
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")

        if (jsonNote != null){ // Edit Note
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)
            binding.txtTitle.setText(note!!.title)
            binding.txtDescription.setText(note!!.description)
        }
        else {  // New Note
            binding.addEditText.text = "Add Note"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}