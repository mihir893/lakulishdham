package org.lakulishdham.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_image_item.view.*
import kotlinx.android.synthetic.main.text_slider_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.glideLoadWithCache
import org.lakulishdham.model.GalleryListData

class TextSliderAdapter(var context : Context, var textList : ArrayList<String>) : RecyclerView.Adapter<TextSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.text_slider_item,parent,false))
    }

    override fun getItemCount(): Int {
        return textList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setText(textList.get(position))
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setText(data: String) {
                itemView.txtSlogan.setText(data)
        }
    }
}