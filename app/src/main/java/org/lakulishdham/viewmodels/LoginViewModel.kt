package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import com.adcreators.youtique.helper.Prefs
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.LoginRequest
import org.lakulishdham.model.UserData
import org.lakulishdham.model.VerifyOtpData

class LoginViewModel(var context: Context, var callBack: LoginViewModelCallback) : ViewModel() {


    fun login(phone : String,password : String) {

        if (!phone.isValidPhone()) {
            callBack.onError(context.resources.getString(R.string.valid_mobile_number))
            return
        }

        if (!password.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.valid_password))
            return
        }

        val loginRequest = LoginRequest()
        loginRequest.phone_number = phone
        loginRequest.password = password

        LoginApiClient(context).doLogin(loginRequest,object : LoginApiClient.LoginApiCallBack{
            override fun getData(data: VerifyOtpData?) {
                data?.token?.let { PrefUtils.setAuthKey(context, it) }
                data?.data?.let { PrefUtils.setUserData(context, it) }
                PrefUtils.setUserLogin(context,true)

                callBack.onSuccuessLogin()
            }

            override fun goToVerification() {
                callBack.onGoToVerification()
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



    interface LoginViewModelCallback : ErrorCallBack {
        fun onSuccuessLogin()
        fun onGoToVerification()
    }
}