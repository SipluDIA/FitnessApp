package com.example.fitnessapp.offline

import androidx.room.*

@Dao
interface OfflineActivityDao {
    @Insert
    suspend fun insert(activity: OfflineActivity)

    @Query("SELECT * FROM offline_activities WHERE synced = 0")
    suspend fun getUnsynced(): List<OfflineActivity>

    @Update
    suspend fun update(activity: OfflineActivity)

    @Delete
    suspend fun delete(activity: OfflineActivity)
}
