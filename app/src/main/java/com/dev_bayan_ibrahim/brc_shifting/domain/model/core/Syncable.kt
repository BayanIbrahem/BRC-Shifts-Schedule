package com.dev_bayan_ibrahim.brc_shifting.domain.model.core

import kotlinx.datetime.Instant

interface Syncable {
    val createdAt: Instant
    val updatedAt: Instant
}