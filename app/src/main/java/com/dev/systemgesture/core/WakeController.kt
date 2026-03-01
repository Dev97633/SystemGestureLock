package com.dev.systemgesture.core

import android.content.Context
import android.content.Intent
import com.dev.systemgesture.ui.WakeActivity

object WakeController {

    fun wake(context: Context) {
        val intent = Intent(context, WakeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
