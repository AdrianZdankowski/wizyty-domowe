package com.medical.wizytydomowe

import android.content.Context
import com.medical.wizytydomowe.api.login.LoginResponse

class PreferenceManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("authToken", token)
        editor.apply()
    }

    fun saveRole(role: String){
        val editor = sharedPreferences.edit()
        editor.putString("role", role)
        editor.apply()
    }

    fun saveRefreshAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("refreshAuthToken", token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("authToken", null)
    }

    fun getRole(): String? {
        return sharedPreferences.getString("role", null)
    }

    fun clearAuthToken() {
        val editor = sharedPreferences.edit()
        editor.remove("authToken")
        editor.apply()
    }

    fun clearRole() {
        val editor = sharedPreferences.edit()
        editor.remove("role")
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return !getAuthToken().isNullOrEmpty()
    }

    fun setTokenAndRole(token: String?, refresh_token: String?, role: String?) : Boolean{
        if (token != null && refresh_token != null && role != null) {
            saveAuthToken(token)
            saveRefreshAuthToken(refresh_token)
            saveRole(role)
            return true
        }
        return false
    }
}
