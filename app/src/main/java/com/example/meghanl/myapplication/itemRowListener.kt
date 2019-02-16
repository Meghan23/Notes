package com.example.meghanl.myapplication

interface itemRowListener {

    fun modifyItemState(id: String, newBody: String)
    fun onItemDelete(id : String)
}