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
    private val carouselAdapterItemClickListener: CarouselAdapterItemClickListener
) : RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    private lateinit var preferences: SharedPreferencesManager

    interface CarouselAdapterItemClickListener {
        fun onAddToCartClickWithoutSignIn()
        fun onAddToCartClickWithSignIn(holder: CarouselItemViewHolder, position: Int, productId: Int, userId: Int)
        fun carouselHomeToCartFragment()
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
                addToCart(holder, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    private fun addToCart(holder: CarouselItemViewHolder, position: Int) {
        preferences = SharedPreferencesManager.getInstance(holder.itemView.context, "shared_pref")
        val productId = carouselDataList[position][4].toInt()

        CoroutineScope(Dispatchers.IO).launch {
            if (preferences.getLoggedInUser() != null) {
                val userEmail = preferences.getLoggedInUser()!!
                val response = RetrofitInstance.authService.getUserByEmail(userEmail).execute()
                if (response.body() != null) {
                    val userId = response.body()!!
                    carouselAdapterItemClickListener.onAddToCartClickWithSignIn(holder, position, productId, userId)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(holder.itemView.context, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    carouselAdapterItemClickListener.onAddToCartClickWithoutSignIn()
                }
            }
        }
    }
}


//private fun addToCart(holder: CarouselRVAdapter.CarouselItemViewHolder, position: Int) {
//    preferences = SharedPreferencesManager.getInstance(holder.itemView.context, "shared_pref")
//    val productId = carouselDataList[position][4].toInt()
//
//    CoroutineScope(Dispatchers.IO).launch {
//        if (preferences.getLoggedInUser() != null) {
//            val userEmail = preferences.getLoggedInUser()!!
//            val response = RetrofitInstance.authService.getUserByEmail(userEmail).execute()
//            if (response.body() != null) {
//                val userId = response.body()!!
//                if (!checkProductInCart(productId, userId)) {
//                    val cartItem = CartDetails(userId, productId, 1)
//                    Log.i("Retrofit", cartItem.toString())
//
//                    val addToCartResponse = RetrofitInstance.authService.addToCart(cartItem).execute()
//                    if (addToCartResponse.isSuccessful && addToCartResponse.body() != null && addToCartResponse.body()!!.success) {
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(
//                                holder.itemView.context,
//                                "Added to cart",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(
//                                holder.itemView.context,
//                                "Failed to add product to cart",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            Log.i("Retrofit", addToCartResponse.body().toString())
//                        }
//                    }
//                } else {
//                    withContext(Dispatchers.Main) {
//                        holder.addToCartButton.text = "Go to cart"
//                        holder.addToCartButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blueTheme))
//                        holder.addToCartButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
//                    }
//                }
//            } else {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(holder.itemView.context, "User not found", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } else {
//            withContext(Dispatchers.Main) {
//                carouselAdapterItemClickListener.onAddToCartClickWithoutSignIn()
//            }
//        }
//    }
//}


//    private suspend fun checkProductInCart(productId: Int, userId: Int): Boolean {
//        return withContext(Dispatchers.IO) {
//            var productInCart = false
//            val cartResponse = RetrofitInstance.authService.getCart(userId).execute()
//            val cartList = cartResponse.body()
//            if (cartList != null && cartList.size > 0) {
//                for (item in cartList) {
//                    Log.i("Retrofit", item.toString())
//                    if (item.product.id == productId) {
//                        Log.i("Retrofit", "Product id  ${item.product.id} already in cart")
//                        productInCart = true
//                        break
//                    }
//                }
//            }
//            productInCart
//        }
//    }