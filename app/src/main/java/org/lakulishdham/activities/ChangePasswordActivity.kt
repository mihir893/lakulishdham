package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_change_password.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.EditProfileViewModelFactory
import org.lakulishdham.factories.UpdatePasswordViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.viewmodels.EditProfileViewModel
import org.lakulishdham.viewmodels.UpdatePasswordViewModel

class ChangePasswordActivity : BaseActivity(),
    UpdatePasswordViewModel.UpdatePasswordViewModelCallback, View.OnClickListener {

    lateinit var viewModel: UpdatePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        init()
    }

    fun init() {

        viewModel = ViewModelProvider(this, UpdatePasswordViewModelFactory(this,this)).get(UpdatePasswordViewModel :: class.java)

        btnChange.setOnClickListener(this)

    }

    override fun onPasswordUpdateSuccess() {
        showToast("Password Updated Successfully")
        closeScreen()
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    override fun onClick(v: View?) {

        when(v?.id) {

            R.id.btnChange -> {
                hideKeypad()
                viewModel.ChangePassword(edOldPassword.Text(),edPassword.Text(),edRePassword.Text())
            }

        }

    }

    fun goBack(view: View) {
        closeScreen()
    }
}