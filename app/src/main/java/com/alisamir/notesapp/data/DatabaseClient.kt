package com.alisamir.notesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alisamir.notesapp.pojo.Note

@Database(entities = [Note::class],version = 1)
abstract class DatabaseClient: RoomDatabase() {
    abstract fun notesDao():NotesDao

    companion object{
        var instance: DatabaseClient? = null
        fun getInstance(context: Context):DatabaseClient?{
            Log.e("TAG", "getInstance: One" )
            synchronized(this){
               if(instance == null){
                   instance = Room.databaseBuilder(context.applicationContext,DatabaseClient::class.java,"Notes database")
                       .fallbackToDestructiveMigration()
                       .build()
               }
                return instance
            }
        }
    }
}

