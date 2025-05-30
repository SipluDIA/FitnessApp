package com.example.fitnessapp.network

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
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
    fun readProfile(userId: Int, callback: (Boolean, String, String, String, String, String, String?) -> Unit) {
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
                    val profileImageUrl = user?.optString("profile_image_url", null)
                    callback(true, name, gender, age, weight, height, profileImageUrl)
                } else {
                    val message = response.optString("message", "Failed to load profile.")
                    callback(false, message, "", "", "", "", null)
                }
            },
            { error ->
                callback(false, error.localizedMessage ?: "Unknown error", "", "", "", "", null)
            }
        )
        requestQueue.add(request)
    }
    fun uploadProfileImage(userId: Int, imageUri: Uri, context: Context, callback: (Boolean, String?) -> Unit) {
        val url = "$BASE_URL/upload_profile_image.php"
        val request = object : VolleyMultipartRequest(Method.POST, url,
            Response.Listener { response ->
                val json = JSONObject(String(response.data))
                val success = json.optBoolean("success", false)
                val imageUrl = json.optString("imageUrl", null)
                callback(success, imageUrl)
            },
            Response.ErrorListener { error ->
                callback(false, error.localizedMessage)
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = userId.toString()
                return params
            }
            override fun getByteData(): Map<String, DataPart> {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes() ?: ByteArray(0)
                val fileName = "profile_${userId}.jpg"
                return mapOf("profile_image" to DataPart(fileName, bytes, "image/jpeg"))
            }
        }
        requestQueue.add(request)
    }
}