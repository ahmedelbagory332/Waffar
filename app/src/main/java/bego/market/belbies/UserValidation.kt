package bego.market.belbies

import android.content.Context
import android.content.SharedPreferences




class UserValidation(private var context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("UserValidation",Context.MODE_PRIVATE)

    fun writeLoginStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("Login", status)
        editor.apply()
    }

    fun readLoginStatus(): Boolean {
        return sharedPreferences.getBoolean("Login", false)
    }
////////////////////////////////////////////////////////////////////////////////
    fun writeEmail(email: String) {
        val editor = sharedPreferences.edit()
        editor.putString("mail", email)
        editor.apply()
    }

    fun readEmail(): String {
        return sharedPreferences.getString("mail", "")!!
    }
///////////////////////////////////////////////////////////////////////////////
fun writeSplashStatus(status: Boolean) {
    val editor = sharedPreferences.edit()
    editor.putBoolean("SplashStatus", status)
    editor.apply()
}

    fun readSplashStatus(): Boolean {
        return sharedPreferences.getBoolean("SplashStatus", false)
    }
////////////////////////////////////////////////////////////////////////////////
    fun writeAddress(address: String) {
        val editor = sharedPreferences.edit()
        editor.putString("address", address)
        editor.apply()
    }

    fun readAddress(): String {
        return sharedPreferences.getString("address", "")!!
    }

    fun writePhone(phone: String) {
        val editor = sharedPreferences.edit()
        editor.putString("phone", phone)
        editor.apply()
    }

    fun readPhone(): String {
        return sharedPreferences.getString("phone", "")!!
    }
///////////////////////////////////////////////////////////////////////////////
fun writeLoginStatusAdmin(status: Boolean) {
    val editor = sharedPreferences.edit()
    editor.putBoolean("AdminLogin", status)
    editor.apply()
}

    fun readLoginStatusAdmin(): Boolean {
        return sharedPreferences.getBoolean("AdminLogin", false)
    }
////////////////////////////////////////////////////////////////////////////////
}