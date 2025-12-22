package kr.ac.kumoh.s20230625.tint_song2.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseApiConfig {
    const val PROJECT_URL = "https://meonznlfvhfpipubcpoj.supabase.co"
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lb256bmxmdmhmcGlwdWJjcG9qIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ4MTMxNzMsImV4cCI6MjA4MDM4OTE3M30.Vb7RVlBP1RPLbzuqMLiG6wZ08rI-pglBuYXObi-uUqY"
    const val SERVER_URL = "$PROJECT_URL/rest/v1/"

    private class SupabaseHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val req = chain.request().newBuilder()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            return chain.proceed(req)
        }
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(SupabaseHeaderInterceptor())
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: SupabaseService by lazy {
        retrofit.create(SupabaseService::class.java)
    }
}
