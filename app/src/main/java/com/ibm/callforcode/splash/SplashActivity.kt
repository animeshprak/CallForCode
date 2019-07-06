package com.ibm.callforcode.splash

import android.os.Bundle
import android.os.Handler
import com.ibm.callforcode.R
import com.ibm.callforcode.activity.CCCBuilderActivity
import com.ibm.callforcode.activity.MainActivity
import com.ibm.callforcode.login.ManualLoginActivity
import com.ibm.callforcode.utils.SessionState
import com.sample.utils.AppConstants

class SplashActivity : CCCBuilderActivity() {
    override fun setLayoutView() = R.layout.activity_splash

    override fun initialize(savedInstanceState: Bundle?) {
        setUpSplash()
    }

    private fun setUpSplash() {
        Handler().postDelayed( {
            this@SplashActivity.runOnUiThread() {
                SessionState.instance.readValuesFromPreferences(this)
                if (SessionState.instance.isLoggedIn) {
                    startActivity(MainActivity::class.java, true)
                } else {
                    startActivity(ManualLoginActivity::class.java, true)
                }
                finish()
            }
        }, AppConstants.SPLASH_TIME)
    }

    override fun onBackPressed() {}
}
