package com.example.trusttkart.recyclerview

import SharedPreferencesManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trusttkart.MainActivity
import com.example.trusttkart.R
import com.example.trusttkart.retrofit.AddToCartResponse
import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarouselRVAdapter(
    private val carouselDataList: ArrayList<ArrayList<String>>,
    private val carouselAdapterItemClickListener: CarouselAdapterItemClickListener):
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    interface CarouselAdapterItemClickListener {
        fun carouselHomeToCartFragment()
        fun onAddToCartClickListener(holder: CarouselItemViewHolder, productId: Int)
    }

    inner class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carouselProductNameTextView: TextView = view.findViewById(R.id.carouselProductNameTextView)
        val carouselProductCategoryTextView: TextView = view.findViewById(R.id.carouselProductCategoryTextView)
        val carouselProductPriceTextView: TextView = view.findViewById(R.id.carouselProductPriceTextView)
        val carouselProductImageView: ImageView = view.findViewById(R.id.carouselProductImageView)
        val addToCartButton: Button = view.findViewById(R.id.addToCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.product_carousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        holder.carouselProductNameTextView.text = carouselDataList[position][0]
        holder.carouselProductCategoryTextView.text = carouselDataList[position][1]
        holder.carouselProductPriceTextView.text = carouselDataList[position][2]
        Glide.with(holder.itemView.context)
            .load(carouselDataList[position][3])
            .into(holder.carouselProductImageView)

        holder.addToCartButton.setOnClickListener {
            if (holder.addToCartButton.text == "Go to cart") {
                carouselAdapterItemClickListener.carouselHomeToCartFragment()
            } else {
                val productId = carouselDataList[position][4].toInt()
                carouselAdapterItemClickListener.onAddToCartClickListener(holder, productId)
            }
        }
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }
}