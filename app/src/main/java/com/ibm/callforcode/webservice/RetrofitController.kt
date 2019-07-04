package com.sample.webservice

import com.google.gson.GsonBuilder
import com.ibm.callforcode.webservice.data.Employees
import com.sample.utils.AppConstants.Companion.BASE_URL
import com.sample.utils.getOkHttpClientBuilder
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitController {
    companion object {
        private val mRetrofit: Retrofit = getInstance()
        private val webserviceApi = mRetrofit.create<WebServiceApiInterface>(WebServiceApiInterface::class.java!!)

        fun getInstance() : Retrofit {
            if (mRetrofit == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                val client = getOkHttpClientBuilder()
                client.addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Basic M2YyOTA1ZDItYTZlZi00YzQ3LWFiYzAtNjk0YTc3MjdmMTE2LWJsdWVtaXg6OTJmYzk0YzgzNjExYTE2NjMyMGMwMmQ3ZjcxNmNhYjY5M2VlYWFkYTRkMzQxYzJmZDJhYTE4ZDRiNjNkNTUzNA==")
                        .build()
                    chain.proceed(newRequest)
                }

                return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client.build())
                    .build()
            }

            return mRetrofit
        }

        fun getCharactersData(charactersCallBack: Callback<Employees>) {
            val call = webserviceApi.getCharacters()
            call.enqueue(charactersCallBack)
        }
    }
}