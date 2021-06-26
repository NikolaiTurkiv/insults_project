package com.example.insult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.insult.newDatabaseInsult.NewDatabaseInsult
import com.example.insult.newDatabaseInsult.NewInsultTable
import com.example.insult.pojo.Insult
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var newDatabase: NewDatabaseInsult
    private lateinit var insultToCheck: NewInsultTable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
        newDatabase = NewDatabaseInsult.getInstanceNewDataBaseInsult(applicationContext)
        insultToCheck = NewInsultTable("textForStart")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun setLocal(locale : String){
        val res = resources.displayMetrics
        val conf = resources.configuration
        conf.setLocale(Locale(locale))
        resources.updateConfiguration(conf,res)
    }

    override fun onStart() {
        super.onStart()
        buttonGetInsult.setOnClickListener {
            if (switchLang.isChecked){
                getDataFromRetrofit(ruLang)
                setLocal(ruLang)
            }  else{
                getDataFromRetrofit(enLang)
            setLocal(enLang)}
        }

        FAB.setOnClickListener {
            addInsultInDb(insultToCheck.textInsult, textViewInsult.text.toString(), it, newDatabase)
        }

        bottom_navigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.itemSendInsult -> {
                    createAlertDialogSendToServer(this)
                }
                R.id.itemAddInsultInBottomMenu -> {
                    createAlertDialogForAddInsult(this, newDatabase)
                }
                R.id.itemShowInsult -> {
                    startActivity(Intent(this, ShowInsults::class.java))
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.showInsults -> startActivity(Intent(this, ShowInsults::class.java))
            R.id.addYourInsult -> createAlertDialogForAddInsult(this, newDatabase)
        }
        return true
    }

    private fun getDataFromRetrofit(lang: String) {
        val call = createRetrofit(BASE_URL).getInsult(lang)
        call.enqueue(object : Callback<Insult> {
            override fun onResponse(call: Call<Insult>, response: Response<Insult>) {
                if (response.isSuccessful && lang == ruLang) {
                    val responseInsult = response.body()

                    textViewInsult.text = responseInsult?.insult.toString()
                    setRussianFields(
                        responseInsult?.number.toString(),
                        responseInsult?.shown.toString()
                    )
                    findInsult(textViewInsult.text.toString())
                } else if (response.isSuccessful && lang == enLang) {
                    val responseInsult = response.body()
                    textViewInsult.text = responseInsult?.insult.toString()
                    setEnglishFields(
                        responseInsult?.number.toString(),
                        responseInsult?.shown.toString()
                    )
                    findInsult(textViewInsult.text.toString())
                }
            }

            override fun onFailure(call: Call<Insult>, t: Throwable) {
            }
        })
    }

    fun findInsult(textInsult: String) {
        newDatabase.newInsultDao().findInsult(textInsult).observeOn(Schedulers.newThread())
            .subscribe({
                insultToCheck = it
            }, {

            }
            )
    }
    companion object {
        const val BASE_URL = "https://evilinsult.com/"
        const val ruLang = "ru"
        const val enLang = "en"
    }
}


