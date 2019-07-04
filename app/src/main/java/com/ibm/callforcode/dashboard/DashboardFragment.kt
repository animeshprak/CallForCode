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
import com.ibm.callforcode.webservice.data.Employees
import com.sample.listeners.OnEmployeeListItemClicked
import com.sample.listeners.TitleChangeListener
import com.sample.utils.hideKeyboard
import com.sample.utils.isTablet
import com.sample.utils.showToast
import com.sample.webservice.RetrofitController
import kotlinx.android.synthetic.main.fragment_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Represent Dashboard
 * Contains Character names
 */
class DashboardFragment : CCCBuilderFragment(), TextWatcher, Callback<Employees>
    , OnEmployeeListItemClicked {
    private var mTitleChangeListener : TitleChangeListener? = null
    private var mEmployeeListItemClicked : OnEmployeeListItemClicked? = null
    private var mRelatedDoc = ArrayList<Doc>()
    private var mRelatedDocTempData = ArrayList<Doc>()
    private var mDashboardAdapter : DashboardAdapter? = null
    private var title : String = "Call For Code"

    override fun setLayoutView() = R.layout.fragment_dashboard

    override fun initialize(savedInstanceState: Bundle?) {
        mTitleChangeListener = if (mBaseActivity != null) mBaseActivity as TitleChangeListener else null
        mEmployeeListItemClicked = if (mBaseActivity != null) mBaseActivity as OnEmployeeListItemClicked else null

        character_name_dashboard_recycler_view.layoutManager = LinearLayoutManager(this.context)
        character_name_dashboard_recycler_view.setHasFixedSize(false)
        character_name_dashboard_recycler_view.itemAnimator = DefaultItemAnimator()
        search_character_edit_text.addTextChangedListener(this)
        initializingKeywordHideListener()
        if (mRelatedDoc.isNullOrEmpty()) {
            showProgressView()
            getCharactersDataFromServer()
        } else {
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
    }

    private fun initializingKeywordHideListener() {
        search_imageView.setOnClickListener { this.activity?.hideKeyboard() }
    }

    private fun getCharactersDataFromServer() {
        RetrofitController.getCharactersData(this)
    }

    override fun onFailure(call: Call<Employees>?, t: Throwable?) {
        hideProgressView()
        this.context?.showToast(R.string.internet_issue)
    }

    override fun onResponse(call: Call<Employees>?, response: Response<Employees>?) {
        if (isAdded && !isHidden) {
            if (response != null && response.isSuccessful && character_name_dashboard_recycler_view != null) {
                val data : Employees? = response.body()
                if (!data?.getRows().isNullOrEmpty()) {
                    data?.getRows()?.forEach { it.apply { mRelatedDoc.add(doc!!) } }
                    mDashboardAdapter = DashboardAdapter(mRelatedDoc, this)
                    character_name_dashboard_recycler_view.adapter = mDashboardAdapter
                    /*if (!data?.heading.isNullOrEmpty()) {
                        title = data?.heading!!
                        mTitleChangeListener?.onTitleChange(title)
                    }*/
                } else {
                    this.context?.showToast(R.string.empty_data_set)
                }
            } else {
                this.context?.showToast(R.string.some_wrong)
            }
            hideProgressView()
        }
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

    override fun onEmployeeItemClicked(relatedEmployee: Doc, title : String) {
        if (!this.context?.isTablet()!!) {
            this.activity?.hideKeyboard()
        }
        mEmployeeListItemClicked?.onEmployeeItemClicked(relatedEmployee, title)
    }
}
