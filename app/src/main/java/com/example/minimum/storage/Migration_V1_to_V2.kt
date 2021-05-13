package com.example.minimum.storage

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration_V1_to_V2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        database.execSQL("CREATE TABLE `Settings` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`key`))")
        database.execSQL("INSERT INTO `Settings` (`key`, `value`) VALUES ('daily_notifications', 'true')")
        database.endTransaction()
    }
}