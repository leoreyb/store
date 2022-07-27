package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Pagination
 *
 * @property total
 * @property offset
 * @property limit
 */
data class Page(
    val total: Int,
    val offset: Int,
    val limit: Int
) : Serializable
