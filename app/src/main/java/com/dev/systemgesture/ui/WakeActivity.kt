package com.dev.systemgesture.ui

import android.app.Activity
import android.os.Bundle

class WakeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        finish()
    }
}
