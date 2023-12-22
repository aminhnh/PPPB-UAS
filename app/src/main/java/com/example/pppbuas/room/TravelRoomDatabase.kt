package com.example.pppbuas.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DATABASE_VERSION = 1
@Database(entities = [TravelEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class TravelRoomDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
    companion object {
        @Volatile
        private var INSTANCE: TravelRoomDatabase? = null

        fun getDatabase(context: Context): TravelRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelRoomDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
