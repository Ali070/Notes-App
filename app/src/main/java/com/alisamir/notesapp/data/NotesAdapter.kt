package com.alisamir.notesapp.data

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alisamir.notesapp.R
import com.alisamir.notesapp.databinding.NoteItemBinding
import com.alisamir.notesapp.pojo.Note
import com.alisamir.notesapp.ui.HomeViewModel
import org.ocpsoft.prettytime.PrettyTime
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesAdapter(val notesList: ArrayList<Note>, private val myViewModel: HomeViewModel, val searchView: SearchView): RecyclerView.Adapter<NotesAdapter.myViewHolder>(),Filterable {
    lateinit var listener:onItemClickListener
    var mActionMode: ActionMode? = null
    var isEnable = false
    var isSelectedAll = false
    val selectedlist = ArrayList<Note>()
    private val fullNotesList = ArrayList<Note>(notesList)
    private var context:Context? = null
    private var isSearch = false
    private val actionModeCallback = object :ActionMode.Callback{
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            searchView.visibility = View.GONE
            p0?.menuInflater?.inflate(R.menu.actionmode_menu,p1)
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            isEnable = true
            notifyDataSetChanged()
            return true
        }

        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
            return when(p1?.itemId){
                R.id.delete -> {
                    if(selectedlist.size !=0) {
                        checkDelete(p0)
                    }
                    true
                }
                R.id.selectAll ->{
                    if(!isSelectedAll){
                        isSelectedAll = true
                        if(selectedlist.size != 0)
                            selectedlist.clear()
                        selectedlist.addAll(notesList)
                        Log.e("TAG", "selectAll: $notesList")
                    }else{
                        isSelectedAll = false
                        selectedlist.clear()
                    }
                    notifyDataSetChanged()
                    true
                }
                else-> {
                    false
                }
            }
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            mActionMode = null
            isEnable = false
            isSelectedAll = false
            searchView.visibility = View.VISIBLE
            selectedlist.clear()
            notifyDataSetChanged()
            Log.d("TAG", "onDestroyActionMode: ")
        }

    }
    fun setOnItemClickListener(l:onItemClickListener){
        listener = l
    }
    interface onItemClickListener{
        fun onClick(note: Note)
    }
    class myViewHolder(val binding: NoteItemBinding, val listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, mActionMode: ActionMode?,actionCallback:ActionMode.Callback,isEnable:Boolean,selected:ArrayList<Note>,selectAll:Boolean,isSearch:Boolean) {
            Log.d("TAG", "binding ")
            binding.titleTv.text = note.Title
            val prettyTime = PrettyTime()
            val time = prettyTime.format(Date(note.Date))
            val calendar = Calendar.getInstance()
            val difference = calendar.timeInMillis - note.Date
            binding.dateTv.text = time
            /*if(difference < 60000){
                binding.dateTv.text = "Just now"
            }else if (difference in 60000..3600000){
                binding.dateTv.text = "${(difference/60000).toString()} Minute"
            }else if (difference in 3600000..86400000){
                binding.dateTv.text = "${(difference/3600000).toString()} Hour"
            }else{
                binding.dateTv.text = note.Day
            }*/

            if(selectAll){
                binding.checkLayout.visibility = View.VISIBLE
                binding.checkBox.isChecked = true
                binding.itemBack.setBackgroundResource(R.drawable.checked_back)
            }else {
                if (selected.size == 0){
                    binding.checkBox.isChecked = false
                    binding.itemBack.setBackgroundResource(R.drawable.note_item_back)
                }
                if(isEnable){
                    binding.checkLayout.visibility = View.VISIBLE
                }else{
                    binding.itemBack.setBackgroundResource(R.drawable.note_item_back)
                    binding.checkLayout.visibility = View.GONE
                }
            }
            binding.root.setOnClickListener {
                val inanimation = AnimationUtils.loadAnimation(it.context,R.anim.in_anim)
                val outanimation = AnimationUtils.loadAnimation(it.context,R.anim.out_anim)
                val animationSet = AnimationSet(true)

                animationSet.addAnimation(inanimation)
                animationSet.addAnimation(outanimation)
                if(!isEnable){
                   val position = adapterPosition
                   listener.onClick(note)
               }else{
                   if(binding.checkBox.isChecked){

                       binding.checkBox.isChecked = false
                       selected.remove(note)
                       Log.e("TAG", "false: $selected" )
                       Log.e("TAG", "false" )
                       mActionMode?.title = it.context.getString(R.string.actionTitle,selected.size)
                       binding.itemBack.setBackgroundResource(R.drawable.note_item_back)
                   }else{
                       inanimation.setAnimationListener(object: Animation.AnimationListener{
                           override fun onAnimationStart(p0: Animation?) {

                           }

                           override fun onAnimationEnd(p0: Animation?) {
                               it.startAnimation(outanimation)
                           }

                           override fun onAnimationRepeat(p0: Animation?) {
                           }

                       })

                       it.startAnimation(inanimation)
                       binding.checkBox.isChecked = true
                       selected.add(note)
                       mActionMode?.title = it.context.getString(R.string.actionTitle,selected.size)
                       Log.e("TAG", "true: $selected" )
                       Log.e("TAG", "true" )
                       binding.itemBack.setBackgroundResource(R.drawable.checked_back)
                   }

               }
            }
            binding.checkBox.setOnClickListener {
                if(binding.checkBox.isChecked){
                    selected.add(note)
                    mActionMode?.title = it.context.getString(R.string.actionTitle,selected.size)
                    Log.e("TAG", "true: $selected" )
                    Log.e("TAG", "true" )
                    binding.itemBack.setBackgroundResource(R.drawable.checked_back)
                }else{
                    selected.remove(note)
                    mActionMode?.title = it.context.getString(R.string.actionTitle,selected.size)
                    Log.e("TAG", "false: $selected" )
                    Log.e("TAG", "false" )
                    binding.itemBack.setBackgroundResource(R.drawable.note_item_back)

                }
            }
            binding.root.setOnLongClickListener {
                if(isSearch){
                    return@setOnLongClickListener false
                }
                var tempAction = mActionMode
                if(tempAction!=null){
                    Log.d("TAG", "bind: is enabled")
                    return@setOnLongClickListener false
                }
                tempAction = (it.context as AppCompatActivity).startActionMode(actionCallback)
                binding.checkBox.isChecked = true
                binding.itemBack.setBackgroundResource(R.drawable.checked_back)
                selected.add(note)
                Log.e("TAG", "onLong: $selected" )
                return@setOnLongClickListener true
            }
        }
    }
    private fun checkDelete(p0:ActionMode?){
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.delete_alert)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(0))
        dialog.window?.setWindowAnimations(R.style.dialog)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.show()
        val deleteBtn = dialog.findViewById<Button>(R.id.deleteBtn)
        val noBtn = dialog.findViewById<Button>(R.id.noBtn)
        noBtn.setOnClickListener {
            dialog.cancel()
        }
        deleteBtn.setOnClickListener {
            val idList = ArrayList<Int>()
            selectedlist.forEach {
                idList.add(it.id)
            }
            myViewModel.deleteNotes(idList)
            notesList.removeAll(selectedlist)
            dialog.cancel()
            p0?.finish()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.myViewHolder {
        context = parent.context
       return myViewHolder(NoteItemBinding.inflate(LayoutInflater.from(parent.context),parent,false),listener)
    }

    override fun onBindViewHolder(holder: NotesAdapter.myViewHolder, position: Int) {
        holder.bind(notesList.get(position),mActionMode,actionModeCallback,isEnable,selectedlist,isSelectedAll,isSearch)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun getFilter(): Filter {
        return notesFilter
    }
    private val notesFilter = object:Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val tempList = ArrayList<Note>()
            if (p0==null || p0.isEmpty()){
                isSearch = false
                tempList.addAll(fullNotesList)
            }else{
                isSearch = true
                val filterPattern = p0.toString().lowercase(Locale.getDefault()).trim()
                fullNotesList.forEach {
                    if(it.Title.lowercase(Locale.getDefault()).trim().contains(filterPattern)){
                        tempList.add(it)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = tempList
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notesList.clear()
            notesList.addAll(p1?.values as List<Note>)
            notifyDataSetChanged()
        }


    }
}