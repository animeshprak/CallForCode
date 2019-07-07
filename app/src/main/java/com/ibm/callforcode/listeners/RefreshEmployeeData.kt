package com.ibm.callforcode.listeners

import com.ibm.callforcode.webservice.data.Doc

interface RefreshEmployeeData {
    fun refreshData(doc: Doc)
}