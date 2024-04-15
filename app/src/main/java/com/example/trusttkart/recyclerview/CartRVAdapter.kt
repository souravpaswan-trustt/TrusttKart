package com.example.trusttkart.recyclerview

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
import com.example.trusttkart.retrofit.FetchCartResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartRVAdapter(private val cartItemOnClickListener: CartItemOnClickListener,
                    private val cartItems: List<FetchCartResponse>) : RecyclerView.Adapter<CartRVAdapter.CartViewHolder>() {

    interface CartItemOnClickListener{
        fun removeCartItemOnClick(productId: Int)
        fun updateCartItemOnClick(productId: Int, increase: Boolean)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.cartProductNameTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.cartProductPriceTextView)
        val productQuantityTextView: TextView = itemView.findViewById(R.id.cartProductQuantityTextView)
        val productImageView: ImageView = itemView.findViewById(R.id.cartProductImageView)
        val removeCartItemButton: Button = itemView.findViewById(R.id.removeCartItemButton)
        val increaseItemQuantityButton: Button = itemView.findViewById(R.id.increaseItemQuantityButton)
        val decreaseItemQuantityButton: Button = itemView.findViewById(R.id.decreaseItemQuantityButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_list, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        if(!cartItems.isNullOrEmpty()) {

            val currentItem = cartItems[position].product
            holder.productNameTextView.text = currentItem.productName
            holder.productPriceTextView.text = currentItem.productPrice.toString()
            holder.productQuantityTextView.text = cartItems[position].quantity.toString()

            Glide.with(holder.productImageView)
                .load(currentItem.imageUrl)
                .into(holder.productImageView)

            holder.increaseItemQuantityButton.setOnClickListener {
                val productId = cartItems[position].product.id
                cartItemOnClickListener.updateCartItemOnClick(productId, true)
            }

            holder.decreaseItemQuantityButton.setOnClickListener {
                val productId = cartItems[position].product.id
                cartItemOnClickListener.updateCartItemOnClick(productId, false)
            }

            holder.removeCartItemButton.setOnClickListener {
                val productId = cartItems[position].product.id
                cartItemOnClickListener.removeCartItemOnClick(productId)
            }
        } else{
            Toast.makeText(holder.itemView.context, "Cart is empty", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = cartItems.size
}