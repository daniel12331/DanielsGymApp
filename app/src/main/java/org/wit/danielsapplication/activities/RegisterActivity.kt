package org.wit.danielsapplication.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.danielsapplication.R
import org.wit.danielsapplication.firestore.FireStore
import org.wit.danielsapplication.models.User

class RegisterActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Full Screen Experience
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Listener switches from registeractivity to loginactivity, and closes this activity
        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            info("Login Activity running ~~ Closing Register Activity")
        }

        // Validation for register activity, using when condition to validate the user fills out fields and matches password
        btn_register.setOnClickListener {
            when {
                et_first_name.text.toString().isEmpty() -> toast("Please fill in your first name")

                et_last_name.text.toString().isEmpty() -> toast("Please fill in your last name")

                et_email.text.toString().isEmpty() -> toast("Please fill your email")

                et_password.text.toString().isEmpty() -> toast("Please fill in your password")

                et_password.text.toString().length < 6 ->toast("Please make sure password is more then five characters")

                et_confirm_password.text.toString().isEmpty() -> toast("Please fill in your confirm password")

                !et_password.text.toString().equals(et_confirm_password.text.toString()) -> toast("Please make sure your password matches")

                else  -> RegisterUser()

                }
        }
}
        private fun RegisterUser() {
            // Trimming email and password to remove empty spaces
            val email: String = et_email.text.toString().trim{ it <= ' '}
            val password: String = et_password.text.toString().trim{it <= ' '}


            // Firebaseauth is the class coming from the firebase dependencies that I added, we can see that email + password is passed through
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)

                    // addOnCompleteListner is executed after the creation of the account is finished, and then the OnCompleteListner waits for auth-results and then checks if the task is
                    // successful and then creates a new firebase user as an object
                    // This is reference link to where I got help from + finding classes -> //https://firebase.google.com/docs/auth/android/password-auth?authuser=0#kotlin+ktx
                    // Note - for future reference I did have some problems at the start when trying to create a new User, I found a solution by uninstalling the application
                    // on the API and then cleaning the project
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful) {

                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{ it <= ' '},
                                et_last_name.text.toString().trim{ it <= ' '},
                                et_email.text.toString().trim{ it <= ' '}
                            )

                            //Storing above data in the cloud
                            //
                            FireStore().registerUser(this@RegisterActivity, user)

                            // Logging the User ID
                            info("New USERID created : ${firebaseUser.uid} ")

                            //I want to have the user to sign in through the LoginActivity once he/she registers an account so I use the signout method to sign the user out and close this activity
                            // link referenced below
                            // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuth#signOut()

                        } else {
                            info("createUserWithEmail:failure", task.exception)
                            toast("Authentication failed")
                        }
                    })
        }
    // https://firebase.google.com/docs/auth/android/start
    // https://firebase.google.com/docs/rules/get-started?authuser=0

    fun userRegisterSuccess(){

        toast("You are registered successfully")
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
