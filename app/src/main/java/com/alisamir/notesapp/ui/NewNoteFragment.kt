package com.alisamir.notesapp.ui

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alisamir.notesapp.R
import com.alisamir.notesapp.databinding.FragmentNewNoteBinding
import com.alisamir.notesapp.pojo.Note
import java.util.*


class NewNoteFragment : Fragment() {
    private lateinit var binding:FragmentNewNoteBinding
    private val myViewModel:newNoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.newNoteFragment = this
        /*val callback = object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                save()
                Log.d("TAG", "handleOnBackPressed: ")
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)*/

        binding.noteET.addTextChangedListener {
            if (it.toString().isNotEmpty()){
                binding.saveBtn.visibility = View.VISIBLE
            }else{
                binding.saveBtn.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home){
            save()
            Log.d("TAG", "home button pressed ")
            true
        }else{
            super.onOptionsItemSelected(item)
        }
    }
    private fun checkTitleAndDesc(): Note? {
        var note:Note? = null
        if(binding.noteET.text.isNotEmpty()){
            note = if(binding.titleET.text.isEmpty()){
                Note(0,"No Title",binding.noteET.text.toString(),Calendar.getInstance().timeInMillis)
            }else{
                Note(0,binding.titleET.text.toString(),binding.noteET.text.toString(),Calendar.getInstance().timeInMillis)
            }
        }
        return note

    }
     fun save(){
        val note = checkTitleAndDesc()
         if (note != null) {
             val dialog = Dialog(requireContext())
             dialog.setCancelable(false)
             dialog.setContentView(R.layout.save_alert)
             dialog.window?.setGravity(Gravity.CENTER)
             dialog.window?.setBackgroundDrawable(ColorDrawable(0))
             dialog.window?.setWindowAnimations(R.style.dialog)
             dialog.show()
             val saveBtn = dialog.findViewById<Button>(R.id.saveBtn)
             val noBtn = dialog.findViewById<Button>(R.id.noBtn)
             saveBtn.setOnClickListener {
                 myViewModel.insertNote(note)
                 dialog.cancel()
                 findNavController().navigateUp()
             }
             noBtn.setOnClickListener {
                 dialog.cancel()
             }

         }

    }





}