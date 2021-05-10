package org.wit.danielsapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.session_card.view.*
import org.wit.danielsapplication.R
import org.wit.danielsapplication.fragments.ListFragment
import org.wit.danielsapplication.models.Session


open class SessionListAdapter(
    private var list: ArrayList<Session>,
    private val fragment: ListFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            //Inflating session_card
            LayoutInflater.from(parent.context).inflate(R.layout.session_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //A new temp model that gives the position of the bindview
        val model = list[position]

        if (holder is MyViewHolder) {
            //Pretty displays the list of sessions
            holder.itemView.resource_card.text = model.resource
            holder.itemView.time_card.text = model.time
            holder.itemView.date_card.text = model.date

            //Delete method in listfragment
            holder.itemView.session_delete.setOnClickListener {
                fragment.deleteSession(model.session_id)
            }

        }
    }

    override fun getItemCount(): Int = list.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}