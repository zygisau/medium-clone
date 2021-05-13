package com.example.minimum.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimum.data.SettingsRepository
import com.example.minimum.ui.NotificationReceiver
import kotlinx.coroutines.*
import java.util.*

class ProfileViewModel(private val repository: SettingsRepository) : ViewModel() {
    private val dailyNotificationsSetting = MutableLiveData<Boolean>()

    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    fun toggleDailyNotifications() {
        GlobalScope.launch(Dispatchers.IO) {
            val newSetting = !(repository.isDailyNotifications().toBoolean())
            dailyNotificationsSetting.postValue(newSetting)
            repository.setDailyNotifications(newSetting)
        }
    }

    fun loadSettings() {
        viewModelScope.launch(){
            load()
        }
    }

    private suspend fun load() {
        dailyNotificationsSetting.value = repository.isDailyNotifications().toBoolean()
    }

    fun isDailyNotifications(): LiveData<Boolean> = dailyNotificationsSetting

    fun setAlarm(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 21)
        calendar.set(Calendar.MINUTE, 42)
        calendar.set(Calendar.SECOND, 30)

        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }

    fun removeAlarm() {
        if (pendingIntent != null) {
            alarmManager?.cancel(pendingIntent)
        }
    }
}
