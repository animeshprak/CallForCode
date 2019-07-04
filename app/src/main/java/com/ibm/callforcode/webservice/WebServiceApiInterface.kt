package com.sample.webservice

import com.ibm.callforcode.BuildConfig
import com.ibm.callforcode.webservice.data.Employees
import retrofit2.Call
import retrofit2.http.GET


interface WebServiceApiInterface {

    @GET(BuildConfig.DATA_API_PATH)
    fun getCharacters(): Call<Employees>
}