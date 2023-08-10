package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.DonationApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.*

class DashboardViewModel(var context: Context, var callBack: DashboardViewModelCallback) : ViewModel() {


    fun fetchGalleryImages() {
        DashboardApiClient(context).fetchGalleryImages(object : DashboardApiClient.onGalleryListApiCallback{
            override fun onGetData(data: GalleryData?) {
                callBack.onGetData(data)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }

            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })
    }

    fun generateOrder(amount : String) {

        if (amount.isNullOrEmpty()) {
            callBack.onError("Please enter amount")
            return
        }

        if (amount.toInt() <= 0) {
            callBack.onError("You can not donate amount : 0")
            return
        }

        val request = GenerateOrderRequest()
        request.amount = amount

        DashboardApiClient(context).generateOrder(request,object : DashboardApiClient.onGenerateOrderApiCallback{
            override fun onGetData(data: OrderData?) {
                callBack.onGetOrderData(data)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
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
                TODO("Not yet implemented")
            }
        })

    }

    interface DashboardViewModelCallback : ErrorCallBack {
        fun onGetData(data: GalleryData?)
        fun onGetOrderData(data: OrderData?)
        fun onDonationSuccessful(data : DonationListData?)
    }
}