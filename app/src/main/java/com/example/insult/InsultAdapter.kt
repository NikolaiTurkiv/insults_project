package com.example.insult

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.insult.newDatabaseInsult.NewInsultTable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.insult_item.view.*

class InsultAdapter(context: Context) : RecyclerView.Adapter<InsultAdapter.InsultViewHolder>() {

    var insultList : MutableList<NewInsultTable> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class InsultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textInsult: TextView = itemView.textViewTextInsult
        val textDate: TextView = itemView.textViewDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.insult_item,parent,false)
        return InsultViewHolder(view)
    }

    override fun onBindViewHolder(holder: InsultViewHolder, position: Int) {
        val insult = insultList[position]
        with(holder){
            textInsult.text = insult.textInsult
            textDate.text = insult.date
        }
    }

    override fun getItemCount(): Int {
       return insultList.size
    }
}