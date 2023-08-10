package org.lakulishdham.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.text.bold
import androidx.lifecycle.ViewModelProvider
import com.adcreators.youtique.helper.PrefUtils
import kotlinx.android.synthetic.main.activity_verification.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.OTPViewModelFactory
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.closeScreen
import org.lakulishdham.helper.fireIntent
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.UserData
import org.lakulishdham.model.VerifyOtpData
import org.lakulishdham.viewmodels.OTPViewModel

class VerificationActivity : BaseActivity(), OTPViewModel.OTPViewModelCallback {

    lateinit var mobile: String
    lateinit var from: String
    lateinit var viewModel: OTPViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        init()
    }

    fun init() {
        mobile = intent.getStringExtra(LoginActivity.INTENT_MOBILE)!!
        from = intent.getStringExtra(LoginActivity.INTENT_FROM)!!

        viewModel =
            ViewModelProvider(this, OTPViewModelFactory(this, this)).get(OTPViewModel::class.java)

        val s = SpannableStringBuilder()
            .append("Enter the verification code sent to number\n")
            .bold { append("+91 ${mobile}") }
        txt_msg.text = s

        viewModel.sendOTP(mobile)
    }

    fun OnVerifyClick(view: View) {
        viewModel.verifyOTP(mobile, otp_view.otp)
    }

    override fun onSuccessSend(data: UserData?) {
//        showRedError("OTP : ${data?.otp}")
    }

    override fun onVerificationSuccessful(data: VerifyOtpData?) {

        when (from) {
            AppConstants.FROM_LOGIN, AppConstants.FROM_REGISTER -> {
                PrefUtils.setUserLogin(this, true)
                fireIntent(DashboardActivity::class.java, true)
            }
            AppConstants.FROM_FORGOT_PASSWORD -> {
                fireIntent(ResetPasswordActivity::class.java, true)
            }
        }


    }

    override fun onError(err: String) {
        otp_view.setOTP("")
        showRedError(err)
    }

    fun onResendClick(view: View) {
        otp_view.setOTP("")
        viewModel.sendOTP(mobile)
    }

    fun goBack(view: View) {
        closeScreen()
    }

}