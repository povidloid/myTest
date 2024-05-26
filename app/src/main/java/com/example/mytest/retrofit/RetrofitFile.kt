package com.example.mytest.retrofit

import com.example.mytest.mainScreens.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TestAPI{
    @GET("/tests/{id}")
    suspend fun getTest(@Path("id") id: Int): Test
    @POST("/tests")
    suspend fun saveTest(@Body test: Test) : Int
}

object RetrofitObject{
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.164.124:8080")  // для физического устройства
//        .baseURL("http://10.0.2.2:8080")  // для эмулятора
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val userApi = retrofit.create(TestAPI::class.java)
}