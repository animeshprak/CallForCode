package com.sample.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ibm.callforcode.R
import com.ibm.callforcode.frgament.CCCBuilderFragment
import com.ibm.callforcode.utils.SessionState
import com.ibm.callforcode.webservice.data.Doc
import com.sample.utils.AppConstants
import com.sample.utils.isTablet
import com.sample.utils.showToast
import kotlinx.android.synthetic.main.fragment_dashboard_detail.*

/**
 * A simple [Fragment] subclass.
 * To show Character details
 */
class DashboardDetailFragment : CCCBuilderFragment(), OnMapReadyCallback {
    private var relatedEmployee : Doc? = null
    private var title : String = ""
    private var mMap: GoogleMap? = null
    val MY_PERMISSIONS_REQUEST_LOCATION = 87

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission()
        }

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
            detail_activity_user_map_location.text = "Last Shared Location On Map"
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.product_detail_product_map) as SupportMapFragment?

        mapFragment!!.getMapAsync(this)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isCompassEnabled = true
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.context != null && ContextCompat.checkSelfPermission(this.context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                mMap?.isMyLocationEnabled = true
            }
        } else {
            mMap?.isMyLocationEnabled = true
        }
        addMarkerToMap()
    }

    private fun checkLocationPermission(): Boolean {
        if (this.context != null && ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            } else {
                ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (this@DashboardDetailFragment.activity != null && ContextCompat.checkSelfPermission(this@DashboardDetailFragment.activity!!,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       addMarkerToMap()
                    }
                } else {
                    this.context?.showToast(R.string.permission_denied)
                }
                return
            }
        }
    }

    private fun addMarkerToMap() {
        var latLng = LatLng(relatedEmployee?.empLatestLatitude!!.toDouble(),
            relatedEmployee?.empLatestLogitude!!.toDouble())
        mMap?.addMarker(MarkerOptions().position(latLng).title(relatedEmployee?.empName))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap?.animateCamera(CameraUpdateFactory.zoomTo(17.0F))
    }

    override fun onResume() {
        super.onResume()
        if (SessionState.instance.isEmergency) {
            Glide.with(this).load(R.raw.emergency_light).into(emergency_gif_image_view)
        }
    }
}
