package org.lakulishdham.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_image_item.view.*
import kotlinx.android.synthetic.main.subscription_list_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.glideLoadWithCache
import org.lakulishdham.model.GalleryListData
import org.lakulishdham.model.SubscriptionListData
import kotlin.math.roundToInt

class SubscriptionListAdapter(var context : Context, var planList : ArrayList<SubscriptionListData>,var listener : OnItemSelectedListener) : RecyclerView.Adapter<SubscriptionListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.subscription_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(planList.get(position))
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: SubscriptionListData) {



            itemView.txtSubscriptionAmount.text = AppConstants.RUPEE_ICON + data.amount?.toDouble()?.roundToInt().toString()

            itemView.setOnClickListener {
                listener.onSubscriptionSelectedListener(data)
            }
        }
    }

    interface OnItemSelectedListener {
        fun onSubscriptionSelectedListener(data : SubscriptionListData)
    }
}