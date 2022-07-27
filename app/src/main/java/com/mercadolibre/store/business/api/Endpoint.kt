package com.mercadolibre.store.business.api

import com.mercadolibre.store.BuildConfig
import com.mercadolibre.store.data.Product
import com.mercadolibre.store.data.ProductDetail
import com.mercadolibre.store.data.Result
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * List of exposed services
 *
 */
interface Endpoint {

    companion object {
        /**
         * Create the retrofit object to make a request
         *
         * @return Retrofit object
         */
        fun create(): Endpoint {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(Endpoint::class.java)
        }
    }

    @GET("sites/MLA/search")
    fun listProducts(@QueryMap param: Param): Call<Result<Product>>

    @GET("items/{productId}")
    fun productDetail(@Path("productId") productId: String): Call<ProductDetail>

}