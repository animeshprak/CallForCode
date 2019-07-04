package com.ibm.callforcode.activity

import android.os.Bundle
import android.view.View
import com.ibm.callforcode.R
import com.ibm.callforcode.activity.CCCBuilderActivity
import com.ibm.callforcode.webservice.data.Doc
import com.sample.dashboard.DashboardDetailFragment
import com.sample.dashboard.DashboardFragment
import com.sample.listeners.OnEmployeeListItemClicked
import com.sample.listeners.TitleChangeListener
import com.sample.utils.isTablet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CCCBuilderActivity() , TitleChangeListener, OnEmployeeListItemClicked,
    View.OnClickListener {

    private val mDashboardFragment = DashboardFragment()
    override fun setLayoutView() = R.layout.activity_main

    override fun initialize(savedInstanceState: Bundle?) {
        if (!isTablet()) {
            setSupportActionBar(toolbar)
        }
        onTitleChange(getString(R.string.app_name))
        commitFragment(R.id.fragment_container, mDashboardFragment, false)
    }

    override fun onTitleChange(title: String) {
        title_text_view?.text = title
    }

    override fun onEmployeeItemClicked(relatedTopic: Doc, title: String) {
        val dashboardDetailFragment = DashboardDetailFragment.newInstance(title, relatedTopic)
        /*if (isTablet()) {
            commitFragment(R.id.fragment_detail_container, dashboardDetailFragment, false)
        } else {*/
            detail_back_image_view.visibility = View.VISIBLE
            onTitleChange(title)
            commitFragment(R.id.fragment_container, dashboardDetailFragment, true)
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
}