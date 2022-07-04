package com.alisamir.notesapp.ui

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alisamir.notesapp.R
import com.alisamir.notesapp.data.NotesAdapter
import com.alisamir.notesapp.databinding.FragmentHomeBinding
import com.alisamir.notesapp.pojo.Note
import kotlin.collections.ArrayList

private const val TAG = "TAG"
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private val myViewModel:HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homefragment = this
        myViewModel.getNotes()
        binding.searchView.queryHint = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             Html.fromHtml("<font color = #868B8E> ${resources.getString(R.string.search)}</font>",Html.FROM_HTML_MODE_LEGACY)
        }else{
            Html.fromHtml("<font color = #868B8E> ${resources.getString(R.string.search)}</font>")
        }
        val id = binding.searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        binding.searchView.findViewById<TextView>(id).setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        val noItemsView = binding.frameLayout2.findViewById<View>(R.id.noNotes)
        /*val calender = Calendar.getInstance()
        val formatter = SimpleDateFormat("y-m-d", Locale.getDefault())
        Log.e(TAG, "data: "+formatter.format(calender.time) )
        calender.add(Calendar.DATE,1)
        Log.e(TAG, "data2: "+calender.timeInMillis )*/
        myViewModel.myData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Observe ")
            Log.d(TAG, "onViewCreated: $it")
            if (it.isEmpty()) {
                noItemsView.visibility = View.VISIBLE
            } else {
                noItemsView.visibility = View.GONE
                val adapter = NotesAdapter(it as ArrayList<Note>, myViewModel, binding.searchView)
                binding.notesList.adapter = adapter
                binding.notesList.layoutManager = LinearLayoutManager(context)
                binding.notesList.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,R.anim.list_anim)
                binding.notesList.scheduleLayoutAnimation()
                binding.searchView.imeOptions = EditorInfo.IME_ACTION_DONE
                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        adapter.filter.filter(p0)
                        return false
                    }

                })
                adapter.setOnItemClickListener(object : NotesAdapter.onItemClickListener {
                    override fun onClick(note: Note) {
                        goToNote(note.Title, note.Description)
                    }

                })

            }

        }

    }
    fun goToNewNote(){
        findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
    }
    fun goToNote(title:String, description:String){
        val action = HomeFragmentDirections.actionHomeFragmentToNoteFragment(title,description)
        findNavController().navigate(action)
    }
}