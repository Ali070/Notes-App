package com.alisamir.notesapp.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alisamir.notesapp.data.DatabaseClient
import com.alisamir.notesapp.pojo.Note
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val _myData:MutableLiveData<List<Note>> = MutableLiveData()
    val myData:LiveData<List<Note>>
    get() = _myData
    private val context = application
    fun getNotes(){
          DatabaseClient.getInstance(context)?.notesDao()?.getNotes()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())?.subscribe(object : SingleObserver<List<Note>>{
                  override fun onSubscribe(d: Disposable) {
                  }

                  override fun onSuccess(t: List<Note>) {
                      Log.d("TAG", "onSuccess: $t")
                      _myData.value = t

                  }

                  override fun onError(e: Throwable) {
                      Log.e("TAG", "$e")
                  }


              })
    }
    fun deleteNotes(notesIDS:ArrayList<Int>){
        DatabaseClient.getInstance(context)?.notesDao()?.deleteNote(notesIDS)?.subscribeOn(Schedulers.computation())?.subscribe(object : CompletableObserver{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onComplete() {
                getNotes()
            }

            override fun onError(e: Throwable) {
            }

        })

    }
    fun updateNote(note: Note){
        DatabaseClient.getInstance(context)?.notesDao()?.updateNote(note)
            ?.subscribeOn(Schedulers.computation())?.subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                }

            })
    }
}