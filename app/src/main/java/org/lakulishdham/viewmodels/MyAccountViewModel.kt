package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DonationApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.LoginRequest
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


    interface AccountViewModelCallback : ErrorCallBack {
        fun onLogoutSuccess()
        fun onCancelSubscriptionSuccess(data : UserData?)
    }
}