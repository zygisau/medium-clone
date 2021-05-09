package com.example.minimum.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import java.io.InvalidObjectException

class SimpleFragmentManager<T: FragmentActivity>(private val activity: T) {

    fun <T : Fragment> replaceFragment(fragmentClass: Class<T>, id: Int) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(id, fragmentClass, null)
        transaction.commit()
    }

    fun <T : Fragment> addFragment(fragmentClass: Class<T>, id: Int) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.add(id, fragmentClass, null)
        transaction.commit()
    }

    fun toggleFragment(id: Int, show: Boolean) {
        val fragment: Fragment? = activity.supportFragmentManager.findFragmentById(id)
        if (fragment != null) {
            val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            if (show) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
            transaction.commit()
        } else {
            throw InvalidObjectException("Fragment is not loaded yet")
        }
    }
}