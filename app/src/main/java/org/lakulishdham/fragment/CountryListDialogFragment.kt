package org.lakulishdham.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_member_dialog.*
import kotlinx.android.synthetic.main.fragment_add_member_dialog.imgClose
import kotlinx.android.synthetic.main.fragment_country_list_dialog.*
import org.lakulishdham.R
import org.lakulishdham.adapters.ItemListAdapter
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.model.CountryListData
import org.lakulishdham.model.StateListData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMemberDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CountryListDialogFragment(var mContext : Context,
                                var list : ArrayList<Any>,
                                var type : Int,
                                var callback : onItemSelectedListener,var title : String,var searchHint : String) : BottomSheetDialogFragment(), View.OnClickListener,
    ItemListAdapter.onListItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edSearchCountry.hint = searchHint
        dialogHeader.text = title

        rvItems.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        rvItems.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))


        val listAdapter = ItemListAdapter(mContext,type,this)
        listAdapter.setList(list)
        listAdapter.setMainList(list)
        rvItems.adapter = listAdapter

        imgClose.setOnClickListener(this)
        edSearchCountry.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                listAdapter.filter.filter(s)
            }

        })
    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.imgClose -> {
                dismiss()
            }
        }

    }

    public interface onItemSelectedListener {
        fun onSelected(data : CountryListData)
        fun onSelected(data : StateListData)
        fun onSelected(data : String)

    }

    override fun onItemSelected(type: Int, data: Any) {
        when(type) {
            AppConstants.TYPE_COUNTRY -> {
                dismiss()
                callback.onSelected(data as CountryListData)
            }
            AppConstants.TYPE_STATE -> {
                dismiss()
                callback.onSelected(data as StateListData)
            }
            AppConstants.TYPE_DISCTRICT -> {
                dismiss()
                callback.onSelected(data as String)
            }
        }
    }

}