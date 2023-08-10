package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.LoginRequest
import org.lakulishdham.model.UserData

class ResetPasswordViewModel(var context: Context, var callBack: ResetPasswordViewModelCallback) : ViewModel() {


    fun resetPassword(password : String,confirmpassword : String) {

        if (!password.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.valid_password))
            return
        }

        if (!confirmpassword.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.confirm_valid_password))
            return
        }

        if (!password.equals(confirmpassword)) {
            callBack.onError(context.resources.getString(R.string.password_not_match))
            return
        }

        val loginRequest = LoginRequest()
        loginRequest.password = password
        loginRequest.c_password = confirmpassword

        LoginApiClient(context).resetPassword(loginRequest,object : LoginApiClient.ResetPasswordApiCallback{
            override fun onResetSuccessful(data: UserData?) {
                callBack.onResetSuccessful()
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


    interface ResetPasswordViewModelCallback : ErrorCallBack {
        fun onResetSuccessful()
    }
}