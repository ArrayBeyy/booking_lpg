package com.example.bookinglapangan

import android.app.Application
import android.content.Context

class BookingApp : Application() {
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
