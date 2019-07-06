package com.sample.webservice

import com.ibm.callforcode.BuildConfig
import com.ibm.callforcode.webservice.data.Employees
import com.sample.utils.AppConstants
import retrofit2.Call
import retrofit2.http.GET


interface WebServiceApiInterface {

    @GET(AppConstants.EMP_LIST_URL)
    fun getCharacters(): Call<Employees>
}