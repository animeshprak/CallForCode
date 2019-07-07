package com.ibm.callforcode.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.bumptech.glide.Glide
import com.ibm.callforcode.R
import com.ibm.callforcode.listeners.RefreshAllEmployeeData
import com.ibm.callforcode.listeners.RefreshEmployeeData
import com.ibm.callforcode.settings.SettingsFragment
import com.ibm.callforcode.utils.SessionState
import com.ibm.callforcode.webservice.data.Doc
import com.ibm.callforcode.webservice.data.Employees
import com.sample.dashboard.DashboardDetailFragment
import com.sample.dashboard.DashboardFragment
import com.sample.listeners.OnEmployeeListItemClicked
import com.sample.listeners.TitleChangeListener
import com.sample.utils.AppConstants
import com.sample.utils.showToast
import com.sample.webservice.RetrofitController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : CCCBuilderActivity() , TitleChangeListener, OnEmployeeListItemClicked,
    View.OnClickListener, Callback<Employees> {

    private var mRelatedDoc = ArrayList<Doc>()
    private var mSettingsFragment = SettingsFragment()
    private var mDashboardDetailFragment : DashboardDetailFragment? = null
    private var mDashboardFragment : DashboardFragment? = null

    override fun setLayoutView() = R.layout.activity_main

    override fun initialize(savedInstanceState: Bundle?) {
        //if (!isTablet()) {
            setSupportActionBar(toolbar)
        //}
        emergency_button_image_view.visibility = if (SessionState.instance.isAdmin) View.VISIBLE else View.INVISIBLE
        onTitleChange(getString(com.ibm.callforcode.R.string.app_name))
        setUpTabListeners()
        getEmployeesDataFromServer(true)
        scheduleRefreshData()
    }

    private fun scheduleRefreshData() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                this@MainActivity.runOnUiThread() {
                     getEmployeesDataFromServer(false)
                }
            }
        }, 1000L, 10000L)
    }

    override fun onTitleChange(title: String) {
        title_text_view?.text = title
    }

    private fun setUpTabListeners() {
        bottom_navigation_menu.setOnNavigationItemSelectedListener {
                menuItem ->
            when(menuItem.itemId) {
                R.id.action_home -> {
                    if (!mRelatedDoc.isNullOrEmpty()) {
                        launchDashboard()
                    }
                    true
                }
                R.id.action_edit -> {

                    true
                }
                R.id.action_settings -> {
                    onTitleChange(getString(R.string.settings))
                    commitSettingsFragment(R.id.fragment_container, mSettingsFragment)
                    true
                }
                else -> {
                    true
                }
            }}
    }

    private fun getEmployeesDataFromServer(isProgressRequired : Boolean) {
        if (isProgressRequired) showProgressView()
        RetrofitController.getEmployeesData(this)
    }

    override fun onEmployeeItemClicked(relatedTopic: Doc, title: String, isAnimationRequired: Boolean) {
        mDashboardDetailFragment = DashboardDetailFragment.newInstance(title, relatedTopic)
        /*if (isTablet()) {
            commitFragment(R.id.fragment_detail_container, dashboardDetailFragment, false)
        } else {*/
            if (isAnimationRequired) {
                bottom_navigation_menu.visibility = View.GONE
                detail_back_image_view.visibility = View.VISIBLE
            }
            onTitleChange(title)
            commitFragment(R.id.fragment_container, mDashboardDetailFragment!!, isAnimationRequired)
       // }
    }

    override fun onBackPressed() {
        if (0 < supportFragmentManager?.backStackEntryCount!!) {
            detail_back_image_view.visibility = View.INVISIBLE
            bottom_navigation_menu.visibility = View.VISIBLE
            popFragmentFromBackStack()
        } else {
            finish()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.detail_back_image_view -> onBackPressed()
            R.id.emergency_button_image_view -> {
                showEmergencyAlertDialog()
            }
        }
    }

    override fun onFailure(call: Call<Employees>?, t: Throwable?) {
        hideProgressView()
        this.showToast(R.string.internet_issue)
    }

    override fun onResponse(call: Call<Employees>?, response: Response<Employees>?) {
            if (response != null && response.isSuccessful) {
                val data : Employees? = response.body()
                if (!data?.getRows().isNullOrEmpty()) {
                    mRelatedDoc.clear()
                    data?.getRows()?.forEach { it.apply { mRelatedDoc.add(doc!!) } }
                    if (!mRelatedDoc.isNullOrEmpty() && mDashboardFragment == null && mDashboardDetailFragment == null) {
                        launchDashboard()
                    } else {
                        if (mDashboardFragment != null && mDashboardFragment!!.isAdded && !mDashboardFragment!!.isHidden) {
                            var mOnRefreshDataRequired : RefreshAllEmployeeData = mDashboardFragment as RefreshAllEmployeeData
                            mOnRefreshDataRequired?.refreshData(mRelatedDoc)
                        } else if (mDashboardDetailFragment != null && mDashboardDetailFragment!!.isAdded && !mDashboardDetailFragment!!.isHidden) {
                            var mOnRefreshDataRequired : RefreshEmployeeData = mDashboardDetailFragment as RefreshEmployeeData
                            var employeeDoc : Doc? = null
                            mRelatedDoc.forEach { if (it.id.equals(SessionState.instance.userName)) {employeeDoc = it} }
                            if (employeeDoc != null) {
                                mOnRefreshDataRequired?.refreshData(employeeDoc!!)
                            }
                        }
                    }
                    /*if (!data?.heading.isNullOrEmpty()) {
                        title = data?.heading!!
                        mTitleChangeListener?.onTitleChange(title)
                    }*/
                } else {
                    this.showToast(R.string.empty_data_set)
                }
            } else {
                this.showToast(R.string.some_wrong)
            }
            hideProgressView()
    }

    private fun launchDashboard() {
        if (SessionState.instance.isAdmin) {
            mDashboardFragment = DashboardFragment.newInstance(getString(R.string.app_name), mRelatedDoc)
            commitFragment(R.id.fragment_container, mDashboardFragment!!, false)
        } else {
            var employeeDoc : Doc? = null
            mRelatedDoc.forEach { if (it.id.equals(SessionState.instance.userName)) {employeeDoc = it} }
            if (employeeDoc == null) {
                employeeDoc = mRelatedDoc[0]
            }
            onEmployeeItemClicked(employeeDoc!!, employeeDoc!!.empName!!, false)
        }
    }

    private fun showEmergencyAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("")
        if (SessionState.instance.isEmergency) {
            builder.setMessage("STOP EMERGENCY?")
        } else {
            builder.setMessage("Are you sure you want to trigger EMERGENCY?")
        }
        builder.setPositiveButton("YES") {dialog, which ->
            dialog.dismiss()
            SessionState.instance.isEmergency = !SessionState.instance.isEmergency
            checkForEmergency()
        }
        builder.setNegativeButton("No") {dialog, which ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun checkForEmergency(isToastRequired: Boolean = true) {
        if (SessionState.instance.isEmergency) {
            Glide.with(this).load(R.raw.emergency_light).into(emergency_button_image_view)
            if (isToastRequired) this.showToast(R.string.emergency_triggered)
            SessionState.instance.isEmergency = true
            SessionState.instance.saveBooleanToPreferences(this, AppConstants.Companion.PREFERENCES.EMERGENCY.toString(), true)
        } else {
            emergency_button_image_view.setImageResource(R.drawable.ic_emergency)
            if (isToastRequired) this.showToast(R.string.emergency_stopped)
            SessionState.instance.isEmergency = false
            SessionState.instance.saveBooleanToPreferences(this, AppConstants.Companion.PREFERENCES.EMERGENCY.toString(), false)
        }
    }

    override fun onResume() {
        super.onResume()
        checkForEmergency(false)
    }
}