package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Product Detail information
 *
 * @property id
 * @property title
 * @property price
 * @property pictures
 * @property warranty
 */
data class ProductDetail(
    val id: String,
    val title: String,
    val price: Float,
    val pictures: ArrayList<Picture>,
    val warranty: String

) : Serializable
