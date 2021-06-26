package com.example.insult.newDatabaseInsult

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Action

@Dao
interface NewInsultDao {

    @Query("SELECT * FROM creation_new_insult ORDER BY date DESC")
    fun getNewAll(): Flowable<MutableList<NewInsultTable>>

    @Insert
    fun insertNew(insult: NewInsultTable)

    @Delete
    fun delete(insult: NewInsultTable)

    @Query("DELETE FROM creation_new_insult")
    fun deleteAll()

    @Query("SELECT * FROM creation_new_insult WHERE text_insult LIKE :insultText")
    fun findInsult(insultText: String) : Flowable<NewInsultTable>

}