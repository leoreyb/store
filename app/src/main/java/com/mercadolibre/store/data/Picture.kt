package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Product images
 *
 * @property id
 * @property url
 */
data class Picture(
    val id: String,
    val url: String
) : Serializable
