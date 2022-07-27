package com.mercadolibre.store.business.util

import java.text.NumberFormat

/**
 * Currency util
 *
 */
class CurrencyUtil {

    companion object {
        /**
         * set currency format
         *
         * @param amount
         * @return Currency format
         */
        fun formatCurrency(amount: Float?): String {
            if (amount == null) {
                return "-"
            }
            val format = NumberFormat.getCurrencyInstance()
            return format.format(amount)
        }
    }
}