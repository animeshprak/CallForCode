package com.sample.utils

import com.ibm.callforcode.BuildConfig

/**
 * Created by Animesh on 07/02/19.
 */
class AppConstants {
    companion object {
        const val BUNDLE = "bundle"
        const val BASE_URL = "https://3f2905d2-a6ef-4c47-abc0-694a7727f116-bluemix.cloudant.com/"
        const val EMP_LIST_URL = "callforcode/_all_docs?include_docs=true"
        const val UPDATE_EMP_STATUS_URL = "callforcode/{emp_id}/"
        const val EMERGENCY_STATUS_GETTER_URL = "emergeydb/1001/"
        const val TITLE = "Call For Code"
        const val SPLASH_TIME = 3000L
        const val PREF_FILE = "callforcodepref" + BuildConfig.LEVEL
        const val ADMIN = "admin"

        enum class PREFERENCES private constructor(private val value: String) {
            LOGIN_STATUS("LoginStatus"),
            EMERGENCY("Emergency"),
            USER_NAME("app_config_details"),
            IS_ADMIN("database_detail");

            override fun toString(): String {
                return this.value
            }
        }
    }
}