package com.isaccobertoli.services;

import com.isaccobertoli.dto.auth.LoginRequest;
import com.isaccobertoli.dto.auth.RegisterRequest;
import com.isaccobertoli.dto.note.CreateNoteRequest;
import com.isaccobertoli.dto.note.NoteDetailResponse;
import com.isaccobertoli.dto.note.NoteListResponse;
import com.isaccobertoli.dto.GenericResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/sign-in")
    Call<GenericResponse<String>> login(@Body LoginRequest loginRequest);

    @POST("auth/sign-up")
    Call<GenericResponse<String>> register(@Body RegisterRequest registerRequest);

    @POST("auth/reset-password/{email}")
    Call<GenericResponse<String>> passwordReset(@Path("email") String email);

    @GET("notes")
    Call<GenericResponse<NoteListResponse>> getNotes();

    @GET("notes/{id}")
    Call<GenericResponse<NoteDetailResponse>> getNoteById(@Path("id") String noteId);

    @POST("notes")
    Call<GenericResponse<String>> createNote(@Body CreateNoteRequest createNoteRequest);

    @PUT("notes/{id}")
    Call<GenericResponse<String>> updateNote(@Path("id") String noteId, @Body CreateNoteRequest createNoteRequest);

    @DELETE("notes/{id}")
    Call<GenericResponse<String>> deleteNote(@Path("id") String noteId);
}
