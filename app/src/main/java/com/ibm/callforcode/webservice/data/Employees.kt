package com.ibm.callforcode.webservice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Employees {
    @SerializedName("total_rows")
    @Expose
    private var totalRows: Int? = null
    @SerializedName("offset")
    @Expose
    private var offset: Int? = null
    @SerializedName("rows")
    @Expose
    private var rows: List<Row>? = null

    fun getTotalRows(): Int? {
        return totalRows
    }

    fun setTotalRows(totalRows: Int?) {
        this.totalRows = totalRows
    }

    fun getOffset(): Int? {
        return offset
    }

    fun setOffset(offset: Int?) {
        this.offset = offset
    }

    fun getRows(): List<Row>? {
        return rows
    }

    fun setRows(rows: List<Row>) {
        this.rows = rows
    }
}