package com.example.minimum.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minimum.Injection
import com.example.minimum.R
import com.example.minimum.viewmodel.ProfileViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*

class ProfileFragment: Fragment(R.layout.profile_fragment) {
    private lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {
        viewModel = ViewModelProvider(this, Injection.provideProfileViewModelFactory(context))
                .get(ProfileViewModel::class.java)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.loadSettings()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.app_toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        setSwitchListener(view)
        observeDailyNotifications(view)
        return view
    }

    private fun setSwitchListener(view: View) {
        view.findViewById<SwitchMaterial>(R.id.daily_notifications).setOnClickListener {
            viewModel.toggleDailyNotifications()
        }
    }

    private fun observeDailyNotifications(view: View) {
        viewModel.isDailyNotifications().observe(viewLifecycleOwner) {
            view.findViewById<SwitchMaterial>(R.id.daily_notifications).isChecked = it
            if (it) {
                context?.let { context -> viewModel.setAlarm(context) }
            } else {
                viewModel.removeAlarm()
            }
        }
    }
}