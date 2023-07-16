package com.tharunbalaji.mvvm_notes_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tharunbalaji.mvvm_notes_app.api.NotesAPI
import com.tharunbalaji.mvvm_notes_app.models.NoteRequest
import com.tharunbalaji.mvvm_notes_app.models.NoteResponse
import com.tharunbalaji.mvvm_notes_app.models.UserResponse
import com.tharunbalaji.mvvm_notes_app.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>> = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>> = _statusLiveData

    suspend fun getNotes() {
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.getNotes()
        if (response.isSuccessful && response.body() != null) {  // Success
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {  // Error occurred
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNotes(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }

    suspend fun updateNote(noteId: String,noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNote(noteId, noteRequest)
        handleResponse(response, "Note Updated")
    }

    private fun handleResponse(response: Response<NoteResponse>, successMsg: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(successMsg))
        } else {
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}