package com.example.coursach.network

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://zybouxssfrqdgisphvwt.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp5Ym91eHNzZnJxZGdpc3Bodnd0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzQxODQ3NzcsImV4cCI6MjA0OTc2MDc3N30.Sw7WQsPwnETGv2UtyQbAV2KYOhQlHq4Ztd1MUJoCrEM"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }
}