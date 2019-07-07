package com.ibm.callforcode.webservice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Value {
    @SerializedName("rev")
    @Expose
    private var rev: String? = null

    fun getRev(): String? {
        return rev
    }

    fun setRev(rev: String) {
        this.rev = rev
    }
}