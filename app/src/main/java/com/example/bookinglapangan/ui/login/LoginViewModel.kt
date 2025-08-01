package com.example.bookinglapangan.ui.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.bookinglapangan.data.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
    private val _loginResult = MutableLiveData<ResultState>()
    val loginResult: LiveData<ResultState> get() = _loginResult

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun logout() {
        sharedPreferences.edit().remove("TOKEN").apply()
    }

    fun loginUser(email: String, password: String) {
        _loginResult.postValue(ResultState.Loading)

        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val deviceId = context.getSharedPreferences("DEVICE_PREF", Context.MODE_PRIVATE)
                    .getString("DEVICE_ID", "android-unknown") ?: "android-unknown"

                val request = LoginRequest(email, password, deviceId)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    val token = loginResponse?.token
                    val user = loginResponse?.user

                    if (token != null) {
                        saveToken(token)
                        _loginResult.postValue(ResultState.Success(user?.name ?: "Login berhasil"))
                    } else {
                        _loginResult.postValue(ResultState.Error("Token tidak ditemukan dalam respons"))
                    }
                } else {
                    // ⬇️ Tambahkan log ini untuk melihat isi response error
                    val raw = try {
                        response.errorBody()?.string()
                    } catch (e: Exception) {
                        "Error parsing errorBody: ${e.message}"
                    }
                    Log.e("RESPONSE_RAW", raw ?: "null")
                    _loginResult.postValue(ResultState.Error("Login gagal: ${raw ?: "Unknown error"}"))
                }

            } catch (e: HttpException) {
                Log.e("LOGIN_EXCEPTION", "HttpException: ${e.message()}")
                _loginResult.postValue(ResultState.Error("Http error: ${e.message()}"))
            } catch (e: IOException) {
                Log.e("LOGIN_EXCEPTION", "IOException: ${e.message}")
                _loginResult.postValue(ResultState.Error("Tidak dapat terhubung ke server"))
            } catch (e: Exception) {
                Log.e("LOGIN_EXCEPTION", "Exception: ${e.message}")
                _loginResult.postValue(ResultState.Error("Unexpected error: ${e.message}"))
            }
        }
    }
}
