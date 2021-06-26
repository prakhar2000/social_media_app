package com.example.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialapp.IPostAdapter
import com.example.socialapp.PostAdapter
import com.example.socialmediaapp.models.post
import com.example.socialmediaapp.daos.postdao
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),IPostAdapter {
    private lateinit var postDao: postdao
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener{

            val intent=Intent(this,createpost::class.java)
            startActivity(intent)

        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        postDao = postdao()
        val postsCollections = postDao.postcollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<post>().setQuery(query, post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updatelikes(postId)
    }
}