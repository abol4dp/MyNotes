package com.example.mynotes.mynotes.mynotes.retrofit

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRepository private constructor() {

    companion object {

        private var apiRepository: ApiRepository? = null
        val inestance: ApiRepository
            get() {


                if (apiRepository == null) apiRepository = ApiRepository()
                return apiRepository!!
            }

    }

    fun sendText(
        to: String,
        text: String
    ) {

        RetrofitService.apiServiec.SendTextToTelegram(to, text)
            .enqueue(

                object : Callback<MainModel> {

                    override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {

                        if (response.isSuccessful) {
                            val data = response.body() as MainModel
                            val message = data.massage // اطمینان از این که message مقدار null ندارد
                            if (message != null) {
                                Log.i("ResponseSuccess", message) // یک برچسب معتبر استفاده کنید
                            } else {
                                Log.i("ResponseSuccess", "Message is null") // مدیریت حالت null
                            }
                        }

                    }

                    override fun onFailure(call: Call<MainModel>, t: Throwable) {

                    }


                }


            )


    }


}