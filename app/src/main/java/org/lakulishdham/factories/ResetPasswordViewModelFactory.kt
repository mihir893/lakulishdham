package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.ResetPasswordViewModel

class ResetPasswordViewModelFactory(private val context: Context, private val callback: ResetPasswordViewModel.ResetPasswordViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ResetPasswordViewModel(context, callback) as T
    }
}