package com.example.insult.newDatabaseInsult

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "creation_new_insult")
class NewInsultTable(

    @PrimaryKey
    @ColumnInfo(name = "text_insult") val textInsult: String,
    @ColumnInfo(name = "date") val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()
    ).format(Date())

)


