package org.lakulishdham.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_add_member_dialog.*
import kotlinx.android.synthetic.main.fragment_add_member_dialog.header
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.Text
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.MemberListData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMemberDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMemberDialogFragment(var callback: onNewMemberDialogListeners, var type: Int) : BottomSheetDialogFragment(), View.OnClickListener {

    var memberListData: MemberListData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_member_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgClose.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        btnDelete.setOnClickListener(this)

        when (type) {
            AppConstants.TYPE_ADD -> {
                btnDelete.visibility = View.GONE
                header.setText("New Member")
            }

            AppConstants.TYPE_EDIT -> {

                btnDelete.visibility = View.VISIBLE
                header.setText("Member Details")
                btnAdd.setText("EDIT MEMBER")

                if (memberListData != null) {
                    edMemberName.setText(memberListData!!.name)
                    edMemberRelation.setText(memberListData!!.relation)
                }
            }
        }

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.imgClose -> {
                dismiss()
            }
            R.id.btnAdd -> {
                performEdit(edMemberName.Text(), edMemberRelation.Text())
            }
            R.id.btnDelete -> {
                dismiss()
                memberListData?.let { callback.onMemberDelete(it) }
            }


        }

    }

    fun setMemberData(data: MemberListData) {
        memberListData = data
    }

    public interface onNewMemberDialogListeners {
        fun onMemberEdit(memberListData: MemberListData)
        fun onMemberAdd(name: String, relation: String)
        fun onMemberDelete(memberListData: MemberListData)
    }

    fun performEdit(name: String, relation: String) {

        if (name.isEmpty()) {
            context?.showRedError("Please Enter Member's Name")
            return
        }

        if (relation.isEmpty()) {
            context?.showRedError("Please Enter Relation with Member")
            return
        }


        when (type) {
            AppConstants.TYPE_ADD -> {
                dismiss()
                callback.onMemberAdd(name, relation)
            }

            AppConstants.TYPE_EDIT -> {
                if (memberListData != null) {
                    memberListData!!.name = name
                    memberListData!!.relation = relation

                    dismiss()
                    callback.onMemberEdit(memberListData!!)
                }
            }
        }

    }

}