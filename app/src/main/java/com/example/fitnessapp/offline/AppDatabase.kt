package com.example.fitnessapp.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OfflineActivity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineActivityDao(): OfflineActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitness_offline_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
