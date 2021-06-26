package com.example.insult

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.insult.api.ApiInsultServes
import com.example.insult.newDatabaseInsult.NewDatabaseInsult
import com.example.insult.newDatabaseInsult.NewInsultTable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun AppCompatActivity.setRussianFields(number: String, count: String) {
    textViewNumberOfInsult.text = "Номер оскорбления:${number} "
    textViewShow.text = "Это оскорбление видели: ${count} человека"
    buttonGetInsult.text = "НАЖИМАЙ"
    textViewPressTheButton.text = "жмякай на синюю кнопку"
}

fun AppCompatActivity.setEnglishFields(number: String, count: String) {
    textViewNumberOfInsult.text = "Insult number:${number} "
    textViewShow.text = "This insult saw: ${count} man"
    buttonGetInsult.text = "PRESS IT"
    textViewPressTheButton.text = "press to the blue button"
}

fun snackBar(textForSnackBar: String, view: View) {
    Snackbar.make(view, "$textForSnackBar", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
}

fun createAlertDialogForAddInsult(context: Context, newDatabase: NewDatabaseInsult) {
    val layoutInflater = LayoutInflater.from(context)
    val view = layoutInflater.inflate(R.layout.add_my_insult, null)

    val dialogBuilder = MaterialAlertDialogBuilder(context)
    dialogBuilder.setView(view)

    val userInput = view.findViewById<EditText>(R.id.editText_to_add_new_insult)

    dialogBuilder.setCancelable(false)
        .setPositiveButton(R.string.send, DialogInterface.OnClickListener { dialog, id ->
            insertInsult(userInput.text.toString(), newDatabase)
            Toast.makeText(context, "The insult was added in database", Toast.LENGTH_SHORT).show()
        })
        .setNegativeButton( R.string.cancle , DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })

    dialogBuilder.create()
    dialogBuilder.show()
}

fun createAlertDialogSendToServer(context: Context) {
    val layoutInflater = LayoutInflater.from(context)
    val view = layoutInflater.inflate(R.layout.send_insult_to_server, null)

    val dialogBuilder = MaterialAlertDialogBuilder(context)
    dialogBuilder.setView(view)

    val userInput = view.findViewById<EditText>(R.id.editText_to_send_insult)

    dialogBuilder.setCancelable(false)
        .setPositiveButton(R.string.send) { dialog, id ->
            val s = arrayOf("nikolaiturkiv@mail.ru")

            val email = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_EMAIL, s)
                putExtra(Intent.EXTRA_SUBJECT, "Evil Insult Generator Proposal")
                putExtra(Intent.EXTRA_TEXT, userInput.text.toString())
            }
            startActivity(context, email, Bundle())
        }
        .setNegativeButton(R.string.cancle) { dialog, id ->
            dialog.cancel()
        }
    dialogBuilder.create()
    dialogBuilder.show()
}


fun insertInsult(insultText: String, newDatabase: NewDatabaseInsult) {
    val value = NewInsultTable(insultText)
    Observable.just(value)
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.newThread())
        .subscribe({
            newDatabase.newInsultDao().insertNew(value)
        }, {
            Log.e("ROOM ERROR", it.message.toString())
        }, {

        })
}

fun addInsultInDb(
    firstStringToCheck: String,
    secondStringToCheck: String,
    view: View,
    newDatabase: NewDatabaseInsult
) {
    if (firstStringToCheck.equals(secondStringToCheck)) {
        snackBar("INSULT WAS ADDED BEFORE", view)
    } else {
        insertInsult(secondStringToCheck, newDatabase)
        snackBar("INSULT ADDED IN DATABASE", view)
    }
}


fun createRetrofit(BASE_URL: String): ApiInsultServes {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiInsultServes::class.java)
}




