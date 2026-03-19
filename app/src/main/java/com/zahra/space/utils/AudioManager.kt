package com.zahra.space.utils
import android.content.Context
import android.media.MediaPlayer
class AudioManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    fun playMurottal(surahId: Int) {
        try {
            val fileName = String.format("%03d", surahId) + ".mp3"
            val afd = context.assets.openFd("audio/murottal/$fileName")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun playBackgroundMusic() {
        try {
            val afd = context.assets.openFd("audio/music/background1.mp3")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun playSFX(sfx: String) {
        try {
            val path = when (sfx) {
                "meow" -> "audio/sfx/cat_meow.mp3"
                "click" -> "audio/sfx/click.mp3"
                "azan" -> "audio/sfx/azan.mp3"
                "water" -> "audio/sfx/water.mp3"
                "cooking" -> "audio/sfx/cooking.mp3"
                else -> return
            }
            val afd = context.assets.openFd(path)
            MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setOnCompletionListener { release() }
                prepare()
                start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
    fun stop() { mediaPlayer?.stop(); mediaPlayer?.release(); mediaPlayer = null }
}
