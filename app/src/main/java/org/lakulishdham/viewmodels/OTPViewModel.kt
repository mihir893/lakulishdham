package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.LoginRequest
import org.lakulishdham.model.UserData
import org.lakulishdham.model.VerifyOtpData

class OTPViewModel(var context: Context, var callBack: OTPViewModelCallback) : ViewModel() {


    fun sendOTP(phone : String) {

        val loginRequest = LoginRequest()
        loginRequest.phone_number = phone

        LoginApiClient(context).sendOTP(loginRequest,object : LoginApiClient.sendOtpApiCallBack{
            override fun getData(data: UserData?) {
                callBack.onSuccessSend(data)
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

    fun verifyOTP(phone : String,otp : String?) {

        if (otp.isNullOrEmpty()) {
            callBack.onError("Please enter verification code")
            return
        }

        if (otp.length != 4) {
            callBack.onError("Please enter verification code of 4 digits.")
        }


        val loginRequest = LoginRequest()
        loginRequest.phone_number = phone
        loginRequest.otp = otp

        LoginApiClient(context).verifyOTP(loginRequest,object : LoginApiClient.verifyOtpApiCallback{
            override fun onVerificationSuccessfull(data: VerifyOtpData?) {
                data?.token?.let { PrefUtils.setAuthKey(context, it) }
                data?.data?.let { PrefUtils.setUserData(context, it) }
                callBack.onVerificationSuccessful(data)
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


    interface OTPViewModelCallback : ErrorCallBack {
        fun onSuccessSend(data: UserData?)
        fun onVerificationSuccessful(data: VerifyOtpData?)
    }
}