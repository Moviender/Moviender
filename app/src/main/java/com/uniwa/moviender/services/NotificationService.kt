package com.uniwa.moviender.services

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uniwa.moviender.R
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.*

private const val CHANNEL_ID = "1"

class NotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val user = FirebaseAuth.getInstance().currentUser

        user?.let {
            CoroutineScope(Dispatchers.IO).launch {
                MovienderApi.movienderApiService.storeToken(user.uid, token)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        createNotificationChannels()

        postNotification(message)
    }

    private fun postNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_close_24)
            .setContentTitle("New friend request")
            .setContentText(message.data["name"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, builder)
    }

    private fun createNotificationChannels() {
        val name = "Notifications"
        val description = "sd"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            this.description = description
            enableLights(true)
            enableVibration(true)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}