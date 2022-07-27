package com.mercadolibre.store.view.helper.holder

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mercadolibre.store.R
import com.mercadolibre.store.business.util.CurrencyUtil
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.view.activity.ProductDetailActivity

/**
 * Show product information in the RecyclerView
 *
 * @constructor
 * Item to inflate
 *
 * @param itemView
 */
class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    private val imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
    private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
    private val container: ConstraintLayout = itemView.findViewById(R.id.container)
    private lateinit var product: Product

    /**
     * Set container click event
     */
    init {
        container.setOnClickListener(View.OnClickListener {
            click()
        })
    }

    /**
     * Show product information in the item
     *
     * @param product product information
     */
    fun bind(product: Product) {
        this.product = product
        tvTitle.text = product.title
        tvPrice.text = CurrencyUtil.formatCurrency(product.price)
        loadImage(product.thumbnail)
    }

    /**
     * Load product image
     *
     * @param path url
     */
    private fun loadImage(path: String?) {
        if (path == null) {
            return
        }
        Glide.with(itemView)
            .load(path)
            .centerCrop()
            .into(imgPhoto)
    }

    /**
     * Show product detail activity
     *
     */
    private fun click() {
        Log.i("ProductHolder.click", "Show product information: ${product.id}")
        val intent = Intent(itemView.context, ProductDetailActivity::class.java)
        intent.putExtra("product", product)
        itemView.context.startActivity(intent)
    }
}