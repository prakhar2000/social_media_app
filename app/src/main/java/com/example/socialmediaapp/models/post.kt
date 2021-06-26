package com.example.socialmediaapp.models



data class post(
    val text: String="",
    val createdby: user=user(),
    val createdAt:Long=0L,
    val likedby: ArrayList<String> = ArrayList())




