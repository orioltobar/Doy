package com.napptilians.networkdatasource.api.data

import com.napptilians.domain.models.push.ChatSenderModel
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseService {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAA5vPn8z0:APA91bFRR0VIjQ1VEhCM1q0nYPtdXk93IsrvONkgxahmN8d5vrMEE-7jpZs11_AZf2esPC53QQRQPrrhK02c9rcH56QRLc5RxJUK2vTzkWDyNX6k3w09KzT-iNFzwUU0IqkfpCZp6mfo"
    )
    @POST("fcm/send")
    suspend fun sendNotification(
        @Body sender: ChatSenderModel
    ): Unit
}
