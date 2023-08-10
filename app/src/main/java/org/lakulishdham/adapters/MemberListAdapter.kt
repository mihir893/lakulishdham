package org.lakulishdham.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_image_item.view.*
import kotlinx.android.synthetic.main.member_list_item.view.*
import kotlinx.android.synthetic.main.subscription_list_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.helper.glideLoadWithCache
import org.lakulishdham.helper.showRedError
import org.lakulishdham.model.GalleryListData
import org.lakulishdham.model.MemberListData
import org.lakulishdham.model.SubscriptionListData

class MemberListAdapter(var context: Context, var planList: ArrayList<MemberListData>, var listner: OnItemSelectedListener) : RecyclerView.Adapter<MemberListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.member_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return planList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(planList.get(position))
    }

    fun AddMember(memberListData: MemberListData) {
        planList.add(memberListData)
        planList.sortByDescending {
            it.id
        }

        notifyDataSetChanged()
    }

    fun UpdateMember(memberListData: MemberListData) {
       for ((index, value) in planList.withIndex()) {
            if (value.id == memberListData.id) {
                planList.set(index,memberListData)
                notifyItemChanged(index)
                break
            }
       }
    }

    fun DeleteMember(memberListData: MemberListData) {
        val i = planList.indexOf(memberListData)
        planList.removeAt(i)

        notifyItemRemoved(i)
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: MemberListData) {
            itemView.txtMemberName.text = data.name
            itemView.txtMemberRelation.text = data.relation?.toLowerCase()

            itemView.txtMemberEdit.setOnClickListener {
                listner.onMemberEditListener(adapterPosition, data)
            }
        }
    }

    interface OnItemSelectedListener {
        fun onMemberEditListener(position: Int, data: MemberListData)
    }
}