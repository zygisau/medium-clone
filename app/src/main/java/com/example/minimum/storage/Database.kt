package com.example.minimum.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.minimum.storage.dao.BookmarkDao
import com.example.minimum.storage.dao.SettingsDao
import com.example.minimum.storage.model.Bookmark
import com.example.minimum.storage.model.Settings

@Database(entities = [Bookmark::class, Settings::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun settingsDao(): SettingsDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "minimum.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
//                        .addMigrations(Migration_V1_to_V2())
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
