package com.ibm.callforcode.activity

import android.os.Bundle
import com.ibm.callforcode.R
import com.ibm.callforcode.activity.CCCBuilderActivity

class MainActivity : CCCBuilderActivity() {
    override fun setLayoutView() = R.layout.activity_main

    override fun initialize(savedInstanceState: Bundle?) {

    }
}