package com.mercadolibre.store.data

import java.io.Serializable

/**
 * Location information
 *
 * @property stateName
 * @property cityName
 */
data class Address(
    val stateName: String?,
    val cityName: String?
) : Serializable