package org.lakulishdham.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edMobileNumber
import kotlinx.android.synthetic.main.activity_sign_up.edPassword
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.factories.SignupViewModelFactory
import org.lakulishdham.fragment.CountryListDialogFragment
import org.lakulishdham.helper.*
import org.lakulishdham.model.CountryListData
import org.lakulishdham.model.SignupRequest
import org.lakulishdham.model.StateListData
import org.lakulishdham.model.UserData
import org.lakulishdham.viewmodels.SignUpViewModel


class SignUpActivity : BaseActivity(), SignUpViewModel.SignUpViewModelCallback,
    CountryListDialogFragment.onItemSelectedListener, View.OnClickListener {

    lateinit var viewModel: SignUpViewModel

    var request = SignupRequest()

    var countryList = arrayListOf<CountryListData>()
    var stateList = arrayListOf<StateListData>()
    var cityList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this, SignupViewModelFactory(this,this)).get(SignUpViewModel :: class.java)
        viewModel.getCountries()

        edCountry.setOnClickListener(this)
        edState.setOnClickListener(this)
        edDistrict.setOnClickListener(this)
        btnSignup.setOnClickListener(this)
        txtTermsConds.setOnClickListener(this)
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

    override fun onSignUpSuccess(data: UserData?) {
        val intent = Intent(this,VerificationActivity::class.java)
        intent.putExtra(LoginActivity.INTENT_MOBILE,data?.phone_number)
        intent.putExtra(LoginActivity.INTENT_FROM,AppConstants.FROM_REGISTER)
        fireIntentWithData(intent,false)
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    fun showCountryDialog() {
        val dialog = CountryListDialogFragment(this,
            countryList as ArrayList<Any>,AppConstants.TYPE_COUNTRY,this,"Countries","Search Country")
        dialog.show(supportFragmentManager,"COUNTRY_DIALOG")
    }

    fun showStateDialog() {
        val dialog = CountryListDialogFragment(this,
            stateList as ArrayList<Any>,AppConstants.TYPE_STATE,this,"States","Search State")
        dialog.show(supportFragmentManager,"STATE_DIALOG")
    }

    fun showCityDialog() {
        val dialog = CountryListDialogFragment(this,
            cityList as ArrayList<Any>,AppConstants.TYPE_DISCTRICT,this,"Districts","Search District")
        dialog.show(supportFragmentManager,"CITY_DIALOG")
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
            R.id.btnSignup -> {
                goForSignup()
            }
            R.id.txtTermsConds -> {
                openBrowser(AppConstants.TERMS_CONDITIONS)
            }

        }
    }

    private fun goForSignup() {

        if (edName.Text().isEmpty()) {
            edName.requestFocus()
            showRedError("Please Enter Full Name")
            return
        }

        if(!edMobileNumber.Text().isValidPhone()) {
            edMobileNumber.requestFocus()
            showRedError(resources.getString(R.string.valid_mobile_number))
            return
        }

        /*if (!edEmail.Text().isValidEmail()) {
            edEmail.requestFocus()
            showRedError("Please Enter Valid Email")
            returnn
        }*/

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

        if (!edPassword.Text().isValidPassword()) {
            edPassword.requestFocus()
            showRedError(resources.getString(R.string.valid_password))
            return
        }

        if (!edRePassword.Text().isValidPassword()) {
            edRePassword.requestFocus()
            showRedError(resources.getString(R.string.confirm_valid_password))
            return
        }

        if (!edPassword.Text().equals(edRePassword.Text())) {
            edRePassword.requestFocus()
            showRedError(resources.getString(R.string.password_not_match))
            return
        }

        /*if (edReferBy.Text().isEmpty()) {
            edReferBy.requestFocus()
            showRedError("Please Enter Referral's Name")
            return
        }

        if(!edReferMobile.Text().isValidPhone()) {
            edReferMobile.requestFocus()
            showRedError("Please Enter Referral's Mobile Number")
            return
        }*/

        if (!chkTermsConditions.isChecked) {
            showRedError("Please agree with Terms & Conditions")
            return
        }

        request.name = edName.Text()
        request.phone_number = edMobileNumber.Text()
        request.email = edEmail.Text()
        request.address = edAddress.Text()
        request.password = edPassword.Text()
        request.c_password = edRePassword.Text()
        request.refer_by = if(edReferBy.Text().isEmpty()) AppConstants.DEFAULT_REFERBY else edReferBy.Text()
        request.refer_mobile_no = if(edReferMobile.Text().isEmpty()) edMobileNumber.Text() else edReferMobile.Text()

        viewModel.SignUp(request)

    }
}