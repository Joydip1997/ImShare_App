package com.androdude.myapplication.data.network

import com.androdude.myapplication.data.ResponseData
import com.androdude.myapplication.utils.Constants.BASE_URL
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadApi {

    @Multipart
    @POST("api/file")
    fun upload(
        @Part image : MultipartBody.Part,
        @Part("myfile") myfile : String
    ) : Call<ResponseData>


    companion object
    {
        operator fun invoke() : ImageUploadApi
        {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ImageUploadApi::class.java)
        }
    }
}