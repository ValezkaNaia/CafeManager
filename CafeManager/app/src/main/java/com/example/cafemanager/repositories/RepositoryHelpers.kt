package com.example.cafemanager.repositories

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Extensões utilitárias para converter listeners do Firestore em Flows Kotlin.
 * - snapshotFlow() para Query: emite QuerySnapshot sempre que há alterações.
 * - snapshotFlow() para DocumentReference: emite DocumentSnapshot quando o documento muda.
 */
fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
    // Adiciona um listener ao query; sempre que há update, tenta enviar o snapshot pelo flow
    val listenerRegistration = addSnapshotListener { value, error ->
        if (error != null) {
            // Em caso de erro, fecha o fluxo
            close()
            return@addSnapshotListener
        }
        if (value != null)
            trySend(value)
    }
    // Remove o listener quando o flow é cancelado/fechado
    awaitClose {
        listenerRegistration.remove()
    }
}

fun DocumentReference.snapshotFlow(): Flow<DocumentSnapshot> = callbackFlow {
    // Listener para um documento específico
    val listenerRegistration = addSnapshotListener { value, error ->
        if (error != null) {
            close()
            return@addSnapshotListener
        }
        if (value != null)
            trySend(value)
    }
    awaitClose {
        listenerRegistration.remove()
    }
}
