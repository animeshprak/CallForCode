package com.ibm.callforcode.webservice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Row {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("value")
    @Expose
    var value: Value? = null
    @SerializedName("doc")
    @Expose
    var doc: Doc? = null

}