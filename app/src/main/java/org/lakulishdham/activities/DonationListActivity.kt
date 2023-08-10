package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_donation_list.*
import kotlinx.android.synthetic.main.activity_subscription_plan_list.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.adapters.DonationListAdapter
import org.lakulishdham.adapters.SubscriptionListAdapter
import org.lakulishdham.factories.DonationViewModelFactory
import org.lakulishdham.factories.SubscriptionViewModelFactory
import org.lakulishdham.helper.closeScreen
import org.lakulishdham.model.DonationListData
import org.lakulishdham.model.SubscriptionListData
import org.lakulishdham.viewmodels.DonationViewModel
import org.lakulishdham.viewmodels.SuubscriptionViewModel

class DonationListActivity : BaseActivity(), DonationViewModel.DonationViewModelCallback {

    lateinit var viewModel: DonationViewModel
    lateinit var adapter : DonationListAdapter

    val donationList = arrayListOf<DonationListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        init()
    }

    fun init() {

        viewModel = ViewModelProvider(this, DonationViewModelFactory(this,this)).get(DonationViewModel :: class.java)

        rvDonations.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvDonations.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = DonationListAdapter(this,donationList)
        rvDonations.adapter = adapter

        viewModel.getDonations()
    }

    fun goBack(view: View) {
        closeScreen()
    }

    override fun onGetData(list: ArrayList<DonationListData>?) {
        if (!list.isNullOrEmpty()) {
            donationList.clear()
            donationList.addAll(list)

            adapter.notifyDataSetChanged()
        }
    }

    override fun onError(err: String) {
    }
}