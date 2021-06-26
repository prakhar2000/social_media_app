package com.example.socialmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmediaapp.daos.postdao
import kotlinx.android.synthetic.main.activity_createpost.*

class createpost : AppCompatActivity() {

    private  lateinit var postdao: postdao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createpost)

         postdao=postdao()

        postbutton.setOnClickListener{
            val input=editpost.text.toString().trim()
            if(input.isNotEmpty()){

                postdao.addpost(input)
                finish()

            }
        }
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

    }
}