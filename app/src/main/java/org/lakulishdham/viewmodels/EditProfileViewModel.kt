package org.lakulishdham.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.adcreators.youtique.helper.PrefUtils
import com.google.gson.JsonObject
import org.lakulishdham.R
import org.lakulishdham.api.ErrorCallBack
import org.lakulishdham.api.repo.DashboardApiClient
import org.lakulishdham.api.repo.LoginApiClient
import org.lakulishdham.api.repo.SignupApiClient
import org.lakulishdham.helper.isValidPassword
import org.lakulishdham.helper.isValidPhone
import org.lakulishdham.model.*

class EditProfileViewModel(var context: Context, var callBack: EditProfileViewModelCallback) : ViewModel() {


    fun getCountries() {
        SignupApiClient(context).getCountries(object : SignupApiClient.onCountryListApiCallback{
            override fun onGetCountries(list: ArrayList<CountryListData>?) {
                callBack.onGetCountriesData(list)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })

    }

    fun fetchStates(countryName : String) {
        val jsonObject  = JsonObject()
        jsonObject.addProperty("country",countryName)

        SignupApiClient(context).fetchStates(jsonObject,object : SignupApiClient.onStateListApiCallback{
            override fun onGetStates(list: ArrayList<StateListData>?) {
                callBack.onGetStatesData(list)
            }
            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchCities(countryName : String,state : String) {
        val jsonObject  = JsonObject()
        jsonObject.addProperty("country",countryName)
        jsonObject.addProperty("state",state)

        SignupApiClient(context).fetchDistrict(jsonObject,object : SignupApiClient.onDistrictListApiCallback{
            override fun onGetCities(list: ArrayList<String>?) {
                callBack.onGetCityData(list)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })
    }


    fun UpdateDetails(request: SignupRequest) {

        SignupApiClient(context).UpdateUserDetails(request,object : SignupApiClient.onEditProfileApiCallback{
            override fun onUpdateProfileSuccess(data: UserData?) {
                    callBack.onUpdateProfileSuccess(data)
            }

            override fun onError(errorString: String?) {
                callBack.onError(errorString!!)
            }
            override fun onDynamicError(errString: String?) {
                TODO("Not yet implemented")
            }

        })

    }

    interface EditProfileViewModelCallback : ErrorCallBack {
        fun onGetCountriesData(list: ArrayList<CountryListData>?)
        fun onGetStatesData(list: ArrayList<StateListData>?)
        fun onGetCityData(list: ArrayList<String>?)
        fun onUpdateProfileSuccess(data : UserData?)
    }
}