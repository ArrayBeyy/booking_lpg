package com.example.bookinglapangan.ui.booking

import android.content.Context
import com.example.bookinglapangan.ui.booking.Booking
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object BookingStorage {
    private const val PREF_NAME = "booking_history"
    private const val KEY_HISTORY = "history"

    fun saveBooking(context: Context, booking: Booking) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existingJson = prefs.getString(KEY_HISTORY, null)
        val listType = object : TypeToken<MutableList<Booking>>() {}.type
        val bookingList: MutableList<Booking> = if (existingJson != null) {
            Gson().fromJson(existingJson, listType)
        } else {
            mutableListOf()
        }

        bookingList.add(booking)
        val newJson = Gson().toJson(bookingList)
        prefs.edit().putString(KEY_HISTORY, newJson).apply()
    }

    fun getBookings(context: Context): List<Booking> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        val listType = object : TypeToken<List<Booking>>() {}.type
        return Gson().fromJson(json, listType)
    }
}
