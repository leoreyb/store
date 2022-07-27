package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Product list information
 *
 * @param T (ProductDetail, Product)
 * @property query
 * @property results Product List
 * @property paging
 */
data class Result<T>(
    val query: String?,
    var results: List<T>?,
    val paging: Page
) : Serializable
