package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.DashboardViewModel
import org.lakulishdham.viewmodels.LoginViewModel

class DashboardViewModelFactory(private val context: Context, private val callback: DashboardViewModel.DashboardViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardViewModel(context, callback) as T
    }
}