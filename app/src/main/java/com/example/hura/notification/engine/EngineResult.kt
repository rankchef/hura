package com.example.hura.notification.engine

import com.example.hura.domain.model.ParsedTransaction

sealed class EngineResult {
    object NotSupported : EngineResult()
    object ParseFailed : EngineResult()
    data class Success(
        val transaction: ParsedTransaction
    ) : EngineResult()
}