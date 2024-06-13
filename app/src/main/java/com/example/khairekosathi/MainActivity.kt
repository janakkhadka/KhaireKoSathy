package com.example.khairekosathi

import com.example.khairekosathi.R
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.khairekosathi.databinding.ActivityMainBinding
import com.example.khairekosathi.recordaudio.AndroidAudioRecorder
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import com.example.khairekosathi.playbackaudio.AndroidAudioPlayer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaRecorder: MediaRecorder? = null
    private var outputFilePath: String = ""
    private lateinit var recorder: AndroidAudioRecorder
    private lateinit var player: AndroidAudioPlayer

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        recorder = AndroidAudioRecorder()
        player = AndroidAudioPlayer()



        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }





//handling onsetclicklistener
        var check = true
        binding.cvMicEnglish.setOnClickListener {

            outputFilePath = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"

            if(check) {
                binding.cvMicEnglish.isEnabled = false //disabling mic after first click

                binding.ivMicEnglish.setImageResource(R.drawable.ic_recording_mic)//changing to red mic

                Toast.makeText(this,"I am listening now...",Toast.LENGTH_SHORT).show()

                check = false

                recorder.startRecording(outputFilePath)
            }

        }
        binding.cvMicNepali.setOnClickListener {
            if(check) {
                outputFilePath = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"

                binding.cvMicNepali.isEnabled = false //disabling mic after first click

                binding.ivMicNepali.setImageResource(R.drawable.ic_recording_mic)//changing to red mic

                player.playRecording(outputFilePath)

                Toast.makeText(this,"I am listening now...",Toast.LENGTH_SHORT).show()

                check = false


            }
        }
        binding.cvStop.setOnClickListener {
            if(!check) {

                //turn to original mic image
                binding.ivMicEnglish.setImageResource(R.drawable.ic_english_mic)
                binding.ivMicNepali.setImageResource(R.drawable.ic_nepali_mic)

                //re-enabling mic
                binding.cvMicEnglish.isEnabled = true
                binding.cvMicNepali.isEnabled = true

                recorder.stopRecording()

                check = true
            }
        }
    }


    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

}