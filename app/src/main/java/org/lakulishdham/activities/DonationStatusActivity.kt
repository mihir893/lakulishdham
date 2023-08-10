package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_donation_status.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.fireIntent

class DonationStatusActivity : BaseActivity(), View.OnClickListener {

    companion object {
        const val INTENT_TRANSACTION_STATUS = "transaction_status"
        const val INTENT_SUBSCRIPTION = "is_subscription"
        const val INTENT_TRANS_ID = "transaction_id"
        const val INTENT_SUBSCRIP_AMOUNT = "subscription_amount"
    }


    var isTransactionSuccess : Boolean = false
    var isSubscription : Boolean =  false
    var transactionId : String = ""
    var subscriptionAmount : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_status)

        init()
    }

    fun init() {

        isTransactionSuccess = intent.getBooleanExtra(INTENT_TRANSACTION_STATUS,false)
        isSubscription = intent.getBooleanExtra(INTENT_SUBSCRIPTION,false)
        transactionId = intent.getStringExtra(INTENT_TRANS_ID)!!
        subscriptionAmount = intent.getStringExtra(INTENT_SUBSCRIP_AMOUNT)!!

        if (isTransactionSuccess) {
            setSuccessPage()
        }
        else {
            setFailPage()
        }

        btnDonateAgain.setOnClickListener(this)

    }

    fun setSuccessPage() {

        imgIcon.setImageResource(R.drawable.ic_success)
        txtMessage.setText("DONATION SUCCESSFUL")
        txtMessage.setTextColor(ContextCompat.getColor(this,R.color.green))

        txtTransactionId.visibility = View.VISIBLE
        txtTransactionId.setText("Transaction Id : $transactionId")

        btnDonateAgain.visibility = View.VISIBLE
        btnDonateAgain.setText("GO TO HOME")

        if (isSubscription) {
            txtSubscription.visibility = View.VISIBLE
            txtSubscription.setText("Now, You are subscribed for auto-pay donation of ${AppConstants.RUPEE_ICON + subscriptionAmount} every month.")
        }
        else {
            txtSubscription.visibility = View.GONE
        }
    }

    fun setFailPage() {

        imgIcon.setImageResource(R.drawable.ic_fail)
        txtMessage.setText("DONATION FAILED")
        txtMessage.setTextColor(ContextCompat.getColor(this,R.color.light_red))
        txtTransactionId.visibility = View.GONE
        txtSubscription.visibility = View.GONE
        btnDonateAgain.visibility = View.VISIBLE

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnDonateAgain -> {
                fireIntent(DashboardActivity::class.java,true)
            }
        }
    }


}