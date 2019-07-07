package com.ibm.callforcode.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import com.gmail.samehadar.iosdialog.IOSDialog
import com.ibm.callforcode.R
import com.ibm.callforcode.login.ManualLoginActivity
import com.ibm.callforcode.utils.SessionState
import com.sample.utils.getProgressView

class SettingsFragment : PreferenceFragmentCompat() {
    var iosDialog : IOSDialog? = null

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.settings_pref)
        iosDialog = this.context?.getProgressView("Signing out...")
        if (SessionState.instance.isAdmin) {
            val sendNotificationPref = findPreference("send_notification")
            sendNotificationPref.title = "Send Notification"
            sendNotificationPref.summary = "Send notification to employees"
        }

        findPreference("sign_out").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            showLogoutAlertDialog()
            true
        }
    }

    fun startActivity(clazz: Class<out Activity>, isNewTask:Boolean) {
        val intent = Intent(context, clazz)
        if(isNewTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    private fun showLogoutAlertDialog() {
        val builder = AlertDialog.Builder(this.context!!)
        builder.setTitle("")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("YES") {dialog, which ->
            iosDialog?.show()
            dialog.dismiss()
            SessionState.instance.clearSession(this.context)
            startActivity(ManualLoginActivity::class.java, true)
            this.activity?.finish()
        }
        builder.setNegativeButton("No") {dialog, which ->
            iosDialog?.dismiss()
            dialog.dismiss()
        }
        builder.create().show()
    }
}