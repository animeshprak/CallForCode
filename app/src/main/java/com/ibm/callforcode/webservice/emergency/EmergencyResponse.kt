package com.ibm.callforcode.webservice.emergency

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmergencyResponse {

    @SerializedName("_id")
    @Expose
    private var id: String? = null
    @SerializedName("_rev")
    @Expose
    private var rev: String? = null
    @SerializedName("isEmergencyTrigged")
    @Expose
    private var isEmergencyTrigged: Boolean? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getRev(): String? {
        return rev
    }

    fun setRev(rev: String) {
        this.rev = rev
    }

    fun getIsEmergencyTrigged(): Boolean? {
        return isEmergencyTrigged
    }

    fun setIsEmergencyTrigged(isEmergencyTrigged: Boolean?) {
        this.isEmergencyTrigged = isEmergencyTrigged
    }
}