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
            detail_activity_user_name.text = relatedEmployee?.empName
            detail_activity_user_id.text = relatedEmployee?.id
            detail_activity_user_phone.text = "Phone - ${relatedEmployee?.empPhone}"
            detail_activity_user_emergency_contact.text = "Emergency contact - ${relatedEmployee?.emergencyContact}"
            detail_activity_user_status.text = Html.fromHtml(getStatus(relatedEmployee?.empInBuilding!!))
            detail_activity_user_location.text = "Location - ${relatedEmployee?.seatNumber}, ODC - ${relatedEmployee?.odcNumber}, Floor - ${relatedEmployee?.odcFloor}"
        }
    }

    private fun getStatus(isEmployeeIn : Boolean) : String {
        var statusColor = "#CA0009"
        var statusText = "OUT"
        if (isEmployeeIn) {
            statusColor = "#008000"
            statusText = "IN"
        }
        return "<font color=#313C3F>Status</font> - <font color=$statusColor>$statusText</font>"
    }

}
