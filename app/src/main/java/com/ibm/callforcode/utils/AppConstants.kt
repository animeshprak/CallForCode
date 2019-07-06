package com.sample.utils

/**
 * Created by Animesh on 07/02/19.
 */
class AppConstants {
    companion object {
        const val BUNDLE = "bundle"
        const val BASE_URL = "https://3f2905d2-a6ef-4c47-abc0-694a7727f116-bluemix.cloudant.com/callforcode/"
        const val EMP_LIST_URL = "_all_docs?include_docs=true"
        const val TITLE = "Call For Code"
        const val SPLASH_TIME = 3000L
        const val PREF_FILE = "callforcodepref"
        const val ADMIN = "admin"

        enum class PREFERENCES private constructor(private val value: String) {
            LOGIN_STATUS("LoginStatus"),
            USER_NAME("app_config_details"),
            IS_ADMIN("database_detail");

            override fun toString(): String {
                return this.value
            }
        }
    }
}