package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.MyAccountViewModel
import org.lakulishdham.viewmodels.OTPViewModel

class MyAccountViewModelFactory(private val context: Context, private val callback: MyAccountViewModel.AccountViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyAccountViewModel(context, callback) as T
    }
}