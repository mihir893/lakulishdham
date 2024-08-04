package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.OTPViewModel
import org.lakulishdham.viewmodels.SuubscriptionViewModel

class SubscriptionViewModelFactory(private val context: Context, private val callback: SuubscriptionViewModel.SubscriptionViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SuubscriptionViewModel(context, callback) as T
    }
}