package com.example.cafemanager.repositories

/**
 * Wrapper genérico para representar o estado de uma operação assíncrona.
 * - Success: operação concluída com sucesso e dados disponíveis.
 * - Error: operação falhou com uma mensagem curta para a UI.
 * - Loading: operação em curso (pode opcionalmente transportar dados parciais).
 */
sealed class ResultWrapper<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : ResultWrapper<T>(data)
    class Error<T>(message: String, data: T? = null) : ResultWrapper<T>(data, message)
    class Loading<T>(data: T? = null) : ResultWrapper<T>(data)
}
