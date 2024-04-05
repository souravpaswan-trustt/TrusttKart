package com.example.trusttkart.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trusttkart.R

class CarouselRVAdapter(private val carouselDataList: ArrayList<ArrayList<String>>) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.product_carousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val carouselProductNameTextView = holder.itemView.findViewById<TextView>(R.id.carouselProductNameTextView)
        val carouselProductCategoryTextView = holder.itemView.findViewById<TextView>(R.id.carouselProductCategoryTextView)
        val carouselProductPriceTextView = holder.itemView.findViewById<TextView>(R.id.carouselProductPriceTextView)
        val carouselProductImageView = holder.itemView.findViewById<ImageView>(R.id.carouselProductImageView)
        carouselProductNameTextView.text = carouselDataList[position][0]
        carouselProductCategoryTextView.text = carouselDataList[position][1]
        carouselProductPriceTextView.text = carouselDataList[position][2]
        Glide.with(holder.itemView.context)
            .load(carouselDataList[position][3])
            .into(carouselProductImageView)
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }
}