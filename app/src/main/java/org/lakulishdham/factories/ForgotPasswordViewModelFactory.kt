package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.ForgotPasswordViewModel
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.OTPViewModel

class ForgotPasswordViewModelFactory(private val context: Context, private val callback: ForgotPasswordViewModel.ForgotPasswordViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ForgotPasswordViewModel(context, callback) as T
    }
}