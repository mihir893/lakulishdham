package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.adcreators.youtique.helper.PrefUtils
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.edCountry
import kotlinx.android.synthetic.main.activity_edit_profile.edDistrict
import kotlinx.android.synthetic.main.activity_edit_profile.edName
import kotlinx.android.synthetic.main.activity_edit_profile.edState
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.EditProfileViewModelFactory
import org.lakulishdham.fragment.CountryListDialogFragment
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.Text
import org.lakulishdham.helper.closeScreen
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.CountryListData
import org.lakulishdham.model.SignupRequest
import org.lakulishdham.model.StateListData
import org.lakulishdham.model.UserData
import org.lakulishdham.viewmodels.EditProfileViewModel

class EditProfileActivity : BaseActivity(),
    EditProfileViewModel.EditProfileViewModelCallback, View.OnClickListener,
    CountryListDialogFragment.onItemSelectedListener {

    lateinit var viewModel: EditProfileViewModel

    var request = SignupRequest()

    var countryList = arrayListOf<CountryListData>()
    var stateList = arrayListOf<StateListData>()
    var cityList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        init()
    }


    fun init() {

        viewModel = ViewModelProvider(this, EditProfileViewModelFactory(this,this)).get(EditProfileViewModel :: class.java)

        setUserInfo()

        edCountry.setOnClickListener(this)
        edState.setOnClickListener(this)
        edDistrict.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    fun setUserInfo() {

        val userData = PrefUtils.getUserData(this)

        edName.setText(userData.name)
        edCountry.setText(userData.country_name)
        edState.setText(userData.state)
        edDistrict.setText(userData.district)
        edAddress.setText(userData.address)

        request.name = userData.name
        request.country_id = userData.country_id?.toInt()
        request.state = userData.state
        request.district = userData.district
        request.address = userData.address

        viewModel.getCountries()
        userData.country_name?.let { viewModel.fetchStates(it) }
        userData.country_name?.let { userData.state?.let { it1 -> viewModel.fetchCities(it, it1) } }
    }

    override fun onGetCountriesData(list: ArrayList<CountryListData>?) {
        if (!list.isNullOrEmpty()) {
            countryList.clear()
            countryList.addAll(list)
        }
    }

    override fun onGetStatesData(list: ArrayList<StateListData>?) {
        if (!list.isNullOrEmpty()) {
            stateList.clear()
            stateList.addAll(list)
        }
    }

    override fun onGetCityData(list: ArrayList<String>?) {
        if (!list.isNullOrEmpty()) {
            cityList.clear()
            cityList.addAll(list)
        }
    }

    override fun onUpdateProfileSuccess(data: UserData?) {
        if (data != null) {
            PrefUtils.setUserData(this,data)
            closeScreen()
        }
    }

    override fun onError(err: String) {
    }

    fun showCountryDialog() {
        val dialog = CountryListDialogFragment(this,
            countryList as ArrayList<Any>, AppConstants.TYPE_COUNTRY,this,"Countries","Search Country")
        dialog.show(supportFragmentManager,"COUNTRY_DIALOG")
    }

    fun showStateDialog() {
        val dialog = CountryListDialogFragment(this,
            stateList as ArrayList<Any>, AppConstants.TYPE_STATE,this,"States","Search State")
        dialog.show(supportFragmentManager,"STATE_DIALOG")
    }

    fun showCityDialog() {
        val dialog = CountryListDialogFragment(this,
            cityList as ArrayList<Any>, AppConstants.TYPE_DISCTRICT,this,"Districts","Search District")
        dialog.show(supportFragmentManager,"CITY_DIALOG")
    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.edCountry -> {
                showCountryDialog()
            }
            R.id.edState -> {
                showStateDialog()
            }
            R.id.edDistrict -> {
                showCityDialog()
            }
            R.id.btnSave -> {
                saveChanges()
            }

        }
    }

    private fun saveChanges() {

        if (edName.Text().isEmpty()) {
            edName.requestFocus()
            showRedError("Please Enter Full Name")
            return
        }

        if (edCountry.Text().isEmpty()) {
            edCountry.requestFocus()
            showRedError("Please Select Country")
            return
        }

        if (edState.Text().isEmpty()) {
            edState.requestFocus()
            showRedError("Please Select State")
            return
        }

        if (edDistrict.Text().isEmpty()) {
            edDistrict.requestFocus()
            showRedError("Please Select District")
            return
        }

        request.name = edName.Text()
        request.address = edAddress.Text()

        viewModel.UpdateDetails(request)

    }

    override fun onSelected(data: CountryListData) {
        edCountry.setText(data.country_name)
        request.country_id = data.id

        edState.setText("")
        edDistrict.setText("")
        request.state = null
        request.district = null

        viewModel.fetchStates(data.country_name!!)
    }

    override fun onSelected(data: StateListData) {
        edState.setText(data.name)
        request.state = data.name

        edDistrict.setText("")
        request.district = null

        viewModel.fetchCities(edCountry.Text(),data.name!!)
    }

    override fun onSelected(data: String) {
        edDistrict.setText(data)
        request.district = data
    }

    fun goBack(view: View) {
        closeScreen()
    }
}