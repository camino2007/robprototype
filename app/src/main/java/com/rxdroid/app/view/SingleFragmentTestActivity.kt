package com.rxdroid.app.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Is only used to load single fragments for Espresso tests
 */
class SingleFragmentTestActivity : AppCompatActivity() {

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
    }
}