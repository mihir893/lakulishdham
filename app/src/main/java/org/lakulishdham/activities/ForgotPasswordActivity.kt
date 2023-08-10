package org.lakulishdham.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.ForgotPasswordViewModelFactory
import org.lakulishdham.factories.OTPViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.model.UserData
import org.lakulishdham.viewmodels.ForgotPasswordViewModel
import org.lakulishdham.viewmodels.OTPViewModel

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener, ForgotPasswordViewModel.ForgotPasswordViewModelCallback {


    lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        viewModel = ViewModelProvider(this, ForgotPasswordViewModelFactory(this,this)).get(ForgotPasswordViewModel :: class.java)

        initListeners()
    }

    private fun initListeners() {
        btnSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnSend -> {
                viewModel.validate(edMobileNumber.Text())
            }
        }
    }

    override fun onSuccessSend() {
        val intent = Intent(this,VerificationActivity::class.java)
        intent.putExtra(LoginActivity.INTENT_MOBILE,edMobileNumber.Text())
        intent.putExtra(LoginActivity.INTENT_FROM,AppConstants.FROM_FORGOT_PASSWORD)
        fireIntentWithData(intent,false)
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    fun goBack(view: View) {
        closeScreen()
    }
}