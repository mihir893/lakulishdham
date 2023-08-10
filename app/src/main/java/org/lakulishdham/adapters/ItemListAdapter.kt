package org.lakulishdham.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_list_item.view.*
import org.lakulishdham.R
import org.lakulishdham.helper.AppConstants
import org.lakulishdham.model.CountryListData
import org.lakulishdham.model.StateListData

class ItemListAdapter(var context : Context, var type : Int,var listener : onListItemSelectedListener) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>(),Filterable {

    val itemList = arrayListOf<Any>()
    val itemMainList = arrayListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       return ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        when(type) {

            AppConstants.TYPE_COUNTRY -> {
                holder.bind(itemList.get(position) as CountryListData)
            }
            AppConstants.TYPE_STATE -> {
                holder.bind(itemList.get(position) as StateListData)
            }
            AppConstants.TYPE_DISCTRICT -> {
                holder.bind(itemList.get(position) as String)
            }

        }


    }

    fun setMainList(list : ArrayList<Any>) {
        itemMainList.clear()
        itemMainList.addAll(list)
    }

    fun setList(list : ArrayList<Any>) {
        itemList.clear()
        itemList.addAll(list)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(data: CountryListData) {
            itemView.txtItemName.text = data.country_name

            itemView.txtItemName.setOnClickListener {
                listener.onItemSelected(type,data)
            }
        }

        fun bind(data: StateListData) {
            itemView.txtItemName.text = data.name

            itemView.txtItemName.setOnClickListener {
                listener.onItemSelected(type,data)
            }
        }

        fun bind(data: String) {
            itemView.txtItemName.text = data

            itemView.txtItemName.setOnClickListener {
                listener.onItemSelected(type,data)
            }
        }
    }

    interface onListItemSelectedListener {
        fun onItemSelected(type : Int,data : Any)
    }

    override fun getFilter(): Filter {
        return ItemFilters()
    }


    inner class ItemFilters : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()

            if (constraint.isNullOrEmpty()) {
                //No need for filter
                results.values = itemMainList
                results.count = itemMainList.size
            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list

                when(type) {
                    AppConstants.TYPE_COUNTRY -> {
                        val fRecords = ArrayList<CountryListData>()
                        for (s in itemMainList as ArrayList<CountryListData>) {
                            if (s.country_name?.trim()?.toLowerCase()?.contains(constraint.toString().trim().toLowerCase())!!) {
                                fRecords.add(s)
                            }
                        }
                        results.values = fRecords
                        results.count = fRecords.size
                    }
                    AppConstants.TYPE_STATE -> {

                        val fRecords = ArrayList<StateListData>()
                        for (s in itemMainList as ArrayList<StateListData>) {
                            if (s.name?.trim()?.toLowerCase()?.contains(constraint.toString().trim().toLowerCase())!!) {
                                fRecords.add(s)
                            }
                        }
                        results.values = fRecords
                        results.count = fRecords.size

                    }
                    AppConstants.TYPE_DISCTRICT -> {

                        val fRecords = ArrayList<String>()
                        for (s in itemMainList as ArrayList<String>) {
                            if (s.trim()?.toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                                fRecords.add(s)
                            }
                        }
                        results.values = fRecords
                        results.count = fRecords.size
                    }
                }


            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            setList(results?.values as ArrayList<Any>)
            notifyDataSetChanged()
        }

    }
}