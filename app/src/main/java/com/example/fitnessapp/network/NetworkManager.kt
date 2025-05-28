package com.example.fitnessapp.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object NetworkManager {
    private lateinit var requestQueue: RequestQueue
    private const val BASE_URL = "https://dreamarray.com/api"

    fun init(context: Context) {
        requestQueue = Volley.newRequestQueue(context)
    }

    fun signUp(username: String, email: String, password: String, callback: (Boolean, String) -> Unit) {
        val url = "$BASE_URL/signup.php"
        val params = JSONObject().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }
        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "")
                callback(success, message)
            },
            { error ->
                callback(false, error.localizedMessage ?: "Unknown error")
            }
        )
        requestQueue.add(request)
    }

    fun login(username: String, password: String, callback: (Boolean, Int, String) -> Unit) {
        val url = "$BASE_URL/login.php"
        val params = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                val success = response.optBoolean("success", false)
                val userId = response.optInt("userId", 0)
                val userName = response.optString("userName", "")
                if (success) {
                    callback(true, userId, userName)
                } else {
                    val message = response.optString("message", "Login failed. Please check your credentials.")
                    callback(false, 0, message)
                }
            },
            { error ->
                callback(false, 0, error.localizedMessage ?: "Unknown error")
            }
        )
        requestQueue.add(request)
    }
    fun saveActivity(
        userId: Int,
        activityType: String,
        stepCount: Int,
        startTime: String,
        endTime: String,
        callback: (Boolean, String) -> Unit
    ) {
        val url = "$BASE_URL/saveActivity.php"
        val params = JSONObject().apply {
            put("user_id", userId)
            put("activity_type", activityType)
            put("step_count", stepCount)
            put("start_time", startTime)
            put("end_time", endTime)
        }
        val request = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "")
                callback(success, message)
            },
            { error ->
                callback(false, error.localizedMessage ?: "Unknown error")
            }
        )
        requestQueue.add(request)
    }
    fun updateProfile(
        userId: Int,
        name: String,
        gender: String,
        age: Int,
        weight: Float,
        height: Float,
        callback: (Boolean, String) -> Unit
    ) {
        val url = "$BASE_URL/update_profile.php"
        val params = JSONObject().apply {
            put("userId", userId)
            put("name", name)
            put("gender", gender)
            put("age", age)
            put("weight", weight)
            put("height", height)
        }
        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "")
                callback(success, message)
            },
            { error ->
                callback(false, error.localizedMessage ?: "Unknown error")
            }
        )
        requestQueue.add(request)
    }
    fun readProfile(userId: Int, callback: (Boolean, String, String, String, String, String) -> Unit) {
        val url = "$BASE_URL/read_profile.php"
        val params = JSONObject().apply {
            put("userId", userId)
        }
        val request = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                val success = response.optBoolean("success", false)
                if (success) {
                    val user = response.optJSONObject("user")
                    val name = user?.optString("username", "") ?: ""
                    val gender = user?.optString("gender", "") ?: ""
                    val age = user?.optString("age", "") ?: ""
                    val weight = user?.optString("weight", "") ?: ""
                    val height = user?.optString("height", "") ?: ""
                    callback(true, name, gender, age, weight, height)
                } else {
                    val message = response.optString("message", "Failed to load profile.")
                    callback(false, message, "", "", "", "")
                }
            },
            { error ->
                callback(false, error.localizedMessage ?: "Unknown error", "", "", "", "")
            }
        )
        requestQueue.add(request)
    }
}