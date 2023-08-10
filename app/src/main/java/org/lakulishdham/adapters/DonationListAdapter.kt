package org.lakulishdham.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.donation_list_item.view.*
import kotlinx.android.synthetic.main.gallery_image_item.view.*
import kotlinx.android.synthetic.main.member_list_item.view.*
import kotlinx.android.synthetic.main.subscription_list_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.glideLoadWithCache
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.DonationListData
import org.lakulishdham.model.GalleryListData
import org.lakulishdham.model.MemberListData
import org.lakulishdham.model.SubscriptionListData
import org.lakulishdham.utility.DateFormatterUtils

class DonationListAdapter(var context: Context, var donationList: ArrayList<DonationListData>) : RecyclerView.Adapter<DonationListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.donation_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return donationList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(donationList.get(position))
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: DonationListData) {
            itemView.txtDonationAmount.text = AppConstants.RUPEE_ICON + data.amount
            itemView.txtDonationDate.text = DateFormatterUtils.parseDate(data.transaction_date,DateFormatterUtils.ymdFormat,DateFormatterUtils.ddMMMMyyyy)
            itemView.txtTransactionId.text = "Transaction : #${data.transaction_id}"
        }
    }

    interface OnItemSelectedListener {

    }
}