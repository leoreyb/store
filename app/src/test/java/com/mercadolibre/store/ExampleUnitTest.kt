package com.mercadolibre.store


import android.util.Log
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.service.Service
import com.mercadolibre.store.business.util.CurrencyUtil
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.ProductDetail
import com.mercadolibre.store.data.Result
import org.junit.Test

import org.junit.Assert.*
import java.lang.Exception
import java.util.concurrent.CountDownLatch

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("UNCHECKED_CAST")
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun validateCurrency() {
        val format = CurrencyUtil.formatCurrency(20000F)
        assertEquals(format, "$20,000.00")
    }

    @Test
    fun serviceListProduct() {
        val resultExpected = 20
        var productQuantity = 0
        val latch = CountDownLatch(1)
        val service = Service(object : IResponse {
            override fun onResponse(result: Any) {
                if (result is Result<*>) {
                    val response = result as Result<Product>
                    productQuantity = response.results!!.size
                }
                latch.countDown()
            }

            override fun onError() {
                latch.countDown()
            }
        })
        service.listProducts(0)
        try {
            latch.await()
        } catch (ex: Exception) {
            Log.e("Error", ex.message, ex)
        }
        assertEquals(resultExpected, productQuantity)
    }


    @Test
    fun serviceDetailProduct() {
        val id = "MLA786198896"
        var resultId = ""
        val latch = CountDownLatch(1)
        val service = Service(object : IResponse {

            override fun onResponse(result: Any) {
                if (result is ProductDetail) {
                    resultId = result.id
                }
                latch.countDown()
            }

            override fun onError() {
                latch.countDown()
            }
        })
        service.productDetail(id)
        try {
            latch.await()
        } catch (ex: Exception) {
            Log.e("Error", ex.message, ex)
        }
        assertEquals(id, resultId)
    }


}