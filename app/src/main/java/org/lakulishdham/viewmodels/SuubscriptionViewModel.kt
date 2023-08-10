package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.DonationApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.api.repo.SubscriptionApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.*

class SuubscriptionViewModel(var context: Context, var callBack: SubscriptionViewModelCallback) : ViewModel() {


    fun getSubscriptions() {
        SubscriptionApiClient(context).getSubscriptionList(object : SubscriptionApiClient.onSubscriptionListApiCallback{
            override fun onGetData(list: ArrayList<SubscriptionListData>?) {
                callBack.onGetData(list)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }
        })

    }

    fun generateSubscriptions(planId : Int,amount: String) {

         val request = GenerateOrderRequest()
        request.plan_id = planId

        SubscriptionApiClient(context).generateSubscription(request,object : SubscriptionApiClient.onGenerateSubscriptionListApiCallback{
            override fun onGetData(data: GenerateSubscriptionData?) {
                callBack.onSubscriptionGenerateSuccessful(data)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                callBack.onAlreadyDonation(errString!!)
            }
        })

    }

    fun AddDonation(request: AddDonationRequest) {

        DonationApiClient(context).AddDonation(request,object : DonationApiClient.onAddDonationApiCallback{
            override fun onDonationSuccess(data: DonationListData?) {
                callBack.onDonationSuccessful(data)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {

            }
        })

    }

    interface SubscriptionViewModelCallback : ErrorCallBack {
        fun onGetData(list: ArrayList<SubscriptionListData>?)
        fun onSubscriptionGenerateSuccessful(data : GenerateSubscriptionData?)
        fun onDonationSuccessful(data : DonationListData?)
        fun onAlreadyDonation(msg : String)
    }
}