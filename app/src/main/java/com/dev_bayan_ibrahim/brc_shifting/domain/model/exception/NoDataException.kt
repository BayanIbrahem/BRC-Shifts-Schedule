package com.dev_bayan_ibrahim.brc_shifting.domain.model.exception

sealed class DataException(override val message: String? = null): Exception(message)
data class NoDataException(override val message: String? = null) : DataException(message)
data class UnAccessibleDataException(override val message: String? = null) : DataException(message)
