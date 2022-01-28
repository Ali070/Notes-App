package com.alisamir.notesapp.data

import androidx.room.*
import com.alisamir.notesapp.pojo.Note
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NotesDao {
    @Insert
    fun InsertNote(note:Note):Completable

    @Query("select * from Note")
    fun getNotes(): Single<List<Note>>

    @Query("Delete from Note where id in (:notesIDS)")
    fun deleteNote(notesIDS: List<Int>):Completable

    @Update
    fun updateNote(note: Note):Completable
}