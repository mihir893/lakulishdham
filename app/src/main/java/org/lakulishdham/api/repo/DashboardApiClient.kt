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

open class DashboardApiClient(var context: Context) {

    private var apiService: ApiService = AppApplication.retrofit!!.create(ApiService::class.java)

    fun fetchGalleryImages(callBack: onGalleryListApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.galleryList().enqueue(object : Callback<GalleryListResponse> {
            override fun onResponse(call: Call<GalleryListResponse>, response: Response<GalleryListResponse>) {
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
                                callBack.onGetData(registerResponse.response)
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

            override fun onFailure(call: Call<GalleryListResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun generateOrder(request: GenerateOrderRequest,callBack: onGenerateOrderApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.generateOrder(request).enqueue(object : Callback<GenerateOrderResponse> {
            override fun onResponse(call: Call<GenerateOrderResponse>, response: Response<GenerateOrderResponse>) {
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
                                callBack.onGetData(registerResponse.response)
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

            override fun onFailure(call: Call<GenerateOrderResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    interface onGalleryListApiCallback : BaseInterface {
        fun onGetData(data : GalleryData?)
    }

    interface onGenerateOrderApiCallback : BaseInterface {
        fun onGetData(data : OrderData?)
    }

}