package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.adcreators.youtique.helper.PrefUtils
import kotlinx.android.synthetic.main.activity_my_account.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.DashboardViewModelFactory
import org.lakulishdham.factories.MyAccountViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.model.UserData
import org.lakulishdham.viewmodels.DashboardViewModel
import org.lakulishdham.viewmodels.MyAccountViewModel

class MyAccountActivity : BaseActivity(), View.OnClickListener,
    MyAccountViewModel.AccountViewModelCallback {

    lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        init()
        initListeners()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, MyAccountViewModelFactory(this,this)).get(
            MyAccountViewModel :: class.java)

    }

    override fun onResume() {
        super.onResume()

        setUserInfo()
    }

    private fun initListeners() {

        txtMembers.setOnClickListener(this)
        txtDonations.setOnClickListener(this)
        txtProfile.setOnClickListener(this)
        txtChangePassword.setOnClickListener(this)
        txtLogout.setOnClickListener(this)
        txtMySubscription.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.txtMembers -> {
                fireIntent(MembersActivity::class.java, false)
            }
            R.id.txtDonations -> {
                fireIntent(DonationListActivity::class.java, false)
            }
            R.id.txtProfile -> {
                fireIntent(EditProfileActivity::class.java, false)
            }
            R.id.txtChangePassword -> {
                fireIntent(ChangePasswordActivity::class.java, false)
            }
            R.id.txtLogout -> {
                performLogout()
            }
            R.id.txtMySubscription -> {
                fireIntent(MySubscriptionActivity::class.java, false)
            }
        }
    }

    private fun performLogout() {
        showAlert("Are you sure want to logout?","yes","no",object : DialogOptionsSelectedListener{
            override fun onSelect(isYes: Boolean) {
                viewModel.Logout()
            }
        },object : DialogOptionsSelectedListener{
            override fun onSelect(isYes: Boolean) {
            }
        })
    }




    fun setUserInfo() {
        val data : UserData = PrefUtils.getUserData(this)
        data.let {
            txtUsername.text = data.name
            txtUserMobile.text = data.phone_number
            txtUserNameInitials.text = data.name?.GetInitials()

            if(data.plan != null) {
                view_my_sub.visibility = View.VISIBLE
                txtMySubscription.visibility = View.VISIBLE
            }
            else {
                txtMySubscription.visibility = View.GONE
                view_my_sub.visibility = View.GONE
            }

        }
    }

    fun goBack(view: View) {
        closeScreen()
    }

    override fun onLogoutSuccess() {
        PrefUtils.clearAll(this)
        fireIntent(LoginActivity::class.java,true)
    }

    override fun onCancelSubscriptionSuccess(data: UserData?) {
        val data : UserData = PrefUtils.getUserData(this)
        data.subscription_id = ""
        data.plan = null

        PrefUtils.setUserData(this,data)

        setUserInfo()
    }

    override fun onError(err: String) {
        showRedError(err)
    }


}