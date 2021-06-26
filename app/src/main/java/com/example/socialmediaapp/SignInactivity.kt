package com.example.socialmediaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaapp.daos.userdao
import com.example.socialmediaapp.models.user
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_inactivity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignInactivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int =123
    private lateinit var googleSignInClient: GoogleSignInClient
    private val TAG = "SignInActivity Tag"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_inactivity)



        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        signinbutton.setOnClickListener{
            signIn()
        }
    }


    override fun onStart() {
        super.onStart()
        val currentuser=auth.currentUser
        updateUI(currentuser)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSigninresult(task)
        }
    }

    private fun handleSigninresult(task: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        signinbutton.visibility=View.GONE
        progressBar.visibility=View.VISIBLE
        GlobalScope.launch(Dispatchers.IO) {
            val auth=auth.signInWithCredential(credential).await()


            val firebaseuser=auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseuser)
            }

                }




    }

    private fun updateUI(firebaseuser: FirebaseUser?) {

        if(firebaseuser != null) {


            val user= user(firebaseuser.uid,firebaseuser.displayName,firebaseuser.photoUrl.toString())
            val userdao=userdao()
            userdao.adduser(user)

            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        } else {
            signinbutton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}