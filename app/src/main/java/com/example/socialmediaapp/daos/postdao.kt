package com.example.socialmediaapp.daos

import com.example.socialmediaapp.models.post
import com.example.socialmediaapp.models.user
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class postdao {
    val db=FirebaseFirestore.getInstance()
    val postcollections=db.collection("posts")
    val auth= Firebase.auth
    fun addpost(text:String){
        val currentuserid=auth.currentUser!!.uid  //!! to crash the app if illegal user
        GlobalScope.launch {
            val userdao=userdao()
            val user=userdao.getuserbyid(currentuserid).await().toObject(user::class.java)!!

            val currenttime=System.currentTimeMillis()
            val post=post(text,user,currenttime)
            postcollections.document().set(post)
        }

    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postcollections.document(postId).get()
    }

    fun updatelikes(postId: String) {
        GlobalScope.launch {
            val currentUserId = auth.currentUser!!.uid
            val post = getPostById(postId).await().toObject(post::class.java)!!
            val isLiked = post.likedby.contains(currentUserId)

            if(isLiked) {
                post.likedby.remove(currentUserId)
            } else {
                post.likedby.add(currentUserId)
            }
            postcollections.document(postId).set(post)
        }

    }

}