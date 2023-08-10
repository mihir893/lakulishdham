package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.adcreators.youtique.helper.PrefUtils
import kotlinx.android.synthetic.main.activity_my_subscription.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.MyAccountViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.model.UserData
import org.lakulishdham.viewmodels.MyAccountViewModel

class MySubscriptionActivity : BaseActivity(), MyAccountViewModel.AccountViewModelCallback,
    View.OnClickListener {

    lateinit var viewModel: MyAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_subscription)

        viewModel = ViewModelProvider(this, MyAccountViewModelFactory(this,this)).get(
            MyAccountViewModel :: class.java)

        setUserInfo()
    }

    fun goBack(view: View) {
        closeScreen()
    }

    fun setUserInfo() {
        val data : UserData = PrefUtils.getUserData(this)
        data.let {
            if(data.plan != null) {
                txtSubscriptionAmount.text = AppConstants.RUPEE_ICON + data.plan!!.amount
            }
        }

        txtCancelSub.makeLinks(this, Pair("CANCEL", View.OnClickListener {
            performCancelSubscription()
        }))
    }

    fun performCancelSubscription() {
        showAlert("Are you sure want to cancel this monthly donation?","yes","no",object :
            DialogOptionsSelectedListener {
            override fun onSelect(isYes: Boolean) {
                viewModel.CancelSubscription()
            }
        },object : DialogOptionsSelectedListener {
            override fun onSelect(isYes: Boolean) {
            }
        })
    }

    override fun onLogoutSuccess() {

    }

    override fun onCancelSubscriptionSuccess(data: UserData?) {
        val data : UserData = PrefUtils.getUserData(this)
        data.subscription_id = ""
        data.plan = null

        PrefUtils.setUserData(this,data)
        closeScreen()
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    override fun onClick(v: View?) {
        when(v?.id) {



        }
    }
}