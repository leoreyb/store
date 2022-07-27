package com.mercadolibre.store.business.api

/**
 * Listener the response form the service
 *
 */
interface IResponse {

    fun onResponse(result: Any)

    fun onError();
}