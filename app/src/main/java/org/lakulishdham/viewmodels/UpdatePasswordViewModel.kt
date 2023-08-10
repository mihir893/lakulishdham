package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.JsonObject
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.api.repo.SignupApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.*

class UpdatePasswordViewModel(var context: Context, var callBack: UpdatePasswordViewModelCallback) : ViewModel() {


    fun ChangePassword(old_passwrod : String, new_password : String, confirm_new_password : String) {

        if (!old_passwrod.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.old_valid_password))
            return
        }

        if (!new_password.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.new_valid_password))
            return
        }

        if (!confirm_new_password.isValidPassword()) {
            callBack.onError(context.resources.getString(R.string.confirm_new_valid_password))
            return
        }

        if (!new_password.equals(confirm_new_password)) {
            callBack.onError(context.resources.getString(R.string.password_not_match))
            return
        }

        val request = UpdatePasswordRequest()
        request.old_password = old_passwrod
        request.new_password = new_password
        request.confirm_new_password = confirm_new_password

        SignupApiClient(context).UpdatePassword(request,object : SignupApiClient.onUpdatePasswordApiCallback{
            override fun onPasswordUpdateSuccess() {
                callBack.onPasswordUpdateSuccess()
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    interface UpdatePasswordViewModelCallback : ErrorCallBack {
        fun onPasswordUpdateSuccess()
    }
}