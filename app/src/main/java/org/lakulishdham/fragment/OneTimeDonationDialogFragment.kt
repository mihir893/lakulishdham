package org.lakulishdham.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_member_dialog.*
import kotlinx.android.synthetic.main.fragment_add_member_dialog.imgClose
import kotlinx.android.synthetic.main.fragment_donation_option_dialog.*
import kotlinx.android.synthetic.main.fragment_onetime_donations_dialog.*
import org.lakulishdham.R
import org.lakulishdham.helper.Text
import org.lakulishdham.helper.showRedError

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMemberDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OneTimeDonationDialogFragment(var callback : OnProceedDonationDialogListeners) : BottomSheetDialogFragment(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onetime_donations_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgClose.setOnClickListener(this)
        btnDonate.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id) {
            R.id.imgClose -> {
                dismiss()
            }
            R.id.btnDonate -> {

                if (edAmount.Text().isNullOrEmpty()) {
                    activity?.showRedError("Please enter amount")
                }
                else if (edAmount.Text().toInt() <= 0) {
                    activity?.showRedError("You can not donate amount 0 or less than 0")
                }
                else {
                    dismiss()
                    callback.onProceedToCheckout(edAmount.Text())
                }
            }
        }

    }

    public interface OnProceedDonationDialogListeners {
        fun onProceedToCheckout(amount : String)
    }

}