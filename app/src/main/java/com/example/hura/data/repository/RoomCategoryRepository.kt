package com.example.hura.data.repository

import com.example.hura.data.local.dao.CategoryDao
import com.example.hura.data.local.entity.CategoryEntity
import com.example.hura.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

class RoomCategoryRepository(
    private val dao: CategoryDao
) : CategoryRepository {

    override suspend fun getByName(name: String): CategoryEntity? {
        return dao.getByName(name)
    }

    override suspend fun getById(id: Long): CategoryEntity? {
        return dao.getById(id);
    }

    override suspend fun insert(category: CategoryEntity): Long {
        return dao.insert(category);
    }

    override suspend fun getOrInsert(name: String, iconId: Int?): CategoryEntity {
        val existing = dao.getByName(name)
        if (existing != null) return existing

        val id = dao.insert(
            CategoryEntity(name = name, iconId = iconId)
        )

        return dao.getByName(name)!!
    }

    override fun observeAll(): Flow<List<CategoryEntity>> {
        return dao.observeAll()
    }
}