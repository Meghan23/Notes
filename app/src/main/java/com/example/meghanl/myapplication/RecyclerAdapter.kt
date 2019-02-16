package com.example.meghanl.myapplication

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.app.AppCompatActivity



lateinit var data : String

class RecyclerAdapter(var list: List<MyNote>) : RecyclerView.Adapter<RecyclerAdapter.MyHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.note, parent, false) as CardView
        return MyHoder(view)
    }

    override fun onBindViewHolder(holder: MyHoder, position: Int) {
        val myList = list[position]
        holder.name.text = (myList.body)
        holder.id = (myList.id)

    }


    override fun getItemCount() = list.size

    inner class MyHoder(val textView: CardView) : RecyclerView.ViewHolder(textView) {

        var name: TextView
        lateinit var id : String


        init {
            name = textView.findViewById(R.id.vname)

            textView.setOnClickListener {

                data = name.text.toString()
                val intent = Intent(textView.context, ActivityEditNote::class.java)
                intent.putExtra("data", data)
                intent.putExtra("id", id)
                textView.context.startActivity(intent)
            }

        /*    textView.setOnLongClickListener {
                (it.getContext() as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
                true
            }
         */
        }

    }
}

