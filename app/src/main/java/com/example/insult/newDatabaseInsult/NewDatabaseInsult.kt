package com.example.insult.newDatabaseInsult

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [NewInsultTable::class],version = 1,exportSchema = false)
abstract class NewDatabaseInsult : RoomDatabase() {

   abstract fun newInsultDao() : NewInsultDao

    companion object{
        @Volatile
        private var INSTANCE : NewDatabaseInsult? = null
        private val LOCK : Any = Any()

        fun getInstanceNewDataBaseInsult(context: Context): NewDatabaseInsult {
            return INSTANCE ?:  synchronized(LOCK){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewDatabaseInsult::class.java,
                    "new_creation_insults"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}