package org.lakulishdham.api.repo

import android.content.Context
import com.google.gson.JsonObject
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

open class MemberApiClient(var context: Context) {

    private var apiService: ApiService = AppApplication.retrofit!!.create(ApiService::class.java)

    fun getMemberList(callBack: onMemberListApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.getMembers().enqueue(object : Callback<MemberListResponse> {
            override fun onResponse(call: Call<MemberListResponse>, response: Response<MemberListResponse>) {
                CustomProgressUtils.hideProgress()

                when (response.code()) {
                    Apis.OK_RESPONSE -> {
                        // success
                        val registerResponse = response.body()
                        when(registerResponse?.code) {
                            Apis.CODE_UNAUTHORIZED_REQUEST -> {
                                callBack.onGetMembersData(null)
                            }
                            Apis.CODE_VALIDATION_ERROR -> {
                                callBack.onGetMembersData(null)
                            }
                            Apis.OK_RESPONSE -> {
                                callBack.onGetMembersData(registerResponse.response?.data)
                            }
                            else -> {
                                callBack.onGetMembersData(null)
                            }
                        }
                    }
                    else -> {
                        // if response is null or any other error
                        callBack.onError(context.getString(R.string.try_again))
                    }
                }
            }

            override fun onFailure(call: Call<MemberListResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun AddMembers(jsonObject: JsonObject,callBack: onAddMemberApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.addMembers(jsonObject).enqueue(object : Callback<AddMemberResponse> {
            override fun onResponse(call: Call<AddMemberResponse>, response: Response<AddMemberResponse>) {
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
                                callBack.onAddMemberSuccess(registerResponse.response)
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

            override fun onFailure(call: Call<AddMemberResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun UpdateMembers(jsonObject: JsonObject,callBack: onUdateMemberApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.updateMembers(jsonObject).enqueue(object : Callback<AddMemberResponse> {
            override fun onResponse(call: Call<AddMemberResponse>, response: Response<AddMemberResponse>) {
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
                                callBack.onUpdateMemberSuccess(registerResponse.response)
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

            override fun onFailure(call: Call<AddMemberResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    fun DeleteMembers(jsonObject: JsonObject,callBack: onDeleteMemberApiCallback) {
        if (!Functions.isInternetConnected(context)) {
            callBack.onError(context.getString(R.string.no_internet))
            return
        }
        CustomProgressUtils.showProgress(context)
        apiService.DeleteMembers(jsonObject).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
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
                                callBack.onDeleteMemberSuccess()
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

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                CustomProgressUtils.hideProgress()
                callBack.onError(t.message!!)
            }
        })

    }

    interface onMemberListApiCallback : BaseInterface {
        fun onGetMembersData(list : ArrayList<MemberListData>?)
    }

    interface onAddMemberApiCallback : BaseInterface {
        fun onAddMemberSuccess(data : MemberListData?)
    }

    interface onUdateMemberApiCallback : BaseInterface {
        fun onUpdateMemberSuccess(data : MemberListData?)
    }

    interface onDeleteMemberApiCallback : BaseInterface {
        fun onDeleteMemberSuccess()
    }

}