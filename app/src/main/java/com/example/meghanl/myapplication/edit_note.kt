package com.example.meghanl.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import android.speech.RecognizerIntent
import android.widget.ImageButton
import android.content.ActivityNotFoundException
import java.util.*


var database = FirebaseDatabase.getInstance()
val myRef: DatabaseReference = database.reference
val r = myRef.child("notes")


class ActivityEditNote : AppCompatActivity(), itemRowListener {

    private val REQ_CODE_SPEECH_INPUT = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note)
        val data = intent.getStringExtra("data")
        val nid = intent.getStringExtra("id")
        val but = findViewById<Button>(R.id.Push)
        val but1 = findViewById<Button>(R.id.Delete)
        val bodyB = findViewById<EditText>(R.id.Body)
        val mic = findViewById<ImageButton>(R.id.btnSpeak)
        bodyB.setText(data)
        mic.setOnClickListener {
            promptSpeechInput()
        }
        but.setOnClickListener {
            val body = bodyB.text.toString()
            if(body!="")
                modifyItemState(nid, body)
            else
                onItemDelete(nid)
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
        but1.setOnClickListener {
            onItemDelete(nid)
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }
    }

    override fun modifyItemState(id: String, newBody: String) {

        val newN = MyNote(newBody,id)
        r.child(id).setValue(newN).addOnCompleteListener {
            Toast.makeText(this, "Note edited successfully", Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemDelete(id: String) {

        r.child(id).removeValue().addOnCompleteListener {
            Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_LONG).show()
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