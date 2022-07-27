package com.mercadolibre.store.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.store.R
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.service.Service
import com.mercadolibre.store.business.util.CurrencyUtil
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.ProductDetail
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

/**
 * Shows the detail of a product selected by the user
 *
 */
class ProductDetailActivity : AppCompatActivity(), IResponse {


    private lateinit var carousel: ImageCarousel
    private lateinit var tvProductTitle: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvWarranty: TextView
    private lateinit var service: Service
    private lateinit var productDetail: ProductDetail

    /**
     * create activity
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        init(savedInstanceState == null)
    }

    /**
     * Initializes the graphical components of the interface
     *
     * @param invoke Indicates whether the service should be invoked to query product information
     */
    private fun init(invoke: Boolean) {
        try {
            Log.i("init", "Initialize ProductDetailActivity")
            carousel = findViewById(R.id.carousel)
            tvProductTitle = findViewById(R.id.tvProductTitle)
            tvProductPrice = findViewById(R.id.tvProductPrice)
            tvWarranty = findViewById(R.id.tvWarranty)
            service = Service(this)
            val product = intent.getSerializableExtra("product") as Product
            tvProductTitle.text = product.title
            if (invoke) {
                executeService(product.id)
            }
        } catch (ex: Exception) {
            Log.e("init", ex.message, ex)
        }
    }

    /**
     * Set the product information in the form
     */
    private fun updateInfo() {
        tvProductTitle.text = productDetail.title
        tvProductPrice.text = CurrencyUtil.formatCurrency(productDetail.price)
        tvWarranty.text = productDetail.warranty
        updateCarousel()
    }

    /**
     * Show product images in the carousel
     */
    private fun updateCarousel() {
        val list = ArrayList<CarouselItem>()
        for (picture in productDetail.pictures) {
            list.add(CarouselItem(picture.url))
        }
        carousel.setData(list)
    }

    /**
     * Save the state of the activity
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("product", productDetail)
        super.onSaveInstanceState(outState)
    }

    /**
     * Restore the state of the activity
     *
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        productDetail = savedInstanceState.getSerializable("product") as ProductDetail
        updateInfo()
        super.onRestoreInstanceState(savedInstanceState)
    }


    /**
     * Get the product information
     * @param productId product ID
     */
    private fun executeService(productId: String?) {
        Log.i("executeService", "Run get product information service: $productId")
        if (productId == null) {
            return
        }
        service.productDetail(productId)
    }

    /**
     * Process the response from the service
     *
     * @param result
     */
    override fun onResponse(result: Any) {
        if (result !is ProductDetail) {
            return
        }
        Log.i("onResponse", "Product: ${result.title}")
        productDetail = result
        updateInfo()
    }

    /**
     * Shows an error message if it could not connect to the service or if the service responds
     * with an error
     *
     */
    override fun onError() {
        Toast.makeText(this, getString(R.string.error_load_product), Toast.LENGTH_LONG).show()
    }


}