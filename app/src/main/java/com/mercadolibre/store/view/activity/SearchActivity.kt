package com.mercadolibre.store.view.activity

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.store.R
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.service.Service
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.Result
import com.mercadolibre.store.view.helper.adapter.ProductAdapter

/**
 * Show search results
 *
 */
class SearchActivity : AppCompatActivity(), IResponse {

    private lateinit var service: Service
    private var start: Int = 0
    private val limit: Int = 50
    private var productList: ArrayList<Product> = ArrayList()
    private var loading: Boolean = false
    private var query: String? = null

    private lateinit var rvProductsSearch: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var pbLoading: ProgressBar
    private lateinit var tvResults: TextView

    /**
     * Create activity
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        searchProduct()
    }

    /**
     * Initialize graphic components
     *
     */
    private fun init() {
        Log.i("SearchActivity.init", "Initialize components")
        pbLoading = findViewById(R.id.pbLoading)
        tvResults = findViewById(R.id.tvResults)
        rvProductsSearch = findViewById(R.id.rvProductsSearch)
        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvProductsSearch.layoutManager = layout
        adapter = ProductAdapter(productList)
        rvProductsSearch.adapter = adapter
        rvProductsSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadMoreProducts(layout)
            }
        })
        service = Service(this)
    }

    /**
     * When the user reaches the end of the list show more results
     *
     * @param layout
     */
    private fun loadMoreProducts(layout: LinearLayoutManager) {
        Log.i("loadMoreProducts", "Quantity: ${productList.size} ")
        val position = layout.findLastVisibleItemPosition()
        val childCount = productList.size
        if (childCount - position < 2 && !loading) {
            executeService()
        }
    }

    /**
     * Return to previous activity
     *
     * @return
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /**
     * Validate the action and run the service
     *
     */
    private fun searchProduct() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { text ->
                Log.i("searchProduct", "searching: $text")
                query = text
                tvResults.text = getString(R.string.text_result, text)
                executeService()
            }
        }
    }

    /**
     * Execute search
     *
     */
    private fun executeService() {
        Log.i("executeService", "Load more products  query:$query, start: $start, limit = $limit ")
        pbLoading.visibility = View.VISIBLE
        loading = true
        service.listProducts(query, start, limit)
    }


    /**
     * Process the response from the service
     *
     * @param result
     */
    @Suppress("UNCHECKED_CAST")
    override fun onResponse(result: Any) {
        if (result !is Result<*>) {
            return
        }
        Log.i("onResponse", "Start: ${result.paging.offset} Limit: ${result.paging.limit}")
        val response: Result<Product> = result as Result<Product>
        pbLoading.visibility = View.GONE
        val productList = response.results ?: ArrayList()
        this.productList.addAll(productList)
        refreshProducts()
        loading = false

    }

    /**
     * Set the product information in the form
     */
    private fun refreshProducts() {
        val size = productList.size
        if (start != size) {
            adapter.notifyItemRangeChanged(start, size)
            start = size
        }
    }

    /**
     * Save the state of the activity
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("productList", productList)
        super.onSaveInstanceState(outState)
    }

    /**
     * Shows an error message if it could not connect to the service or if the service responds
     * with an error
     *
     */
    override fun onError() {
        Toast.makeText(this, getString(R.string.error_query), Toast.LENGTH_LONG).show()
        pbLoading.visibility = View.GONE
    }

    /**
     * Restore the state of the activity
     *
     * @param savedInstanceState
     */
    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        try {
            val tempProductList = savedInstanceState.getSerializable("productList")
            if (tempProductList != null && tempProductList is ArrayList<*>) {
                this.productList.addAll(tempProductList as ArrayList<Product>)
                refreshProducts()
            }
        } catch (ex: Exception) {
            Log.e("onRestoreInstanceState", ex.message, ex)
        }
        super.onRestoreInstanceState(savedInstanceState)
    }


}