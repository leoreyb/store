package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Product information
 *
 * @property id
 * @property title
 * @property currencyId
 * @property thumbnail
 * @property price
 * @property condition
 * @property availableQuantity
 * @property address
 */
data class Product(
    val id: String,
    val title: String,
    val currencyId: String,
    val thumbnail: String,
    val price: Float,
    val condition: String,
    val availableQuantity: Int,
    val address: Address?
) : Serializable