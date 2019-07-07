package com.sample.webservice

import com.ibm.callforcode.webservice.data.Doc
import com.ibm.callforcode.webservice.data.Employees
import com.ibm.callforcode.webservice.emergency.EmergencyResponse
import com.ibm.callforcode.webservice.updatestatus.UpdatedStatusResponse
import com.sample.utils.AppConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface WebServiceApiInterface {

    @GET(AppConstants.EMP_LIST_URL)
    fun getEmployees(): Call<Employees>

    @PUT(AppConstants.UPDATE_EMP_STATUS_URL)
    fun updateEmployeeStatus(@Path("emp_id") empId: String, @Body doc: Doc): Call<UpdatedStatusResponse>

    @GET(AppConstants.EMERGENCY_STATUS_GETTER_URL)
    fun getEmergencyStatus(): Call<EmergencyResponse>

    @PUT(AppConstants.EMERGENCY_STATUS_GETTER_URL)
    fun setEmergencyStatus(@Body doc: EmergencyResponse): Call<UpdatedStatusResponse>
}