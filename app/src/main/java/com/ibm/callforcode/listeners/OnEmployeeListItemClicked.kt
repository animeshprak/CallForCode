package com.sample.listeners

import com.ibm.callforcode.webservice.data.Doc

interface OnEmployeeListItemClicked {
    fun onEmployeeItemClicked(relatedTopic: Doc, title : String)
}
