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

class DonationViewModel(var context: Context, var callBack: DonationViewModelCallback) : ViewModel() {


    fun getDonations() {
        DonationApiClient(context).getDonationList(object : DonationApiClient.onDonationListApiCallback{
            override fun onGetData(list: ArrayList<DonationListData>?) {
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



    interface DonationViewModelCallback : ErrorCallBack {
        fun onGetData(list: ArrayList<DonationListData>?)

    }
}