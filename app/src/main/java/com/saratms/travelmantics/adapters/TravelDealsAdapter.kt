package com.saratms.travelmantics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saratms.travelmantics.databinding.TravelDealListItemBinding
import com.saratms.travelmantics.models.TravelDeal
import com.squareup.picasso.Picasso

/**
 * Created by Sarah Al-Shamy on 04/08/2019.
 */
class TravelDealsAdapter(var clickListener: TravelItemClickListener) : RecyclerView.Adapter<TravelDealsAdapter.TravelDealsViewHolder>() {

    var travelDealList = listOf<TravelDeal>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelDealsViewHolder {
        var binding = TravelDealListItemBinding.inflate(LayoutInflater.from(parent.context))
        return TravelDealsViewHolder(binding)
    }

    override fun getItemCount(): Int = travelDealList.size

    override fun onBindViewHolder(holder: TravelDealsViewHolder, position: Int) {
        var travelDeal = travelDealList.get(position)
        holder.bind(travelDeal, clickListener)
    }

    class TravelDealsViewHolder(var binding: TravelDealListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(travelDeal: TravelDeal, clickListener: TravelItemClickListener) {
            binding.travelDeal = travelDeal
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    class TravelItemClickListener(var clickHandle: (travelDeal: TravelDeal) -> Unit){
        fun onClick(travelDeal: TravelDeal?){
            travelDeal?.let { clickHandle(travelDeal) }
        }
    }
}