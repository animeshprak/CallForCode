package com.ibm.callforcode.activity

import android.os.Bundle
import android.view.View
import com.ibm.callforcode.R
import com.ibm.callforcode.settings.SettingsFragment
import com.ibm.callforcode.utils.SessionState
import com.ibm.callforcode.webservice.data.Doc
import com.ibm.callforcode.webservice.data.Employees
import com.sample.dashboard.DashboardDetailFragment
import com.sample.dashboard.DashboardFragment
import com.sample.listeners.OnEmployeeListItemClicked
import com.sample.listeners.TitleChangeListener
import com.sample.utils.isTablet
import com.sample.utils.showToast
import com.sample.webservice.RetrofitController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : CCCBuilderActivity() , TitleChangeListener, OnEmployeeListItemClicked,
    View.OnClickListener, Callback<Employees> {

    private var mRelatedDoc = ArrayList<Doc>()
    private var mSettingsFragment = SettingsFragment()

    override fun setLayoutView() = R.layout.activity_main

    override fun initialize(savedInstanceState: Bundle?) {
        if (!isTablet()) {
            setSupportActionBar(toolbar)
        }
        onTitleChange(getString(R.string.app_name))
        setUpTabListeners()
        getCharactersDataFromServer()
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
                    commitFragment(R.id.fragment_container, mSettingsFragment, false)
                    true
                }
                else -> {
                    true
                }
            }}
    }

    private fun getCharactersDataFromServer() {
        showProgressView()
        RetrofitController.getCharactersData(this)
    }

    override fun onEmployeeItemClicked(relatedTopic: Doc, title: String, isAnimationRequired: Boolean) {
        val dashboardDetailFragment = DashboardDetailFragment.newInstance(title, relatedTopic)
        /*if (isTablet()) {
            commitFragment(R.id.fragment_detail_container, dashboardDetailFragment, false)
        } else {*/
            if (isAnimationRequired) {
                detail_back_image_view.visibility = View.VISIBLE
            }
            onTitleChange(title)
            commitFragment(R.id.fragment_container, dashboardDetailFragment, isAnimationRequired)
       // }
    }

    override fun onBackPressed() {
        if (1 < supportFragmentManager?.backStackEntryCount!!) {
            detail_back_image_view.visibility = View.INVISIBLE
            popFragmentFromBackStack()
        } else {
            finish()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.detail_back_image_view -> onBackPressed()
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
                    data?.getRows()?.forEach { it.apply { mRelatedDoc.add(doc!!) } }
                    if (!mRelatedDoc.isNullOrEmpty()) {
                        launchDashboard()
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
            commitFragment(R.id.fragment_container, DashboardFragment.newInstance(getString(R.string.app_name), mRelatedDoc), false)
        } else {
            onEmployeeItemClicked(mRelatedDoc[0], mRelatedDoc[0].empName!!, false)
        }
    }
}