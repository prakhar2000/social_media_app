package com.example.socialmediaapp.daos

import com.example.socialmediaapp.models.user
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class userdao {
    val db=FirebaseFirestore.getInstance()
    val usercollection=db.collection("users")

    fun adduser(user: user?)
    {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usercollection.document(user.uid).set(it)
            }
        }
    }

    fun getuserbyid(uid:String): Task<DocumentSnapshot>{
        return  usercollection.document(uid).get()
    }
}