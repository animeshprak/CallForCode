package com.ibm.callforcode.webservice.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class Doc() : Parcelable {

    @SerializedName("_id")
    @Expose
    var id: String? = null
    @SerializedName("_rev")
    @Expose
    var rev: String? = null
    @SerializedName("emp_name")
    @Expose
    var empName: String? = null
    @SerializedName("work_location")
    @Expose
    var workLocation: String? = null
    @SerializedName("odc_floor")
    @Expose
    var odcFloor: String? = null
    @SerializedName("odc_number")
    @Expose
    var odcNumber: String? = null
    @SerializedName("seat_number")
    @Expose
    var seatNumber: String? = null
    @SerializedName("emp_in_building")
    @Expose
    var empInBuilding: Boolean? = null
    @SerializedName("facility_address")
    @Expose
    var facilityAddress: String? = null
    @SerializedName("facility_city")
    @Expose
    var facilityCity: String? = null
    @SerializedName("facitlity_state")
    @Expose
    var facitlityState: String? = null
    @SerializedName("facility_pincode")
    @Expose
    var facilityPincode: String? = null
    @SerializedName("facility_latitude")
    @Expose
    var facilityLatitude: String? = null
    @SerializedName("facility_logitute")
    @Expose
    var facilityLogitute: String? = null
    @SerializedName("emp_latest_latitude")
    @Expose
    var empLatestLatitude: String? = null
    @SerializedName("emp_latest_logitude")
    @Expose
    var empLatestLogitude: String? = null
    @SerializedName("emp_phone")
    @Expose
    var empPhone: String? = null
    @SerializedName("emergency_contact")
    @Expose
    var emergencyContact: String? = null
    @SerializedName("_attachments")
    @Expose
    var attachments: JSONObject? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        rev = parcel.readString()
        empName = parcel.readString()
        workLocation = parcel.readString()
        odcFloor = parcel.readString()
        odcNumber = parcel.readString()
        seatNumber = parcel.readString()
        empInBuilding = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        facilityAddress = parcel.readString()
        facilityCity = parcel.readString()
        facitlityState = parcel.readString()
        facilityPincode = parcel.readString()
        facilityLatitude = parcel.readString()
        facilityLogitute = parcel.readString()
        empLatestLatitude = parcel.readString()
        empLatestLogitude = parcel.readString()
        empPhone = parcel.readString()
        emergencyContact = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(rev)
        parcel.writeString(empName)
        parcel.writeString(workLocation)
        parcel.writeString(odcFloor)
        parcel.writeString(odcNumber)
        parcel.writeString(seatNumber)
        parcel.writeValue(empInBuilding)
        parcel.writeString(facilityAddress)
        parcel.writeString(facilityCity)
        parcel.writeString(facitlityState)
        parcel.writeString(facilityPincode)
        parcel.writeString(facilityLatitude)
        parcel.writeString(facilityLogitute)
        parcel.writeString(empLatestLatitude)
        parcel.writeString(empLatestLogitude)
        parcel.writeString(empPhone)
        parcel.writeString(emergencyContact)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doc> {
        override fun createFromParcel(parcel: Parcel): Doc {
            return Doc(parcel)
        }

        override fun newArray(size: Int): Array<Doc?> {
            return arrayOfNulls(size)
        }
    }

}