package com.saratms.travelmantics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saratms.travelmantics.utilities.FirebaseUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseUtil.setFirebaseUtilActivity(this)
        setContentView(R.layout.activity_main)
    }

    fun redrawMenu(){
        invalidateOptionsMenu()
    }

    override fun onStart() {
        super.onStart()
        FirebaseUtil.attachAuthListener()
    }

    override fun onStop() {
        super.onStop()
        FirebaseUtil.detachAuthListener()
    }

    override fun onDestroy() {
        FirebaseUtil.releaseActivityReference()
        super.onDestroy()
    }
}
