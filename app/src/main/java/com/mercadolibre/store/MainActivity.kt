package com.mercadolibre.store

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mercadolibre.store.view.activity.ProductListActivity

/**
 * Show the splash
 *
 */
class MainActivity : AppCompatActivity() {

    /**
     * Create activity
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nextActivity()
    }

    /**
     * Redirect the user to the products activity
     *
     */
    private fun nextActivity() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        finish()
    }
}