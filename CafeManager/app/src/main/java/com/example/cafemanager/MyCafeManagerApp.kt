package com.example.cafemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe Application da app.
 * - A anotação @HiltAndroidApp inicializa o Hilt logo no arranque do processo,
 *   permitindo injeção de dependências em toda a aplicação.
 * - Não tem lógica adicional; serve para o Hilt gerar o código necessário.
 */
@HiltAndroidApp
class MyCafeManagerApp : Application()
