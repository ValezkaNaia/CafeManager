package com.example.cafemanager.views

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de DI (Hilt) da aplicação.
 * - Fornece instâncias singleton de FirebaseAuth e FirebaseFirestore.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Fornece uma instância singleton do FirebaseAuth. */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /** Fornece uma instância singleton do FirebaseFirestore. */
    @Provides
    @Singleton
    fun firestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
