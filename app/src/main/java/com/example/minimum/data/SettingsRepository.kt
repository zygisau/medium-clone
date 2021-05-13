package com.example.minimum.data

import androidx.annotation.WorkerThread
import com.example.minimum.storage.dao.SettingsDao
import com.example.minimum.storage.model.Settings

class SettingsRepository(private val settingsDao: SettingsDao) {
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun setDailyNotifications(state: Boolean) {
        settingsDao.setDailyNotifications(state.toString())
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun isDailyNotifications(): String {
        return settingsDao.isDailyNotifications()
    }
}