package org.lakulishdham.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_image_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.glideLoadWithCache
import org.lakulishdham.model.GalleryListData

class GalleryImageAdapter(var context : Context,var imageList : ArrayList<GalleryListData>) : RecyclerView.Adapter<GalleryImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
       return ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.gallery_image_item,parent,false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.setImage(imageList.get(position))
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setImage(data: GalleryListData) {
                itemView.shapeable_image.glideLoadWithCache(Uri.parse(data.name),context,R.drawable.image_placeholder_default)
        }
    }
}