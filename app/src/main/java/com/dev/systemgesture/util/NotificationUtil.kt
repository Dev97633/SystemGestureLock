object NotificationUtil {

    fun create(context: Context): Notification {
        val channelId = "gesture_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "System Gesture",
                NotificationManager.IMPORTANCE_MIN
            )
            val manager =
                context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("System Gesture Active")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }
}
