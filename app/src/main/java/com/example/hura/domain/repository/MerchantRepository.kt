package com.example.hura.domain.repository

import com.example.hura.data.local.entity.MerchantEntity

interface MerchantRepository {
    suspend fun getById(id: Long): MerchantEntity?
    suspend fun getByRawName(rawName: String): MerchantEntity?
    suspend fun insert(merchant: MerchantEntity): Long
    suspend fun getOrInsert(rawName: String, nickname: String? = null, categoryId: Long? = null): MerchantEntity
}