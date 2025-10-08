package com.example.fitnessapp.network

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.launch
import org.json.JSONObject

object NetworkManager {
    /**
     * Fetches the latest activity for a user (most recent end_time).
     */
    fun getLatestActivity(userId: Int, callback: (Boolean, ActivityItem?, String) -> Unit) {
        getActivities(userId) { success, activities, message ->
            if (!success) {
                callback(false, null, message)
                return@getActivities
            }
            // Flatten all activities into a single list
            val all = activities.values.flatten()
            val latest = all.maxByOrNull {
                try {
                    java.time.LocalDateTime.parse(it.endTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } catch (e: Exception) { java.time.LocalDateTime.MIN }
            }
            callback(true, latest, message)
        }
    }
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
    /*
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
    */

    fun setOrUpdateGoal(
        userId: Int,
        walking: Int,
        running: Int,
        cycling: Int,
        swimming: Int,
        callback: (Boolean, String) -> Unit
    ) {
        val url = "$BASE_URL/goal.php"
        val params = JSONObject().apply {
            put("userId", userId)
            put("walking", walking)
            put("running", running)
            put("cycling", cycling)
            put("swimming", swimming)
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

    fun getGoal(
        userId: Int,
        callback: (Boolean, Int, Int, Int, Int) -> Unit
    ) {
        val url = "$BASE_URL/goal.php?userId=$userId"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val success = response.optBoolean("success", false)
                if (success) {
                    val goal = response.optJSONObject("goal")
                    val walking = goal?.optInt("walking", 0) ?: 0
                    val running = goal?.optInt("running", 0) ?: 0
                    val cycling = goal?.optInt("cycling", 0) ?: 0
                    val swimming = goal?.optInt("swimming", 0) ?: 0
                    callback(true, walking, running, cycling, swimming)
                } else {
                    callback(false, 0, 0, 0, 0)
                }
            },
            { error ->
                callback(false, 0, 0, 0, 0)
            }
        )
        requestQueue.add(request)
    }

    fun getActivities(userId: Int, callback: (Boolean, Map<String, List<ActivityItem>>, String) -> Unit) {
        val url = "$BASE_URL/getActivities.php?userId=$userId"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val success = response.optBoolean("success", false)
                val message = response.optString("message", "")
                if (success) {
                    val activitiesJson = response.optJSONObject("activities")
                    val result = mutableMapOf<String, List<ActivityItem>>()
                    if (activitiesJson != null) {
                        val keys = activitiesJson.keys()
                        while (keys.hasNext()) {
                            val type = keys.next()
                            val arr = activitiesJson.getJSONArray(type)
                            val list = mutableListOf<ActivityItem>()
                            for (i in 0 until arr.length()) {
                                val obj = arr.getJSONObject(i)
                                list.add(
                                    ActivityItem(
                                        stepCount = obj.optInt("step_count", 0),
                                        startTime = obj.optString("start_time", ""),
                                        endTime = obj.optString("end_time", "")
                                    )
                                )
                            }
                            result[type] = list
                        }
                    }
                    callback(true, result, message)
                } else {
                    callback(false, emptyMap(), message)
                }
            },
            { error ->
                callback(false, emptyMap(), error.localizedMessage ?: "Unknown error")
            }
        )
        requestQueue.add(request)
    }

    /**
     * Fetches 'Walking' activities for the given user, grouped by date and time for charting.
     * @param userId The user ID
     * @param callback (success, todayList, monthMap, message)
     *        todayList: List of Pair<time, step_count> for today
     *        monthMap: Map<date, total_step_count> for current month
     */
    fun getWalkingActivityReport(
        userId: Int,
        callback: (Boolean, List<Pair<String, Int>>, Map<String, Int>, String) -> Unit
    ) {
        getActivities(userId) { success, activities, message ->
            if (!success) {
                callback(false, emptyList(), emptyMap(), message)
                return@getActivities
            }
            val walkingList = activities["Walking"] ?: emptyList()
            val today = java.time.LocalDate.now()
            val month = today.monthValue
            val year = today.year
            val todayList = mutableListOf<Pair<String, Int>>()
            val monthMap = mutableMapOf<String, Int>()
            for (item in walkingList) {
                // Parse start_time as LocalDateTime
                val dt = try {
                    java.time.LocalDateTime.parse(item.startTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                } catch (e: Exception) {
                    null
                }
                if (dt != null) {
                    // For today chart (x: time, y: step_count)
                    if (dt.toLocalDate() == today) {
                        todayList.add(dt.toLocalTime().toString() to item.stepCount)
                    }
                    // For month chart (x: date, y: total step_count)
                    if (dt.year == year && dt.monthValue == month) {
                        val dateStr = dt.toLocalDate().toString()
                        monthMap[dateStr] = (monthMap[dateStr] ?: 0) + item.stepCount
                    }
                }
            }
            // Sort todayList by time, monthMap by date
            callback(true, todayList.sortedBy { it.first }, monthMap.toSortedMap(), "")
        }
    }

    data class ActivityItem(
        val stepCount: Int,
        val startTime: String,
        val endTime: String
    )
    /**
     * Syncs a list of offline activities to the server and marks them as synced if successful.
     * Call this from your OfflineSyncManager or connectivity callback.
     */
    fun syncOfflineActivities(
        context: Context,
        activities: List<com.example.fitnessapp.offline.OfflineActivity>,
        onComplete: (() -> Unit)? = null
    ) {
        if (activities.isEmpty()) {
            onComplete?.invoke()
            return
        }
        val db = com.example.fitnessapp.offline.AppDatabase.getDatabase(context)
        val dao = db.offlineActivityDao()
        var remaining = activities.size
        for (activity in activities) {
            saveActivity(
                userId = activity.userId,
                activityType = activity.activityType,
                stepCount = activity.stepCount,
                startTime = activity.startTime,
                endTime = activity.endTime
            ) { success, _ ->
                if (success) {
                    // Mark as synced
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        dao.update(activity.copy(synced = true))
                    }
                }
                remaining--
                if (remaining == 0) {
                    onComplete?.invoke()
                }
            }
        }
    }
}