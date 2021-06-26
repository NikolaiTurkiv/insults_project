package com.example.insult

import android.content.Intent
import android.os.Bundle

import android.view.Menu

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.insult.newDatabaseInsult.NewDatabaseInsult

import com.example.insult.newDatabaseInsult.NewInsultTable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable

import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_show_insults.*
import kotlinx.android.synthetic.main.app_bar_show_insult.*


class ShowInsults : AppCompatActivity() {

    private lateinit var newDatabase: NewDatabaseInsult
    private lateinit var adapter: InsultAdapter
    private lateinit var mutableList: MutableList<NewInsultTable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_insults)

        setSupportActionBar(top_App_Bar_To_Show_Insult)

        val recyclerView = recyclerViewShowInsult
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = InsultAdapter(this)
        recyclerView.adapter = adapter

        newDatabase = NewDatabaseInsult.getInstanceNewDataBaseInsult(applicationContext)
        getDatabase(newDatabase)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteItem(viewHolder)
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        top_App_Bar_To_Show_Insult.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_show_insult, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addYourInsultForShowInsult -> createAlertDialogForAddInsult(this, newDatabase)
        }
        return true
    }

    private fun getDatabase(newDatabase: NewDatabaseInsult) {
        newDatabase.newInsultDao().getNewAll().observeOn(AndroidSchedulers.mainThread()).subscribe({
            mutableList = it
            adapter.insultList = it
        }, {

        })
    }

    fun deleteItem(viewHolder: RecyclerView.ViewHolder) {
        val adapterPosition = viewHolder.adapterPosition
        Completable.fromAction(Action {
            newDatabase.newInsultDao().delete(adapter.insultList[adapterPosition])
        })
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread())
            .subscribe()
        adapter.notifyDataSetChanged()
    }
}

