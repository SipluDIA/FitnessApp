package com.example.fitnessapp.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnessapp.data.model.User
import com.example.fitnessapp.data.model.ActivityLog

@Database(
    entities = [OfflineActivity::class, User::class, ActivityLog::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineActivityDao(): OfflineActivityDao
    abstract fun userDao(): UserDao
    abstract fun activityLogDao(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_offline_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
