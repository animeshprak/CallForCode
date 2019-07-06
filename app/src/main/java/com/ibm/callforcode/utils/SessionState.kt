package com.ibm.callforcode.utils

import android.content.Context
import com.sample.utils.AppConstants

class SessionState private constructor() {
    var isLoggedIn = false
    var userName = ""
    var isAdmin = false

    companion object {
        var mSessionState : SessionState? = null
        val instance: SessionState
            get() {
                if (mSessionState == null) {
                    mSessionState = SessionState()
                }
                return mSessionState!!
            }

    }

    fun saveValuesToPreferences(context: Context, prefName: String, prefValue: String) {
        if (context != null) {
            val editor = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE).edit()
            editor.putString(prefName, prefValue)
            editor.commit()
        }
    }

    fun saveBooleanToPreferences(context: Context, prefName: String, prefValue: Boolean) {
        if (context != null) {
            val editor = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE).edit()
            editor.putBoolean(prefName, prefValue)
            editor.commit()
        }
    }

    fun readValuesFromPreferences(context: Context?) {
        if (context != null) {
            val prefs = context.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)
            this.isLoggedIn = prefs.getBoolean(AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), false)
            this.isAdmin = prefs.getBoolean(AppConstants.Companion.PREFERENCES.IS_ADMIN.toString(), false)
            this.userName = prefs.getString(AppConstants.Companion.PREFERENCES.USER_NAME.toString(), "")
        }
    }

    fun clearSession(context: Context?) {
        this.userName = ""
        this.isLoggedIn = false
        this.isAdmin = false
        context?.getSharedPreferences(AppConstants.PREF_FILE, Context.MODE_PRIVATE)?.edit()?.clear()?.commit()
    }
}