package org.wit.danielsapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*
import org.jetbrains.anko.*
import org.wit.danielsapplication.Adapter.SessionListAdapter
import org.wit.danielsapplication.R
import org.wit.danielsapplication.activities.AddSessionActivity
import org.wit.danielsapplication.firestore.FireStore
import org.wit.danielsapplication.models.Session


class ListFragment : Fragment(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)

        return root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_session_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
// Add session activity, similar to the addactivity in the labs but I have assigned Id with the proper params
        when(id) {
            R.id.action_add_session -> {
                startActivity(Intent(activity, AddSessionActivity::class.java))
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun SessionFromFirestore(sessionlist: ArrayList<Session>){
// Checks if sessionlist is empty
        if(sessionlist.size > 0){
            // If the session is greater then 0 then we display the recycleview and View.GONE the no sessions text
            recyclerView.visibility = View.VISIBLE
            text_nosessions.visibility = View.GONE

            //I set a LinearLayoutManager with the acitivty
            recyclerView.layoutManager = LinearLayoutManager(activity)
            // We can set the fixed size to true
            recyclerView.setHasFixedSize(true)
            //We create adapaterSession where we pass the session list and this fragment
            val adapterSession = SessionListAdapter( sessionlist, this)
            //we assign this recyclerview to the adapter
            recyclerView.adapter = adapterSession

//We display the nosessions text and wont bother displaying the recyclerview
        } else {
            recyclerView.visibility = View.GONE
            text_nosessions.visibility = View.VISIBLE
        }
    }
// Uses Firestore method
    private fun getSessionListFromFireStore() {
        FireStore().getSessionDetails(this)
    }
// When you return to the list it updates sessions list
    override fun onResume() {
        super.onResume()
        getSessionListFromFireStore()

    }

// Deleting session method in Firestore passing sessionID + log
    fun deleteSession(sessionid: String){
        FireStore().deleteSession(this, sessionid)

        info("Session deleted: ${sessionid}")

    }

    //Delete session and updating the list + log
    fun deleteSuccess() {
        getSessionListFromFireStore()
        info("Sessions Updated")
    }

}

/* References
* https://youtu.be/qA9L3_cK9Z0
* https://youtu.be/I485b7LzYkM
* https://firebase.google.com/docs/database/android/read-and-write
* https://medium.com/google-developer-experts/firebase-met-kotlin-d1394c2f402
 */