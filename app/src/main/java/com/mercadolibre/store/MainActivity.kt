package com.mercadolibre.store

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.service.Service
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.Result
import com.mercadolibre.store.view.activity.ProductListActivity

/**
 * Show the splash
 *
 */
class MainActivity : AppCompatActivity(), IResponse {

    /**
     * Create activity
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreen()
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        executeService()
    }

    private fun fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    private fun executeService() {
        val service = Service(this)
        service.listProducts(0)
    }

    /**
     * Redirect the user to the products activity
     *
     * @param response
     */
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun nextActivity(response: Result<Product>) {
        val intent = Intent(this, ProductListActivity::class.java)
        intent.putExtra("productList", ArrayList(response.results))
        startActivity(intent)
        finish()
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
        val response: Result<Product> = result as Result<Product>
        nextActivity(response)
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