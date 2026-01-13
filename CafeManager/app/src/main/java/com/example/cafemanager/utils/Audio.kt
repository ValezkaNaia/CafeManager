package com.example.cafemanager.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Pequenas utilidades de áudio:
 * - Música de fundo em loop (MediaPlayer) com ciclo de vida do Composable.
 * - Efeitos sonoros one-shot (criam e libertam MediaPlayer automaticamente).
 */

class LoopingMusic(private val mediaPlayer: MediaPlayer) {
    fun start() {
        if (!mediaPlayer.isPlaying) mediaPlayer.start()
    }
    fun pause() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
    }
}

@Composable
fun rememberLoopingMusic(@RawRes resId: Int, volume: Float = 1.0f): LoopingMusic {
    val context = LocalContext.current
    val mp = remember(resId) {
        MediaPlayer.create(context, resId).apply {
            isLooping = true
            setVolume(volume, volume)
        }
    }
    // Assegura libertação quando o Composable sai do ecrã
    DisposableEffect(Unit) {
        onDispose {
            try { mp.stop() } catch (_: Exception) {}
            try { mp.release() } catch (_: Exception) {}
        }
    }
    return remember { LoopingMusic(mp) }
}

fun playSfx(context: Context, @RawRes resId: Int, volume: Float = 1.0f) {
    // MediaPlayer simples para tocar e libertar no onCompletion
    val mp = MediaPlayer.create(context, resId)
    mp.setVolume(volume, volume)
    mp.setOnCompletionListener { player ->
        try { player.release() } catch (_: Exception) {}
    }
    mp.start()
}
