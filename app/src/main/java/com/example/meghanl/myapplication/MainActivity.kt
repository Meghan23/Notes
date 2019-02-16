package com.example.meghanl.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList


class MainActivity : AppCompatActivity() {


    val list = ArrayList<MyNote>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val myRef: DatabaseReference = database.reference

        val r = myRef.child("notes")

        r.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {

                    val note = it.getValue(MyNote::class.java) as MyNote
                    list.add(note)
                    viewAdapter = RecyclerAdapter(list)
                    recyclerView = findViewById<RecyclerView>(R.id.my_notes).apply {

                        setHasFixedSize(true)
                        layoutManager = viewManager

                        adapter = viewAdapter
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        )

        val but = findViewById<FloatingActionButton>(R.id.add_note)
        but.setOnClickListener {
            val intent = Intent(this, ActivityNewNote::class.java)
            startActivity(intent)
        }
    }

}


