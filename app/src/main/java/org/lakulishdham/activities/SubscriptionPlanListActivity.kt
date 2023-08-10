package org.lakulishdham.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_subscription_plan_list.*
import org.json.JSONObject
import org.lakulishdham.R
import org.lakulishdham.adapters.SubscriptionListAdapter
import org.lakulishdham.factories.DashboardViewModelFactory
import org.lakulishdham.factories.SubscriptionViewModelFactory
import org.lakulishdham.helper.*
import org.lakulishdham.model.*
import org.lakulishdham.utility.DateFormatterUtils
import org.lakulishdham.utility.SpaceItemDecoration
import org.lakulishdham.viewmodels.DashboardViewModel
import org.lakulishdham.viewmodels.SuubscriptionViewModel
import java.util.*
import kotlin.collections.ArrayList

class SubscriptionPlanListActivity : AppCompatActivity(),
    SuubscriptionViewModel.SubscriptionViewModelCallback,
    SubscriptionListAdapter.OnItemSelectedListener, PaymentResultWithDataListener {

    lateinit var viewModel: SuubscriptionViewModel
    lateinit var adapter : SubscriptionListAdapter

    val planList = arrayListOf<SubscriptionListData>()

    lateinit var userData : UserData

    lateinit var checkout : Checkout

    lateinit var subscriptionData : SubscriptionListData
    lateinit var subscriptionId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription_plan_list)

        Checkout.clearUserData(this)

        Checkout.preload(applicationContext)

        init()
    }

    fun init() {

        userData = PrefUtils.getUserData(this)

        viewModel = ViewModelProvider(this, SubscriptionViewModelFactory(this,this)).get(SuubscriptionViewModel :: class.java)

        rvPlans.layoutManager = GridLayoutManager(this,2)
        rvPlans.addItemDecoration(SpaceItemDecoration(60))

        adapter = SubscriptionListAdapter(this,planList,this)
        rvPlans.adapter = adapter

        viewModel.getSubscriptions()
    }

    override fun onGetData(list: ArrayList<SubscriptionListData>?) {
        if (!list.isNullOrEmpty()) {
            planList.clear()
            planList.addAll(list)

            planList.sortBy {
             it.amount?.toDouble()
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onSubscriptionGenerateSuccessful(data : GenerateSubscriptionData?) {
        if (data != null) {
            subscriptionId = data.id
            startPayment(data)
        }
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    override fun onSubscriptionSelectedListener(data: SubscriptionListData) {
        subscriptionData = data
        data.amount?.let { viewModel.generateSubscriptions(data.id, it) }
    }

    fun startPayment(data: GenerateSubscriptionData?) {

        checkout = Checkout()
        checkout.setKeyID(AppConstants.getRazorPayKey())
        checkout.setImage(R.mipmap.ic_launcher)
        try {
            val options = JSONObject()
            options.put("name", resources.getString(R.string.app_name))
            options.put("description", "Donation to Lakulish Dham")
            options.put("theme.color", "#F98404")
            options.put("prefill.name", userData.name)
            options.put("prefill.email", if(userData.email.isNullOrEmpty()) AppConstants.DEFAULT_EMAIL else userData.email)
            options.put("prefill.contact", userData.phone_number)
            options.put("subscription_id",data?.id)
            options.put("payment_capture", "1")
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            checkout.open(this, options)
        } catch (e: Exception) {
            AppLogger.e("Error in starting Razorpay Checkout")
        }

    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        val intent = Intent(this,DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS,false)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION,false)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID,"")
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT,"0")
        fireIntentWithData(intent,true)
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?, paymentData: PaymentData?) {
        AppLogger.e("PAYMENT_SUCCESS : ${razorpayPaymentID}")
        AppLogger.e("PAYMENT_DATA : ${Gson().toJson(paymentData)}")

        addDonation(paymentData)
    }

    fun goBack(view: View) {
        closeScreen()
    }

    fun addDonation(paymentData: PaymentData?) {

        paymentData?.let {
            val addDonationRequest = AddDonationRequest()
            addDonationRequest.plan_id = subscriptionData.id.toString()
            addDonationRequest.subscription_id = subscriptionId
            addDonationRequest.amount = subscriptionData.amount
            addDonationRequest.transaction_id = paymentData.paymentId
            addDonationRequest.razorpay_paymentId = paymentData.paymentId
            addDonationRequest.pay_status = "1"
            addDonationRequest.razorpay_orderId = paymentData.orderId
            addDonationRequest.razorpay_signature = paymentData.signature
            addDonationRequest.transaction_date = DateFormatterUtils.parseDate(
                Date(),
                DateFormatterUtils.ymdFormat)

            viewModel.AddDonation(addDonationRequest)
        }
    }

    override fun onDonationSuccessful(data: DonationListData?) {

        if (data != null) {
            if (data.plan != null) {
                val userData = PrefUtils.getUserData(this)
                userData.subscription_id = subscriptionId
                userData.plan = data.plan

                PrefUtils.setUserData(this,userData)
            }
        }

        val intent = Intent(this,DonationStatusActivity::class.java)
        intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS,true)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION,true)
        intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID,data?.transaction_id)
        intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT,data?.amount)
        fireIntentWithData(intent,true)
    }

    override fun onAlreadyDonation(msg: String) {
        showAlert(msg,"OK",object : DialogOptionsSelectedListener{
            override fun onSelect(isYes: Boolean) {
            }
        })
    }

}