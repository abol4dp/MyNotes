package com.example.mynotes.mynotes.mynotes.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

private const val url = "https://notificator.ir/api/v1/"

private  var retrofit = Retrofit.Builder()
    .baseUrl(url)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    val apiServiec: ApiService = retrofit.create(ApiService::class.java)


}