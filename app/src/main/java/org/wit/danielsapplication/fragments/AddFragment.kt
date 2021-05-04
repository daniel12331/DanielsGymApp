package org.wit.danielsapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.wit.danielsapplication.R
import org.wit.danielsapplication.activities.AddSessionActivity

class AddFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add, container, false)
        val textView: TextView = root.findViewById(R.id.text_add)
        textView.text = "This is Add Fragment"


        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_session_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        when(id) {
            R.id.action_add_session -> {
                startActivity(Intent(activity, AddSessionActivity::class.java))
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }
}