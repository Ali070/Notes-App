package com.alisamir.notesapp.data

import android.content.Context
import android.util.Log
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alisamir.notesapp.pojo.Note

@Database(version = 1,entities = [Note::class])
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

