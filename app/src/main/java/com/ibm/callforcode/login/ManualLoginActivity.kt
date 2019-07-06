package com.ibm.callforcode.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.ibm.callforcode.R
import com.ibm.callforcode.activity.CCCBuilderActivity
import com.ibm.callforcode.activity.MainActivity
import com.ibm.callforcode.utils.SessionState
import com.sample.utils.AppConstants
import com.sample.utils.hideKeyboard
import com.sample.utils.showToast
import kotlinx.android.synthetic.main.activity_manual_login.*


class ManualLoginActivity : CCCBuilderActivity(), View.OnFocusChangeListener , TextWatcher, View.OnClickListener {
    var isPasswordShown = false

    override fun setLayoutView() = R.layout.activity_manual_login

    override fun initialize(savedInstanceState: Bundle?) {
        login_email_id_edit_text.addTextChangedListener(this)
        login_password_edit_text.addTextChangedListener(this)
        login_password_edit_text.onFocusChangeListener = this
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
    login_button.isEnabled = login_email_id_edit_text.text.isNotEmpty() && login_password_edit_text.text.isNotEmpty()
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(hasFocus) {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_focussed)
        } else {
            login_password_frame.setBackgroundResource(R.drawable.rounded_edittext_normal)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.show_hide_password_text_view -> {
                if (!isPasswordShown) {
                    show_hide_password_text_view.text = getString(R.string.hide)
                    login_password_edit_text.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    show_hide_password_text_view.text = getString(R.string.show)
                    login_password_edit_text.transformationMethod = PasswordTransformationMethod.getInstance()
                }
                login_password_edit_text.setSelection(login_password_edit_text.text.length)
                isPasswordShown = !isPasswordShown
            }

            R.id.manual_login_screen_back -> onBackPressed()

            R.id.login_forget_password_text_view -> showToast(R.string.work_in_progress)

            R.id.login_button -> loginUser()
        }
    }

    private fun loginUser() {
        hideKeyboard()
        var email = login_email_id_edit_text.text.toString()
        var username = login_email_id_edit_text.text.toString()
        var password = login_password_edit_text.text.toString()
        SessionState.instance.isAdmin = username.toLowerCase().equals(AppConstants.ADMIN)
        SessionState.instance.isLoggedIn = true
        SessionState.instance.userName = username
        SessionState.instance.saveBooleanToPreferences(this@ManualLoginActivity,
            AppConstants.Companion.PREFERENCES.LOGIN_STATUS.toString(), true)
        SessionState.instance.saveBooleanToPreferences(this@ManualLoginActivity,
            AppConstants.Companion.PREFERENCES.IS_ADMIN.toString(), SessionState.instance.isAdmin)
        SessionState.instance.saveValuesToPreferences(this@ManualLoginActivity,
            AppConstants.Companion.PREFERENCES.USER_NAME.toString(), username)
        moveToDashboard()
    }

    private fun moveToDashboard() {
        showToast(R.string.logged_in_successfully)
        startActivity(MainActivity::class.java, true)
        finish()
    }
}
