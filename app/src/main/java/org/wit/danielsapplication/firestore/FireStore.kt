package org.wit.danielsapplication.firestore
import android.media.RoutingSessionInfo
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.wit.danielsapplication.activities.AddSessionActivity
import org.wit.danielsapplication.activities.LoginActivity
import org.wit.danielsapplication.activities.RegisterActivity
import org.wit.danielsapplication.models.Session
import org.wit.danielsapplication.models.User

class FireStore {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        // This where we pull the specific collection path
        // Referenced Link down below
        // https://firebase.google.com/docs/storage/android/upload-files?authuser=0
        mFireStore.collection("users")
        //uid created from
            .document(userInfo.id)
            //.set method where we merge userinfo with a method cal
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Method we made in register activity prints success
                activity.userRegisterSuccess()
            }
            .addOnFailureListener{
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user"
                )

            }
    }
    fun getUserID() : String {
        // get current user function which will get u the current user
        val currentUser = FirebaseAuth.getInstance().currentUser

        var CurrentUserID = "" //making simple CurrentUserID variable to store Uid
        // Checking if currentuser is not empty
        if(currentUser != null) {
            //storing currentuser ID
            CurrentUserID = currentUser.uid
        }
        // Pretty much getting the UId specifically from the Auth module
        return CurrentUserID
    }

    fun getUserDetails(activity: android.app.Activity) {
        // here we pass the collection name from which we wants the data
        mFireStore.collection("users")
            // the document id to get the Fields of user
            .document(getUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it
                        activity.LogInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
    fun UploadSessionDetails(activity: AddSessionActivity, sessionInfo: Session){
        mFireStore.collection("sessions")
            .document()
            .set(sessionInfo, SetOptions.merge())
            .addOnSuccessListener {
                //Success method from session activity -- Simple print and log
            activity.UploadSuccess()
            }
            .addOnFailureListener{ e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the session details.",
                    e
                )
            }
    }

}