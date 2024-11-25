package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.OTPViewModel
import org.lakulishdham.viewmodels.UpdatePasswordViewModel

class UpdatePasswordViewModelFactory(private val context: Context, private val callback: UpdatePasswordViewModel.UpdatePasswordViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UpdatePasswordViewModel(context, callback) as T
    }
}