package com.mercadolibre.store.business.service

import android.util.Log
import com.mercadolibre.store.business.api.Endpoint
import com.mercadolibre.store.business.api.IResponse
import com.mercadolibre.store.business.api.Param
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.ProductDetail
import com.mercadolibre.store.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class responsible for executing web services
 *
 * @property listener
 */
class Service(
    val listener: IResponse
) {

    /**
     * Processes the response and validates if the request was maed correctly
     *
     * @param response
     */
    private fun processResponse(response: Response<*>) {
        try {
            Log.i(
                "processResponse",
                " ResponseCode: ${response.code()}, Successful: ${response.isSuccessful} "
            )
            if (!response.isSuccessful) {
                listener.onError()
                return
            }
            val result = response.body()
            if (result != null) {
                listener.onResponse(result)
            }
        } catch (ex: Exception) {
            Log.e("processResponse", ex.message, ex)
            listener.onError()
        }

    }

    /**
     * Get 20 products more
     *
     * @param offset Start at record
     */
    fun listProducts(offset: Int) {
        listProducts(null, offset, null)
    }

    /**
     * Run a product serarch
     *
     * @param search product description
     * @param offset Start at record
     * @param limit  record Quantity
     */
    fun listProducts(
        search: String? = null,
        offset: Int? = null,
        limit: Int? = null
    ) {
        Log.i("listProduct", "search: $search, offset: $offset, limit: $limit")
        val param = getParam(search, offset, limit)
        Endpoint.create().listProducts(param)
            .enqueue(object : Callback<Result<Product>?> {
                override fun onResponse(
                    call: Call<Result<Product>?>,
                    response: Response<Result<Product>?>
                ) {
                    processResponse(response)
                }

                override fun onFailure(call: Call<Result<Product>?>, t: Throwable) {
                    Log.e("Service.listProducts", t.message, t)
                    listener.onError()
                }
            })
    }

    /**
     * Create parameter to service
     *
     * @param search
     * @param offset
     * @param limit
     * @return
     */
    private fun getParam(search: String?, offset: Int?, limit: Int?): Param {
        val param = Param()
        if (search != null) {
            param.setSearch(search.trim())
        }
        if (limit != null) {
            param.setLimit(limit)
        }
        if (offset != null) {
            param.setOffset(offset)
        }
        return param;
    }

    /**
     * Get product detail
     *
     * @param productId
     */
    fun productDetail(productId: String) {
        Endpoint.create().productDetail(productId)
            .enqueue(object : Callback<ProductDetail?> {
                override fun onResponse(
                    call: Call<ProductDetail?>,
                    response: Response<ProductDetail?>
                ) {
                    processResponse(response)
                }

                override fun onFailure(call: Call<ProductDetail?>, t: Throwable) {
                    Log.e("Service.productDetail", t.message, t)
                    listener.onError()
                }
            })
    }

}