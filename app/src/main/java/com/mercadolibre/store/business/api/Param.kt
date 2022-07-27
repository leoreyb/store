package com.mercadolibre.store.business.api

/**
 * Service parameters
 *
 */
class Param : HashMap<String, Any>() {
    private val limit: Int = 20;
    private val start: Int = 0;
    private val category: String = "MLA5725"

    init {
        put("category", category)
        setLimit(limit)
        setOffset(start)
    }

    /**
     * set limit
     *
     * @param limit
     */
    fun setLimit(limit: Int) {
        put("limit", limit)
    }

    /**
     * set offset
     *
     * @param offset
     */
    fun setOffset(offset: Int) {
        put("offset", offset)
    }

    /**
     * set search criteria
     *
     * @param search
     */
    fun setSearch(search: String) {
        remove("category")
        put("q", search);
    }

}