package com.ibm.callforcode.listeners

import com.ibm.callforcode.webservice.data.Doc

interface RefreshAllEmployeeData {
    fun refreshData(docList: ArrayList<Doc>)
}