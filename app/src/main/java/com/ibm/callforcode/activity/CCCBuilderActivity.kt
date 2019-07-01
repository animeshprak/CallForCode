package com.ibm.callforcode.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ibm.callforcode.R
import com.ibm.callforcode.frgament.CCCBuilderFragment
import com.sample.utils.AppConstants
import com.sample.utils.slideActivityRightToLeft

/**
 * Created by Animesh on 06/29/19.
 */
abstract class CCCBuilderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        slideActivityRightToLeft()
        setContentView(setLayoutView())
        initialize(savedInstanceState)
    }

    protected abstract fun setLayoutView(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)

    fun startActivity(clazz: Class<out Activity>, isNewTask: Boolean) {
        val intent = Intent(this, clazz)
        if (isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivity(clazz: Class<out Activity>, isNewTask: Boolean, bundle: Bundle) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if (isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    fun startActivityForResult(clazz: Class<out Activity>, isNewTask: Boolean, bundle: Bundle, activityStartCode: Int) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, bundle)
        if (isNewTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivityForResult(intent, activityStartCode)
    }

    fun startActivity(clazz: Class<out Activity>, extra: Bundle, parcelName: String) {
        val intent = Intent(this, clazz)
        intent.putExtra(AppConstants.BUNDLE, extra)
        startActivity(intent)
    }

    fun commitFragment(container: Int, fragment : CCCBuilderFragment, isAnimationRequired : Boolean) {
        if (!isFinishing) {
            val fragmentTransaction = supportFragmentManager?.beginTransaction()
            if (isAnimationRequired) {
                fragmentTransaction?.setCustomAnimations(R.anim.right_to_left_start, R.anim.left_to_right_end,
                    R.anim.left_to_right_start, R.anim.right_to_left_end)
            }
            fragmentTransaction?.replace(container, fragment, fragment :: class.java.simpleName)
            //if (!resources.getBoolean(R.bool.isTablet)) {
                fragmentTransaction?.addToBackStack(null)
           // }
            fragmentTransaction?.commit()
        }
    }

    fun popFragmentFromBackStack() {
        supportFragmentManager?.popBackStackImmediate()
    }

}