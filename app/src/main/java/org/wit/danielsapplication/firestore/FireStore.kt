package org.wit.danielsapplication.firestore

import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

import org.wit.danielsapplication.activities.AddSessionActivity
import org.wit.danielsapplication.activities.LoginActivity
import org.wit.danielsapplication.activities.RegisterActivity
import org.wit.danielsapplication.fragments.ListFragment
import org.wit.danielsapplication.models.Session
import org.wit.danielsapplication.models.User


class FireStore : AnkoLogger {

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
            // gets doucments with userid
            .document(getUserID())
            .get()

            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                when (activity) {
                    is LoginActivity -> {
                        // Log in Success method in listfragment
                        activity.LogInSuccess(user)
                    }
                }
            }
                //Logging failure to get user details
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
                //Creating the document
            .document()
                // We set a the model in the document and we can use SetOptions to merge it into exisiting data
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
        //https://firebase.google.com/docs/database/android/read-and-write
    }

    fun getSessionDetails(fragment: Fragment){
        mFireStore.collection("sessions")
                //method where we can equal the current ID signed in and match it with sessions that they have saved (we get the UID not the "users" pathway)
            .whereEqualTo("userid", getUserID())
            .get()
                // With document we can get the document of the sessions that user id fits current user id
            .addOnSuccessListener { document ->
                // Creaitng a arraylist to store multiple sessions that one user might have
            val sessionlist: ArrayList<Session> = ArrayList()
                //  https://firebase.google.com/docs/firestore/query-data/get-data


                for(i in document.documents){

                    // Whatever "i" inside of documents we make a session out of by using our session model
                    val session = i.toObject(Session::class.java)
                    // We assignment an id from the collection to session_id
                    session!!.session_id = i.id

                    //Adding to array list created above
                    sessionlist.add(session)
                }

                //We run code specifically based on the specific fragment in this case ListFragment
                when(fragment){
                    is ListFragment ->{
                        // Passing our list through the method below which will print the list on the recyclerview
                    fragment.SessionFromFirestore(sessionlist)
                    }
                }
            }
    }

    fun deleteSession(fragment: ListFragment, sessionID: String){
        mFireStore.collection("sessions")
            .document(sessionID)
            .delete()
            .addOnSuccessListener {
                fragment.deleteSuccess()
            }
            .addOnFailureListener{
                info ("Failure to delete session")
            }

    }

}