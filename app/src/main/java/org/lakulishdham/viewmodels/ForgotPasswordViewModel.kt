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

class ForgotPasswordViewModel(var context: Context, var callBack: ForgotPasswordViewModelCallback) : ViewModel() {

    fun validate(phone : String) {

        if (!phone.isValidPhone()) {
            callBack.onError(context.resources.getString(R.string.valid_mobile_number))
            return
        }

        callBack.onSuccessSend()

    }

    interface ForgotPasswordViewModelCallback : ErrorCallBack {
        fun onSuccessSend()
    }
}