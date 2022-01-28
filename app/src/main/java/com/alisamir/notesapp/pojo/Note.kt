package com.alisamir.notesapp.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "Title")
    val Title:String,
    @ColumnInfo(name = "Description")
    val Description:String,
    @ColumnInfo(name = "Date")
    val Date:Long
)
