package org.wit.danielsapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_session.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.danielsapplication.R
import org.wit.danielsapplication.firestore.FireStore
import org.wit.danielsapplication.models.Session

class AddSessionActivity : AppCompatActivity(), AnkoLogger {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_session)

        // Verifying that user does not leave anything blank
        btn_add.setOnClickListener {
            when {
                et_resource.text.toString().isEmpty() -> toast("Please fill in your Resource")

                et_date.text.toString().isEmpty() -> toast("Please fill your Date")

                et_time.text.toString().isEmpty() -> toast("Please Fill in your Time")

                else  -> uploadSessionDetails()

            }
        }
    }
    private fun uploadSessionDetails(){
        // Session Object
            val session = Session(
                //Previous method we used to get userID so we know which user had created the session
                FireStore().getUserID(),
                et_resource.text.toString().trim{it <= ' '},
                et_time.text.toString().trim{it <= ' '},
                et_date.text.toString().trim{it <= ' '}

            )

        // Passes Params through method created in firebase class
        FireStore().UploadSessionDetails(this, session)
    }

    fun UploadSuccess(){
        // Toast + logs that session was added to firebase successfully + closes the add activity and brings u back to list fragment
        toast("Session was added successful")
        info("Session was added successfully")
        finish()
    }





}