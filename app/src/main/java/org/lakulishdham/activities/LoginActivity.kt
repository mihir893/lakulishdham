package org.lakulishdham.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.LoginViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.viewmodels.LoginViewModel

class LoginActivity : BaseActivity(), View.OnClickListener, LoginViewModel.LoginViewModelCallback {

    companion object {
        const val INTENT_MOBILE = "mobile"
        const val INTENT_FROM = "from"
    }

    lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        initListeners()

    }

    private fun init() {

        viewModel = ViewModelProvider(this,LoginViewModelFactory(this,this)).get(LoginViewModel :: class.java)
    }

    private fun initListeners() {

        txtRegister.setOnClickListener(this)
        txtForgotPassword.setOnClickListener(this)
        btnSignin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.txtRegister -> {
                fireIntent(SignUpActivity::class.java,false)
            }
            R.id.txtForgotPassword -> {
                fireIntent(ForgotPasswordActivity::class.java,false)
            }
            R.id.btnSignin -> {
                hideKeypad()
                viewModel.login(edMobileNumber.Text(),edPassword.Text())
            }
        }


    }

    override fun onSuccuessLogin() {
        fireIntent(DashboardActivity::class.java,true)
    }

    override fun onGoToVerification() {
        val intent = Intent(this,VerificationActivity::class.java)
        intent.putExtra(INTENT_MOBILE,edMobileNumber.Text())
        intent.putExtra(INTENT_FROM,AppConstants.FROM_LOGIN)
        fireIntentWithData(intent,false)
    }

    override fun onError(err: String) {
        showRedError(err)
    }


}