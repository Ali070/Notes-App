package com.alisamir.notesapp.ui

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.alisamir.notesapp.data.DatabaseClient
import com.alisamir.notesapp.pojo.Note
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class newNoteViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
    fun insertNote(note: Note){
        Log.e("TAG", note.toString() )
        DatabaseClient.getInstance(context)?.notesDao()?.InsertNote(note)
            ?.subscribeOn(Schedulers.computation())
            ?.subscribe(object: CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                }

                override fun onError(e: Throwable) {
                    Log.d("TAG", "Error "+e.message);
                }

            })

    }
}