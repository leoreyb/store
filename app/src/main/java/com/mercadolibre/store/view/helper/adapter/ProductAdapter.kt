package com.mercadolibre.store.view.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.store.R
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.view.helper.holder.ProductHolder

/**
 * Create product list
 *
 * @property productList Product list to show
 */
class ProductAdapter(
    private val productList: List<Product>
) : RecyclerView.Adapter<ProductHolder>() {

    /**
     * Inflate an item
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductHolder(view)
    }

    /**
     * Show product information
     *
     * @param holder product list item
     * @param position product list position
     */
    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    /**
     * Product list size
     *
     * @return Quantity
     */
    override fun getItemCount(): Int {
        return productList.size
    }

}