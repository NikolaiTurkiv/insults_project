package com.example.insult.api

import com.example.insult.pojo.Insult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInsultServes {

    @GET("generate_insult.php?")
    fun getInsult(
        @Query("lang") lang: String,
        @Query("type") type: String = "json"
    ): Call<Insult>

}