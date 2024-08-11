package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.DonationApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.AddDonationRequest
import org.lakulishdham.model.DonationListData
import org.lakulishdham.model.GenerateOrderRequest
import org.lakulishdham.model.LoginRequest
import org.lakulishdham.model.OrderData
import org.lakulishdham.model.UserData
import org.lakulishdham.model.VerifyOtpData

class MyAccountViewModel(var context: Context, var callBack: AccountViewModelCallback) : ViewModel() {


    fun Logout() {
        LoginApiClient(context).logout(object : LoginApiClient.LogoutApiCallback{
            override fun onLogoutSuccessful() {
                callBack.onLogoutSuccess()
            }

            override fun onError(errorString: String?) {
                if (errorString != null) {
                    callBack.onError(errorString)
                }
            }

            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })

    }

    fun CancelSubscription() {
        DonationApiClient(context).CancelSubscription(object : DonationApiClient.onCancelSubscriptionApiCallback{
            override fun onSubscriptionCancelSuccess(data: UserData?) {
                callBack.onCancelSubscriptionSuccess(data)
            }
            override fun onError(errorString: String?) {
                if (errorString != null) {
                    callBack.onError(errorString)
                }
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })

    }

    fun generateOrder(amount : String) {

        if (amount.isNullOrEmpty()) {
            callBack.onError("Please enter amount")
            return
        }

        if (amount.toInt() <= 0) {
            callBack.onError("You can not donate amount : 0")
            return
        }

        val request = GenerateOrderRequest()
        request.amount = amount

        DashboardApiClient(context).generateOrder(request,object : DashboardApiClient.onGenerateOrderApiCallback{
            override fun onGetData(data: OrderData?) {
                callBack.onGetOrderData(data)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    fun AddDonation(request: AddDonationRequest) {

        DonationApiClient(context).AddDonation(request,object : DonationApiClient.onAddDonationApiCallback{
            override fun onDonationSuccess(data: DonationListData?) {
                callBack.onDonationSuccessful(data)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }


    interface AccountViewModelCallback : ErrorCallBack {
        fun onLogoutSuccess()
        fun onCancelSubscriptionSuccess(data : UserData?)
        fun onGetOrderData(data: OrderData?)

        fun onDonationSuccessful(data : DonationListData?)
    }
}