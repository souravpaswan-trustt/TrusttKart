package com.example.trusttkart.recyclerview

import SharedPreferencesManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trusttkart.R
import com.example.trusttkart.retrofit.AddToCartResponse
import com.example.trusttkart.data.CartDetails
import com.example.trusttkart.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarouselRVAdapter(private val carouselDataList: ArrayList<ArrayList<String>>) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

        private lateinit var preferences: SharedPreferencesManager

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

        val addToCartButton = holder.itemView.findViewById<Button>(R.id.addToCartButton)
        addToCartButton.setOnClickListener {
            addToCart(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    fun addToCart(holder: CarouselItemViewHolder, position: Int){
        preferences = SharedPreferencesManager.getInstance(holder.itemView.context, "shared_pref")
        val productId = carouselDataList[position][4].toInt()
//        //assume user is logged in
//        val userEmail = preferences.getLoggedInUser()!!
//        val response = RetrofitInstance.authService.getUserByEmail(userEmail).execute()
//        val userId = response.body()!!
        val cartItem = CartDetails(10, productId, 1)

        RetrofitInstance.authService.addToCart(cartItem).enqueue(object :
            Callback<AddToCartResponse> {
            override fun onResponse(call: Call<AddToCartResponse>, response: Response<AddToCartResponse>) {
                val addToCartResponse = response.body()
                if (addToCartResponse != null && addToCartResponse.success) {
                    Toast.makeText(holder.itemView.context, "Added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                Toast.makeText(holder.itemView.context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}