package org.lakulishdham.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_members.*
import org.lakulishdham.BaseActivity
import org.lakulishdham.R
import org.lakulishdham.adapters.MemberListAdapter
import org.lakulishdham.factories.MemberViewModelFactory
import org.lakulishdham.fragment.AddMemberDialogFragment
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.closeScreen
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.MemberListData
import org.lakulishdham.model.SubscriptionListData
import org.lakulishdham.viewmodels.MemberViewModel

class MembersActivity : BaseActivity(), View.OnClickListener,
    AddMemberDialogFragment.onNewMemberDialogListeners, MemberViewModel.MemberViewModelCallback, MemberListAdapter.OnItemSelectedListener {

    lateinit var viewModel: MemberViewModel
    lateinit var adapter : MemberListAdapter

    val memberList = arrayListOf<MemberListData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)

        initListeners()
    }

    private fun initListeners() {

        viewModel = ViewModelProvider(this, MemberViewModelFactory(this,this)).get(MemberViewModel :: class.java)

        rvMembers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rvMembers.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter = MemberListAdapter(this,memberList,this)
        rvMembers.adapter = adapter

        fabAddMember.setOnClickListener(this)

        viewModel.getMembers()
    }

    override fun onClick(v: View?) {

        when(v?.id) {

            R.id.fabAddMember -> {

                val dialog = AddMemberDialogFragment(this,AppConstants.TYPE_ADD)
                dialog.show(supportFragmentManager,"ADD_MEMBER")

            }

        }

    }

    override fun onGetData(list: ArrayList<MemberListData>?) {
        if (!list.isNullOrEmpty()) {
            memberList.clear()
            memberList.addAll(list)

            adapter.notifyDataSetChanged()
        }
    }

    override fun onAddMemberSuccess(data: MemberListData?) {
        data?.let {
            adapter.AddMember(data)
        }
    }

    override fun onUpdateMemberSuccess(data: MemberListData?) {
        data?.let {
            adapter.UpdateMember(data)
        }
    }

    override fun onDeleteMemberSuccess(memberListData: MemberListData) {
        adapter.DeleteMember(memberListData)
    }

    override fun onError(err: String) {
        showRedError(err)
    }

    fun goBack(view: View) {
        closeScreen()
    }

    override fun onMemberEditListener(position: Int, data: MemberListData) {

        val dialog = AddMemberDialogFragment(this,AppConstants.TYPE_EDIT)
        dialog.setMemberData(data)
        dialog.show(supportFragmentManager,"ADD_MEMBER")

    }

    override fun onMemberEdit(memberListData: MemberListData) {
        val jsonObj = JsonObject()
        jsonObj.addProperty("member_id",memberListData.id)
        jsonObj.addProperty("name",memberListData.name)
        jsonObj.addProperty("relation",memberListData.relation)

        viewModel.UpdateMember(jsonObj)
    }

    override fun onMemberAdd(name: String, relation: String) {
        val jsonObj = JsonObject()
        jsonObj.addProperty("name",name)
        jsonObj.addProperty("relation",relation)

        viewModel.AddMember(jsonObj)
    }

    override fun onMemberDelete(memberListData: MemberListData) {
        val jsonObj = JsonObject()
        jsonObj.addProperty("member_id",memberListData.id)

        viewModel.DeleteMember(jsonObj,memberListData)
    }



}