package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_reset_password.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.LoginViewModelFactory
import org.lakulishdham.factories.ResetPasswordViewModelFactory
import org.lakulishdham.helper.Text
import org.lakulishdham.helper.closeScreen
import org.lakulishdham.helper.fireIntent
import org.lakulishdham.helper.showRedError
import org.lakulishdham.viewmodels.LoginViewModel
import org.lakulishdham.viewmodels.ResetPasswordViewModel

class ResetPasswordActivity : BaseActivity(),
    ResetPasswordViewModel.ResetPasswordViewModelCallback, View.OnClickListener {

    lateinit var viewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, ResetPasswordViewModelFactory(this,this)).get(ResetPasswordViewModel :: class.java)
        btnReset.setOnClickListener(this)
    }

    fun goBack(view: View) {
        closeScreen()
    }

    override fun onResetSuccessful() {
        fireIntent(LoginActivity::class.java,true)
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.btnReset -> {
                viewModel.resetPassword(edPassword.Text(),edRePassword.Text())
            }

        }
    }
}