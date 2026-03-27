package com.example.hura.domain.repository

import com.example.hura.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getById(id: Long): CategoryEntity?
    suspend fun getByName(name: String): CategoryEntity?
    suspend fun insert(category: CategoryEntity): Long
    suspend fun getOrInsert(name: String, iconId: Int? = null): CategoryEntity
    fun observeAll(): Flow<List<CategoryEntity>>
}