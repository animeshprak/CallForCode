package com.ibm.callforcode.listeners

import com.ibm.callforcode.webservice.data.Doc

interface OnEmployeeChangedTheStatus {
    fun onStatusChanged(doc: Doc)
}