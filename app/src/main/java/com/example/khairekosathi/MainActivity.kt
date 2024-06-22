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
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.khairekosathi.playbackaudio.AndroidAudioPlayer


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaRecorder: MediaRecorder? = null
    private var outputFilePath: String = ""
    private lateinit var recorder: AndroidAudioRecorder
    private lateinit var player: AndroidAudioPlayer

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent



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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }


        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                binding.tvText.text = "listening..."
            }

            override fun onBeginningOfSpeech() {
                binding.tvText.text = "Iam listening ..."
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                binding.tvText.text = "processing"
            }

            override fun onError(error: Int) {
                binding.tvText.text = error.toString()
                binding.cvMicEnglish.isEnabled = true
                binding.cvStop.isEnabled = false
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                binding.tvText.text = matches?.get(0) ?: "No results"
                binding.cvMicEnglish.isEnabled = true
                binding.cvStop.isEnabled = false
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })





//handling onsetclicklistener
        var check = true
        binding.cvMicEnglish.setOnClickListener {

            outputFilePath = "${externalCacheDir?.absolutePath}/audiorecordtest.wav"

            if(check) {
                binding.cvMicEnglish.isEnabled = false //disabling mic after first click

                binding.ivMicEnglish.setImageResource(R.drawable.ic_recording_mic)//changing to red mic

                //Toast.makeText(this,"I am listening now...",Toast.LENGTH_SHORT).show()

                check = false

                //recorder.startRecording(outputFilePath)

                speechRecognizer.startListening(speechRecognizerIntent)

                binding.cvMicEnglish.isEnabled = false
                binding.cvStop.isEnabled = true



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

                //recorder.stopRecording()

                speechRecognizer.stopListening()

                check = true
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
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