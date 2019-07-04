package com.sample.dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.method.LinkMovementMethod
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ibm.callforcode.R
import com.ibm.callforcode.frgament.CCCBuilderFragment
import com.ibm.callforcode.webservice.data.Doc
import com.sample.utils.AppConstants
import com.sample.utils.isTablet
import kotlinx.android.synthetic.main.fragment_dashboard_detail.*

/**
 * A simple [Fragment] subclass.
 * To show Character details
 */
class DashboardDetailFragment : CCCBuilderFragment() {
    private var relatedEmployee : Doc? = null
    private var title : String = ""

    companion object {
        @JvmStatic
        fun newInstance(title: String, relatedEmployee : Doc) =
            DashboardDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(AppConstants.TITLE, title)
                    putParcelable(AppConstants.BUNDLE, relatedEmployee)
                }
            }
    }

    override fun setLayoutView() = R.layout.fragment_dashboard_detail

    override fun initialize(savedInstanceState: Bundle?) {
        arguments?.let {
            title = it.getString(AppConstants.TITLE)
            relatedEmployee = it.getParcelable(AppConstants.BUNDLE)
        }

        if (relatedEmployee != null) {
            if (this.context?.isTablet()!! && !title.isNullOrEmpty()) {
                //detail_title_text_view.text = title
            }
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.ic_placeholder)
            Glide.with(detail_fragment_image_view).setDefaultRequestOptions(requestOptions)
                .load(relatedEmployee?.attachments).into(detail_fragment_image_view)
            detail_activity_overview.text = relatedEmployee?.empName
            var moreDetailLink = relatedEmployee?.empName
            detail_activity_link.isClickable = true
            detail_activity_link.movementMethod = LinkMovementMethod.getInstance()
            val linkText = "<a href='$moreDetailLink'>Click to get more details </a>"
            detail_activity_link.text = Html.fromHtml(linkText)
        }
    }

}
