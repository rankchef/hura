package com.example.hura.data.repository

import com.example.hura.data.local.dao.MerchantDao
import com.example.hura.data.local.entity.MerchantEntity

class RoomMerchantRepository (
    private val dao: MerchantDao
) : MerchantRepository {
    override suspend fun getByRawName(rawName: String): MerchantEntity? {
        return dao.getByRawName(rawName)
    }

    override suspend fun getById(id: Long): MerchantEntity? {
        return dao.getById(id)
    }

    override suspend fun insert(merchant: MerchantEntity): Long {
        return dao.insert(merchant)
    }

    override suspend fun getOrInsert(rawName: String, nickname: String?, categoryId: Long?): MerchantEntity {
        val existing = dao.getByRawName(rawName)
        if (existing != null) return existing

        dao.insert(
            MerchantEntity(
                rawName = rawName,
                nickname = nickname,
                categoryId = categoryId
            )
        )

        return dao.getByRawName(rawName)!!

    }
}