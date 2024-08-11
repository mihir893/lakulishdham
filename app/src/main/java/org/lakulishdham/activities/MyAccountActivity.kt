package org.lakulishdham.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_my_account.*
import org.json.JSONObject
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.DashboardViewModelFactory
import org.lakulishdham.factories.MyAccountViewModelFactory
import org.lakulishdham.fragment.OneTimeDonationDialogFragment
import org.lakulishdham.helper.*
import org.lakulishdham.model.AddDonationRequest
import org.lakulishdham.model.DonationListData
import org.lakulishdham.model.OrderData
import org.lakulishdham.model.UserData
import org.lakulishdham.utility.DateFormatterUtils
import org.lakulishdham.viewmodels.DashboardViewModel
import org.lakulishdham.viewmodels.MyAccountViewModel
import java.util.Date

class MyAccountActivity : BaseActivity(), View.OnClickListener,
    MyAccountViewModel.AccountViewModelCallback,
    OneTimeDonationDialogFragment.OnProceedDonationDialogListeners, PaymentResultWithDataListener {

    lateinit var viewModel: MyAccountViewModel

    var amount: String = ""

    lateinit var checkout: Checkout

    lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        Checkout.clearUserData(this)

        Checkout.preload(applicationContext)

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

        txtOneTimeDonate.setOnClickListener(this)
        txtMembers.setOnClickListener(this)
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
            R.id.txtOneTimeDonate -> {
                val dialog = OneTimeDonationDialogFragment(this)
                dialog.show(supportFragmentManager, "SINGLE_DONATION")
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




    private fun setUserInfo() {
        userData = PrefUtils.getUserData(this)
        userData.let {
            txtUsername.text = userData.name
            txtUserMobile.text = userData.phone_number
            txtUserNameInitials.text = userData.name?.GetInitials()

            if(userData.plan != null) {
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

    override fun onGetOrderData(data: OrderData?) {
        data?.let {
            startPayment(it)
        }
    }



    override fun onError(err: String) {
        showRedError(err)
    }

    override fun onProceedToCheckout(a: String) {
        amount = a
        viewModel.generateOrder(a)
    }


    fun startPayment(data: OrderData?) {

        checkout = Checkout()
        checkout.setKeyID(AppConstants.getRazorPayKey())
        checkout.setImage(R.mipmap.ic_launcher)
        try {
            val options = JSONObject()
            options.put("name", resources.getString(R.string.app_name))
            options.put("description", "Donation to Lakulish Dham")
            options.put("order_id", data?.id) //from response of step 3.
            options.put("theme.color", "#F98404")
            options.put("currency", data?.currency)
            options.put("amount", data?.amount) //pass amount in currency subunits
            options.put("prefill.name", userData.name)
            options.put(
                "prefill.email",
                if (userData.email.isNullOrEmpty()) AppConstants.DEFAULT_EMAIL else userData.email
            )
            options.put("prefill.contact", userData.phone_number)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)

            options.put("retry", retryObj)
            checkout.open(this, options)
        } catch (e: Exception) {
            AppLogger.e("Error in starting Razorpay Checkout")
        }

    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        AppLogger.e("PAYMENT_SUCCESS : ${razorpayPaymentID}")
        AppLogger.e("PAYMENT_DATA : ${Gson().toJson(paymentData)}")
//        addDonation(paymentData)
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        AppLogger.e("PAYMENT_FAIL_DATA : ${Gson().toJson(paymentData)}")

        val intent = Intent(this, DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS, false)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION, false)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID, "")
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT, "0")
        fireIntentWithData(intent, true)
    }

    fun addDonation(paymentData: PaymentData?) {

        paymentData?.let {
            val addDonationRequest = AddDonationRequest()
            addDonationRequest.amount = amount
            addDonationRequest.transaction_id = paymentData.paymentId
            addDonationRequest.razorpay_paymentId = paymentData.paymentId
            addDonationRequest.pay_status = "1"
            addDonationRequest.razorpay_orderId = paymentData.orderId
            addDonationRequest.razorpay_signature = paymentData.signature
            addDonationRequest.transaction_date =
                DateFormatterUtils.parseDate(Date(), DateFormatterUtils.ymdFormat)

            viewModel.AddDonation(addDonationRequest)
        }
    }

    override fun onDonationSuccessful(data: DonationListData?) {
        val intent = Intent(this, DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS, true)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION, false)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID, data?.transaction_id)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT, "0")
        fireIntentWithData(intent, true)
    }


}