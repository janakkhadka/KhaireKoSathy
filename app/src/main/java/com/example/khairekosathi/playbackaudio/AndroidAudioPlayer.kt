package com.example.khairekosathi.playbackaudio

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
import java.io.IOException

class AndroidAudioPlayer{

    private var mediaPlayer: MediaPlayer? = null

    fun playRecording(file: String) {
    mediaPlayer = MediaPlayer().apply {
        try {
            setDataSource(file)
            prepare()
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
}