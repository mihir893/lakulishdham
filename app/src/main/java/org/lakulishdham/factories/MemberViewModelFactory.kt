package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.MemberViewModel
import org.lakulishdham.viewmodels.OTPViewModel

class MemberViewModelFactory(private val context: Context, private val callback: MemberViewModel.MemberViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MemberViewModel(context, callback) as T
    }
}