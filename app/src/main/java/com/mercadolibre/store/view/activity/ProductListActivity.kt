package com.mercadolibre.store.view.activity

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercadolibre.store.R
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.service.Service
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.Result
import com.mercadolibre.store.view.helper.adapter.ProductAdapter


/**
 * Show product list
 *
 */
@Suppress("UNCHECKED_CAST")
class ProductListActivity : AppCompatActivity(), IResponse {

    private lateinit var rvProducts: RecyclerView
    private lateinit var service: Service
    private var productList: ArrayList<Product> = ArrayList()
    private lateinit var adapter: ProductAdapter
    private var lastProductIndex: Int = 0
    private var isLoading: Boolean = false
    private val maxQuantity = 200
    private var query: String? = null
    private lateinit var pbLoading: ProgressBar


    /**
     * create activity
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        init(savedInstanceState == null)
    }


    /**
     * Initializes the graphical components of the interface
     *
     * @param invoke Indicates whether the service should be invoked to query product information
     */
    private fun init(invoke: Boolean) {
        rvProducts = findViewById(R.id.rvProducts)
        pbLoading = findViewById(R.id.pbLoading)
        val layout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvProducts.layoutManager = layout
        adapter = ProductAdapter(productList)
        rvProducts.adapter = adapter
        rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                loadMoreProducts(layout)
            }
        })
        service = Service(this)
        if (invoke) {
            val newList: ArrayList<Product> =
                intent.getSerializableExtra("productList") as ArrayList<Product>
            productList.addAll(newList)
            refreshProducts()
        }
    }

    /**
     * Show a text
     *
     * @param text
     */
    private fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    /**
     * Config contextual menu
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val menuItem = menu?.findItem(R.id.search)
        configSearchView(menuItem)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Create and config search view
     *
     * @param menuItem
     */
    private fun configSearchView(menuItem: MenuItem?) {
        Log.i("configSearchView", "create search manager")
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menuItem?.actionView as SearchView
        val componentName = ComponentName(this, SearchActivity::class.java)
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView.setSearchableInfo(searchableInfo)
        if (query != null && query!!.isNotBlank()) {
            Log.i("configSearchView", "")
            menuItem.expandActionView()
            searchView.setQuery(query, false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newQuery: String?): Boolean {
                menuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                query = newText
                return true
            }
        })

    }

    /**
     * Get more products from service
     */
    private fun executeService() {
        service.listProducts(lastProductIndex)
        isLoading = true
        pbLoading.visibility = View.VISIBLE
    }


    /**
     * When the user reaches the end of the list show more results
     *
     * @param layout
     */
    private fun loadMoreProducts(layout: LinearLayoutManager) {
        Log.i("loadMoreProducts", "Quantity: ${productList.size} ")
        if (maxQuantity <= productList.size || isLoading) {
            return
        }
        val position = layout.findLastVisibleItemPosition()
        val childCount = productList.size
        if (childCount - position < 2) {
            Log.i("init", "Load more products")
            executeService()
        }
    }

    /**
     * Update product information in the RecyclerView
     *
     */
    private fun refreshProducts() {
        val size = productList.size
        if (lastProductIndex != size) {
            adapter.notifyItemRangeChanged(lastProductIndex, size)
            lastProductIndex = size
        }
    }

    /**
     * Save the state of the activity
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("productList", productList)
        outState.putString("query", query)
        super.onSaveInstanceState(outState)
    }

    /**
     * Restore the state of the activity
     *
     * @param savedInstanceState
     */
    @Suppress("UNCHECKED_CAST")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val tempProductList = savedInstanceState.getSerializable("productList")
        query = savedInstanceState.getString("query")
        if (tempProductList != null && tempProductList is ArrayList<*>) {
            this.productList.addAll(tempProductList as ArrayList<Product>)
            refreshProducts()
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    /**
     * Process the response from the service
     *
     * @param result
     */
    override fun onResponse(result: Any) {
        if (result !is Result<*>) {
            return
        }
        val response: Result<Product> = result as Result<Product>
        pbLoading.visibility = View.GONE
        val list = response.results ?: ArrayList()
        productList.addAll(list)
        refreshProducts()
        if (productList.size >= maxQuantity) {
            showMessage(getString(R.string.use_search))
        }
        isLoading = false
    }

    /**
     * Shows an error message if it could not connect to the service or if the service responds
     * with an error
     *
     */
    override fun onError() {
        pbLoading.visibility = View.GONE
        showMessage(getString(R.string.error_query))
    }


}