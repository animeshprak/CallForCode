package com.ibm.callforcode.webservice.updatestatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UpdatedStatusResponse {
    @SerializedName("ok")
    @Expose
    private var ok: Boolean? = null
    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("rev")
    @Expose
    private var rev: String? = null

    fun getOk(): Boolean? {
        return ok
    }

    fun setOk(ok: Boolean?) {
        this.ok = ok
    }

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
}