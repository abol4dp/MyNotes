package com.example.mynotes.mynotes.mynotes.retrofit

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mynotes.mynotes.mynotes.retrofit.MainModel as MainModel


interface ApiService {
    @GET("send")
    fun SendTextToTelegram(
@Query("to") token :String,
@Query("text")  massage : String


    ):retrofit2.Call<MainModel>


}