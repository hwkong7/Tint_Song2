package kr.ac.kumoh.s20230625.tint_song2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SupabaseApiConfig {
    const val PROJECT_URL = "https://meonznlfvhfpipubcpoj.supabase.co"
    const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lb256bmxmdmhmcGlwdWJjcG9qIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjQ4MTMxNzMsImV4cCI6MjA4MDM4OTE3M30.Vb7RVlBP1RPLbzuqMLiG6wZ08rI-pglBuYXObi-uUqY"
    const val SERVER_URL = "$PROJECT_URL/rest/v1/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val service: SupabaseService by lazy {
        retrofit.create(SupabaseService::class.java)
    }
}