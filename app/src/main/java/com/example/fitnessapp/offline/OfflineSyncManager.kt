package com.example.fitnessapp.offline

import android.content.Context
import com.example.fitnessapp.network.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OfflineSyncManager {
    fun syncOfflineActivities(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val dao = db.offlineActivityDao()
        CoroutineScope(Dispatchers.IO).launch {
            val unsynced = dao.getUnsynced()
            for (activity in unsynced) {
                NetworkManager.saveActivity(
                    userId = activity.userId,
                    activityType = activity.activityType,
                    stepCount = activity.stepCount,
                    startTime = activity.startTime,
                    endTime = activity.endTime
                ) { success, _ ->
                    if (success) {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.update(activity.copy(synced = true))
                        }
                    }
                }
            }
        }
    }
}
