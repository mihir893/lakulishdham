package org.lakulishdham.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.razorpay.PaymentData
import org.lakulishdham.R
import org.lakulishdham.activities.DonationStatusActivity
import org.lakulishdham.helper.fireIntentWithData
import org.lakulishdham.helper.showToast

class Utils {

    companion object {
        fun handlePaymentError(context: Activity, code: Int, response: String?, paymentData: PaymentData?) {

            var toRedirect = true

            paymentData?.let {
                val jObj = it.data.getJSONObject("error")
                jObj?.let {
                    if (jObj.getString("reason").equals("payment_cancelled")) {
                        toRedirect = false
                        context.showToast(context.getString(R.string.user_payment_cancelled))
                    }
                }
            }

            if (!toRedirect) {
                return
            }

            val intent = Intent(context, DonationStatusActivity::class.java)
            intent.putExtra(DonationStatusActivity.INTENT_TRANSACTION_STATUS,false)
            intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIPTION,false)
            intent.putExtra(DonationStatusActivity.INTENT_TRANS_ID,"")
            intent.putExtra(DonationStatusActivity.INTENT_SUBSCRIP_AMOUNT,"0")
            context.fireIntentWithData(intent,true)

        }


    }

}