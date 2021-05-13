package com.example.minimum.storage.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.minimum.storage.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT `value` FROM settings WHERE `key` = \"daily_notifications\"")
    suspend fun isDailyNotifications(): String

    @Query("UPDATE settings SET `value` = :state WHERE `key` = \"daily_notifications\"")
    suspend fun setDailyNotifications(state: String)
}