package com.translized.translized_ota.api

import com.translized.translized_ota.model.OtaResult
import com.translized.translized_ota.model.RequestCheckOta
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * TODO: Add type description!
 *
 * Created by nikolatomovic on 6.1.22..
 * Copyright © 2022 aktiia SA. All rights reserved.
 */

interface ApiInterface {

    @POST("checkOTA")
    fun checkOta(@Body requestCheckOta: RequestCheckOta) : Call<OtaResult>

    @GET
    fun downloadFile(@Url url: String) : Call<ResponseBody>

    companion object {

        var BASE_URL = "https://translized.eu-4.evennode.com/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}