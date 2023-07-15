package com.tharunbalaji.mvvm_notes_app.api

import com.tharunbalaji.mvvm_notes_app.models.NoteRequest
import com.tharunbalaji.mvvm_notes_app.models.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesAPI {
    @GET("/note")
    suspend fun getNotes(): Response<List<NoteResponse>>
    @POST("/note")
    suspend fun createNotes(@Body noteRequest: NoteRequest): Response<NoteResponse>
    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String, @Body noteRequest: NoteRequest): Response<NoteResponse>
    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>
}