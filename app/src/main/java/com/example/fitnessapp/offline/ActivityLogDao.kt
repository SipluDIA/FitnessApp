package com.example.fitnessapp.offline

import androidx.room.*
import com.example.fitnessapp.data.model.ActivityLog

@Dao
interface ActivityLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ActivityLog)

    @Query("SELECT * FROM ActivityLog WHERE userId = :userId ORDER BY activityDate DESC")
    suspend fun getLogsForUser(userId: Int): List<ActivityLog>

    @Update
    suspend fun update(log: ActivityLog)

    @Delete
    suspend fun delete(log: ActivityLog)
}
