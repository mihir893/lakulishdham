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

open class DonationApiClient(var context: Context) {

    private var apiService: ApiService = AppApplication.retrofit!!.create(ApiService::class.java)

    fun getDonationList(callBack: onDonationListApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.GetDonations().enqueue(object : Callback<DonationListResponse> {
            override fun onResponse(call: Call<DonationListResponse>, response: Response<DonationListResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onGetData(null)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onGetData(null)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.onGetData(registerResponse.response?.data)
                            }
                            else -> {
                                callBack.onGetData(null)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<DonationListResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun AddDonation(request: AddDonationRequest,callBack: onAddDonationApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.AddDonations(request).enqueue(object : Callback<AddDonationsResponse> {
            override fun onResponse(call: Call<AddDonationsResponse>, response: Response<AddDonationsResponse>) {
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
                                callBack.onDonationSuccess(registerResponse.response)
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

            override fun onFailure(call: Call<AddDonationsResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun CancelSubscription(callBack: onCancelSubscriptionApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.CancelSubscription().enqueue(object : Callback<LoginResponse> {
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
                                callBack.onSubscriptionCancelSuccess(registerResponse.response)
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

    interface onDonationListApiCallback : BaseInterface {
        fun onGetData(list : ArrayList<DonationListData>?)
    }

    interface onAddDonationApiCallback : BaseInterface {
        fun onDonationSuccess(data : DonationListData?)
    }

    interface onCancelSubscriptionApiCallback : BaseInterface {
        fun onSubscriptionCancelSuccess(data : UserData?)
    }
}