package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.DonationViewModel
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.OTPViewModel

class DonationViewModelFactory(private val context: Context, private val callback: DonationViewModel.DonationViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DonationViewModel(context, callback) as T
    }
}