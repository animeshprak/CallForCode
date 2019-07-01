package com.sample.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.gmail.samehadar.iosdialog.IOSDialog
import com.ibm.callforcode.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import javax.security.cert.CertificateException
import okhttp3.logging.HttpLoggingInterceptor

class AppExtensions {}

fun Context.getProgressView() : IOSDialog {
    val dialog = IOSDialog.Builder(this)
        .setOnCancelListener {

        }
        .setDimAmount(3f)
        .setSpinnerColorRes(android.R.color.white)
        .setMessageColorRes(android.R.color.white)
        // .setTitle(R.string.standard_title)
        .setTitleColorRes(android.R.color.white)
        .setMessageContent("Loading...")
        .setCancelable(false)
        .setMessageContentGravity(Gravity.END)
        .build()
    return dialog
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Activity.hideKeyboard() {
    if(!this.isFinishing) {
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.slideActivityLeftToRight() {
    this.overridePendingTransition(R.anim.left_to_right_start, R.anim.right_to_left_end)
}

fun Activity.slideActivityRightToLeft() {
    this.overridePendingTransition(R.anim.right_to_left_start, R.anim.left_to_right_end)
}

fun getOkHttpClientBuilder() : OkHttpClient.Builder {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

        }
    })

    // Install the all-trusting trust manager

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
    client.interceptors().add(httpLoggingInterceptor)
    client.readTimeout(180, TimeUnit.SECONDS)
    client.connectTimeout(180, TimeUnit.SECONDS)

    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null, null)

    val sslContext = SSLContext.getInstance("TLS")

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, "keystore_pass".toCharArray())
    sslContext.init(null, trustAllCerts, SecureRandom())
    client.sslSocketFactory(sslContext.socketFactory)
        .hostnameVerifier { _, _ -> true }
    return client
}

fun Context.isTablet() = resources.getBoolean(R.bool.isTablet)