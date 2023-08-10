package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.JsonObject
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.api.repo.MemberApiClient
import org.lakulishdham.api.repo.SubscriptionApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.*

class MemberViewModel(var context: Context, var callBack: MemberViewModelCallback) : ViewModel() {


    fun getMembers() {
        MemberApiClient(context).getMemberList(object : MemberApiClient.onMemberListApiCallback{
            override fun onGetMembersData(list: ArrayList<MemberListData>?) {
                callBack.onGetData(list)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    fun AddMember(jsonObject: JsonObject) {
        MemberApiClient(context).AddMembers(jsonObject,object : MemberApiClient.onAddMemberApiCallback{
            override fun onAddMemberSuccess(data: MemberListData?) {
                callBack.onAddMemberSuccess(data)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    fun UpdateMember(jsonObject: JsonObject) {
        MemberApiClient(context).UpdateMembers(jsonObject,object : MemberApiClient.onUdateMemberApiCallback{
            override fun onUpdateMemberSuccess(data: MemberListData?) {
                callBack.onUpdateMemberSuccess(data)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    fun DeleteMember(jsonObject: JsonObject,memberListData: MemberListData) {
        MemberApiClient(context).DeleteMembers(jsonObject,object : MemberApiClient.onDeleteMemberApiCallback{
            override fun onDeleteMemberSuccess() {
                callBack.onDeleteMemberSuccess(memberListData)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    interface MemberViewModelCallback : ErrorCallBack {
        fun onGetData(list: ArrayList<MemberListData>?)
        fun onAddMemberSuccess(data : MemberListData?)
        fun onUpdateMemberSuccess(data : MemberListData?)
        fun onDeleteMemberSuccess(memberListData: MemberListData)
    }
}