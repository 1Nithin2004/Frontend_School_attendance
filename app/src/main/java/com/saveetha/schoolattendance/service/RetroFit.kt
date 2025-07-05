package com.saveetha.schoolattendance.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.saveetha.schoolattendance.service.response.ReportResponse

    object RetroFit{
        private const val BASE_URL = "https://grlp1vvl-3000.inc1.devtunnels.ms/"

        private fun getClient(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
//                .addInterceptor(AuthInterceptor(token))
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getService() :ApiService{
            return getClient().create(ApiService::class.java)
        }
    }

