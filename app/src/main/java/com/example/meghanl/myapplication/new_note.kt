package com.example.meghanl.myapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import android.speech.RecognizerIntent
import android.widget.ImageButton
import android.content.ActivityNotFoundException
import java.util.*


class ActivityNewNote : AppCompatActivity() {

    private val REQ_CODE_SPEECH_INPUT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_note)
        val but = findViewById<Button>(R.id.Push)
        val mic = findViewById<ImageButton>(R.id.btnSpeak)
        but.setOnClickListener {
            val bodyB = findViewById<EditText>(R.id.Body)
            val body = bodyB.text.toString()
            val ref = FirebaseDatabase.getInstance().getReference("notes")
            val noteId: String = ref.push().key.toString()
            val newN = MyNote(body, noteId)
            ref.child(noteId).setValue(newN).addOnCompleteListener {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_LONG).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
        mic.setOnClickListener {
            promptSpeechInput()
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something")
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Voice input not supported", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bodyB = findViewById<EditText>(R.id.Body)
        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    bodyB.setText(result[0])
                }
            }
        }
    }

}