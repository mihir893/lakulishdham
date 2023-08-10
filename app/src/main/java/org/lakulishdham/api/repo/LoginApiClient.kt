package org.lakulishdham.api.repo

import android.content.Context
import org.lakulishdham.R
import org.lakulishdham.api.ApiService
import org.lakulishdham.api.Apis
import org.lakulishdham.api.BaseInterface
import org.lakulishdham.helper.AppApplication
import org.lakulishdham.helper.AppLogger
import org.lakulishdham.helper.Functions
import org.lakulishdham.model.*
import org.lakulishdham.utility.CustomProgressUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

open class LoginApiClient(var context: Context) {

    private var apiService: ApiService = AppApplication.retrofit!!.create(ApiService::class.java)

    fun doLogin(request: LoginRequest, callBack: LoginApiCallBack) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.doLogin(request).enqueue(object : Callback<VerifyResponse> {
            override fun onResponse(call: Call<VerifyResponse>, response: Response<VerifyResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.goToVerification()
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.getData(registerResponse.response)
                            }
                            else -> {
                                callBack.onError(registerResponse?.message)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<VerifyResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun sendOTP(request: LoginRequest, callBack: sendOtpApiCallBack) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.sendOTP(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.getData(registerResponse.response)
                            }
                            else -> {
                                callBack.onError(registerResponse?.message)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun verifyOTP(request: LoginRequest, callBack: verifyOtpApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.verifyOTP(request).enqueue(object : Callback<VerifyResponse> {
            override fun onResponse(call: Call<VerifyResponse>, response: Response<VerifyResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.onVerificationSuccessfull(registerResponse.response)
                            }
                            else -> {
                                callBack.onError(registerResponse?.message)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<VerifyResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun resetPassword(request: LoginRequest, callBack: ResetPasswordApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.resetPassword(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.onResetSuccessful(registerResponse.response)
                            }
                            else -> {
                                callBack.onError(registerResponse?.message)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun logout(callBack: LogoutApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.logout().enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onError(registerResponse.message)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.onLogoutSuccessful()
                            }
                            else -> {
                                callBack.onError(registerResponse?.message)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    interface LoginApiCallBack : BaseInterface {
        fun getData(data: VerifyOtpData?)
        fun goToVerification()
    }

    interface sendOtpApiCallBack : BaseInterface {
        fun getData(data : UserData?)
    }

    interface verifyOtpApiCallback : BaseInterface {
        fun onVerificationSuccessfull(data : VerifyOtpData?)
    }

    interface ResetPasswordApiCallback : BaseInterface {
        fun onResetSuccessful(data : UserData?)
    }

    interface LogoutApiCallback : BaseInterface {
        fun onLogoutSuccessful()
    }
}