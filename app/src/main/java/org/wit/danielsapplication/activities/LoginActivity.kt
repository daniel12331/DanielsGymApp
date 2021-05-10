package org.wit.danielsapplication.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.danielsapplication.R
import org.wit.danielsapplication.firestore.FireStore
import org.wit.danielsapplication.models.User
import kotlinx.android.synthetic.main.activity_login.et_email as et_email1
import kotlinx.android.synthetic.main.activity_login.et_password as et_password1

class LoginActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Same method used in the splash activity
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
// Listener switches from loginactivity to registeractivity, and closes this activity
        tv_register.setOnClickListener {

            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            //  finish() // Could possibly leave this out, wont allow the user to go back to the previous activity but at the same time will keep a low amount activities open at one time
            info("Register Activity running ~~ Closing Login Activity")

        }

        btn_login.setOnClickListener {
            // Validating login details
            when {
                et_email.text.toString().isEmpty() -> toast("Please fill your email")

                et_password.text.toString().isEmpty() -> toast("Please fill in your password")

                else  -> LoginUser()

            }
        }
    }
        private fun LoginUser() {
            //Gets email password that is passed through the edit text
            val email: String = et_email1.text.toString().trim{ it <= ' '}
            val password: String = et_password1.text.toString().trim{it <= ' '}

            // Logging in with email and password
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    //If task is successful follow the method in firestore clas
                    if(task.isSuccessful){
                        FireStore().getUserDetails(this@LoginActivity)


                    } else {

                        info("createUserWithEmail:failure", task.exception)
                        toast("Authentication failed - Check if your email or password is correct")
                    }
                }
        }
    fun LogInSuccess(user: User){
// Logs user info and switches activity to -> HomeActivity this is called from the firbase store class
        info(user.firstName)
        info(user.lastName)
        info(user.email)
        info(user.id)
        toast("Welcome back ${user.firstName}")

        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        finish()
    }

}