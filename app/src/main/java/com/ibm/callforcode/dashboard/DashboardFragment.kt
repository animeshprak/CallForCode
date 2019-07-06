package com.sample.dashboard


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import com.ibm.callforcode.R
import com.ibm.callforcode.frgament.CCCBuilderFragment
import com.ibm.callforcode.webservice.data.Doc
import com.sample.listeners.OnEmployeeListItemClicked
import com.sample.listeners.TitleChangeListener
import com.sample.utils.AppConstants
import com.sample.utils.hideKeyboard
import com.sample.utils.isTablet
import com.sample.utils.showToast
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * A simple [Fragment] subclass.
 * Represent Dashboard
 * Contains Character names
 */
class DashboardFragment : CCCBuilderFragment(), TextWatcher,
    OnEmployeeListItemClicked {
    private var mTitleChangeListener : TitleChangeListener? = null
    private var mEmployeeListItemClicked : OnEmployeeListItemClicked? = null
    private var mRelatedDoc = ArrayList<Doc>()
    private var mRelatedDocTempData = ArrayList<Doc>()
    private var mDashboardAdapter : DashboardAdapter? = null
    private var title : String = "Call For Code"

    companion object {
        @JvmStatic
        fun newInstance(title: String, relatedEmployeeList : ArrayList<Doc>) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(AppConstants.TITLE, title)
                    putParcelableArrayList(AppConstants.BUNDLE, relatedEmployeeList)
                }
            }
    }

    override fun setLayoutView() = R.layout.fragment_dashboard

    override fun initialize(savedInstanceState: Bundle?) {
        mTitleChangeListener = if (mBaseActivity != null) mBaseActivity as TitleChangeListener else null
        mEmployeeListItemClicked = if (mBaseActivity != null) mBaseActivity as OnEmployeeListItemClicked else null

        character_name_dashboard_recycler_view.layoutManager = LinearLayoutManager(this.context)
        character_name_dashboard_recycler_view.setHasFixedSize(false)
        character_name_dashboard_recycler_view.itemAnimator = DefaultItemAnimator()
        search_character_edit_text.addTextChangedListener(this)
        initializingListener()

        arguments?.let {
            title = it.getString(AppConstants.TITLE)
            mRelatedDoc = it.getParcelableArrayList(AppConstants.BUNDLE)
        }

        mDashboardAdapter = if (!mRelatedDocTempData.isNullOrEmpty()) {
            DashboardAdapter(mRelatedDocTempData, this)
        } else {
            DashboardAdapter(mRelatedDoc, this)
        }
        character_name_dashboard_recycler_view.adapter = mDashboardAdapter
        if (!title.isNullOrEmpty()) {
            mTitleChangeListener?.onTitleChange(title)
        }

    }

    private fun initializingListener() {
        search_imageView.setOnClickListener { this.activity?.hideKeyboard() }
        filter_fab_button.setOnClickListener {  }
    }

    override fun afterTextChanged(charSequence: Editable?) {
        setFilteredDataToList(charSequence.toString())
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    private fun setFilteredDataToList(searchedText: String) {
        if (!mRelatedDoc.isNullOrEmpty() && mDashboardAdapter != null) {
            mRelatedDocTempData.clear()
            mRelatedDoc.forEach {
                if (it.empName!!.toLowerCase().contains(searchedText.toLowerCase())!! ||
                        it.id!!.toLowerCase().contains(searchedText.toLowerCase())!!) {
                    mRelatedDocTempData.add(it)
                }
            }

            mDashboardAdapter?.updateDataSet(mRelatedDocTempData)
            if (mRelatedDocTempData.isNullOrEmpty()) {
                this.context?.showToast(R.string.empty_data_set)
            }
        }
    }

    override fun onEmployeeItemClicked(relatedEmployee: Doc, title : String, isAnimationRequired: Boolean) {
        if (!this.context?.isTablet()!!) {
            this.activity?.hideKeyboard()
        }
        mEmployeeListItemClicked?.onEmployeeItemClicked(relatedEmployee, title, isAnimationRequired)
    }
}
