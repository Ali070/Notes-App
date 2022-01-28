package com.alisamir.notesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alisamir.notesapp.R
import com.alisamir.notesapp.databinding.FragmentNewNoteBinding
import com.alisamir.notesapp.databinding.FragmentNoteBinding
import com.alisamir.notesapp.pojo.Note
import java.util.*

class NoteFragment : Fragment() {
    lateinit var binding: FragmentNoteBinding
    var title: String? = null
    var description: String? = null
    private val myViewModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString("note_title")
            description = it.getString("note_desc")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteFragment = this

    }
    fun save(){
        if(title!=null && description!=null) {
            myViewModel.updateNote(
                Note(
                    0,
                    title!!,
                    description!!,
                    Calendar.getInstance().timeInMillis
                )
            )
            Log.d("TAG", "save: ")
        }
        findNavController().navigateUp()
    }


}