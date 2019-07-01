package com.ibm.callforcode.frgament

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.samehadar.iosdialog.IOSDialog
import com.ibm.callforcode.activity.MainActivity
import com.sample.utils.getProgressView

/**
 * Created by Animesh on 06/29/19.
 */
abstract class CCCBuilderFragment : Fragment() {
    private var iOSDialog : IOSDialog? = null
    var mBaseActivity : MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayoutView(), container, false)
    }

    protected abstract fun setLayoutView(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iOSDialog = this.context?.getProgressView()
        mBaseActivity = if (activity is MainActivity) this.activity as MainActivity else null
        initialize(savedInstanceState)
    }

    fun showProgressView() {
        iOSDialog?.show()
    }

    fun hideProgressView() {
        iOSDialog?.dismiss()
    }

    protected abstract fun initialize(savedInstanceState: Bundle?)
}