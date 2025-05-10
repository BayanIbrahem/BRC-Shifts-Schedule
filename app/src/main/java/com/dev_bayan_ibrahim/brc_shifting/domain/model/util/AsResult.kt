package com.dev_bayan_ibrahim.brc_shifting.domain.model.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.asResult(): Flow<Result<T>> = this.map { Result.success(it) }.catch {
    Result.failure<T>(it)
}