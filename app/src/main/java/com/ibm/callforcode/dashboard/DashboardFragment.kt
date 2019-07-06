package com.sample.dashboard


import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import com.ibm.callforcode.R
import com.ibm.callforcode.frgament.CCCBuilderFragment
import com.ibm.callforcode.utils.CustomAlertDialog
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
    private var sortBySelectedType = 1

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
        filter_fab_button.setOnClickListener {  showSortingAlertDialog() }
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

    private fun showSortingAlertDialog() {
        val sortingDialog = CustomAlertDialog(this.context!!, R.style.custom_filter_dialog)
        sortingDialog.setContentView(R.layout.sorting_options_dialog)
        sortingDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        sortingDialog.getWindow().setBackgroundDrawable(ColorDrawable(resources.getColor(android.R.color.transparent)))
        sortingDialog.setCanceledOnTouchOutside(true)
        sortingDialog.show()

        val sortingAllTextView = sortingDialog.findViewById(R.id.sorting_all_text_view) as AppCompatTextView
        val sortingInsideTextView = sortingDialog.findViewById(R.id.sorting_inside_text_view) as AppCompatTextView
        val sortingOutsideTextView = sortingDialog.findViewById(R.id.sorting_outside_text_view) as AppCompatTextView
        
        val sortingAllTextViewLayout = sortingDialog.findViewById(R.id.sorting_all_text_view_layout) as ConstraintLayout
        val sortingInsideTextViewLayout = sortingDialog.findViewById(R.id.sorting_inside_text_view_layout) as ConstraintLayout
        val sortingOutsideTextViewLayout = sortingDialog.findViewById(R.id.sorting_sorting_outside_text_view_layout) as ConstraintLayout

        if (sortBySelectedType == 1) {
            sortingAllTextView.setTypeface(sortingAllTextView.typeface, Typeface.BOLD)
            sortingInsideTextView.setTypeface(sortingInsideTextView.typeface, Typeface.NORMAL)
            sortingOutsideTextView.setTypeface(sortingOutsideTextView.typeface, Typeface.NORMAL)

            sortingAllTextViewLayout.setBackgroundColor(resources.getColor(R.color.light_gray))
            sortingInsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
            sortingOutsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
        } else if (sortBySelectedType == 2){
            sortingAllTextView.setTypeface(sortingAllTextView.typeface, Typeface.NORMAL)
            sortingInsideTextView.setTypeface(sortingInsideTextView.typeface, Typeface.BOLD)
            sortingOutsideTextView.setTypeface(sortingOutsideTextView.typeface, Typeface.NORMAL)

            sortingAllTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
            sortingInsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.light_gray))
            sortingOutsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
        } else if (sortBySelectedType == 3) {
            sortingAllTextView.setTypeface(sortingAllTextView.typeface, Typeface.NORMAL)
            sortingInsideTextView.setTypeface(sortingInsideTextView.typeface, Typeface.NORMAL)
            sortingOutsideTextView.setTypeface(sortingOutsideTextView.typeface, Typeface.BOLD)

            sortingAllTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
            sortingInsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.white))
            sortingOutsideTextViewLayout.setBackgroundColor(resources.getColor(R.color.light_gray))
        }
        
        sortingAllTextViewLayout.setOnClickListener() {
            if (sortBySelectedType != 1) {
                sortBySelectedType = 1
                sortByType(sortBySelectedType)
            }
            sortingDialog.dismiss()
        }

        sortingInsideTextViewLayout.setOnClickListener() {
            if (sortBySelectedType != 2) {
                sortBySelectedType = 2
                sortByType(sortBySelectedType)
            }
            sortingDialog.dismiss()
        }

        sortingOutsideTextViewLayout.setOnClickListener() {
            if (sortBySelectedType != 3) {
                sortBySelectedType = 3
                sortByType(sortBySelectedType)
            }
            sortingDialog.dismiss()
        }
    }

    private fun sortByType(sortBySelectedType: Int) {
        if (!mRelatedDoc.isNullOrEmpty() && mDashboardAdapter != null) {
            mRelatedDocTempData.clear()
            mRelatedDoc.forEach {
                if (sortBySelectedType == 2 && it.empInBuilding!!) {
                    mRelatedDocTempData.add(it)
                } else if (sortBySelectedType == 3 && !it.empInBuilding!!) {
                    mRelatedDocTempData.add(it)
                } else if (sortBySelectedType == 1) {
                    mRelatedDocTempData.add(it)
                }
            }

            mDashboardAdapter?.updateDataSet(mRelatedDocTempData)
            if (mRelatedDocTempData.isNullOrEmpty()) {
                this.context?.showToast(R.string.empty_data_set)
            }
        }
    }
}
