package org.lakulishdham.factories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.lakulishdham.viewmodels.EditProfileViewModel
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.OTPViewModel
import org.lakulishdham.viewmodels.SignUpViewModel

class EditProfileViewModelFactory(private val context: Context, private val callback: EditProfileViewModel.EditProfileViewModelCallback) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(context, callback) as T
    }
}