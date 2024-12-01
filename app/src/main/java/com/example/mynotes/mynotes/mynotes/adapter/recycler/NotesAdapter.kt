package com.example.mynotes.mynotes.mynotes.adapter.recycler

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.mynotes.mynotes.akh.databinding.ListItemRecyclerBinding
import com.example.mynotes.mynotes.mynotes.data.dao.NotesDao
import com.example.mynotes.mynotes.mynotes.data.dao.model.RecyclerNotesModel
import com.example.mynotes.mynotes.mynotes.data.local.DBHelper
import com.example.mynotes.mynotes.mynotes.ui.AddNotesActivity

class NotesAdapter(
    private val context: Context,

    private val dao: NotesDao

) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {


    private var allData: ArrayList<RecyclerNotesModel>

    init {
        allData = dao.getNotesForRecycler(DBHelper.FALSE_STATE)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotesViewHolder(
            ListItemRecyclerBinding.inflate(LayoutInflater.from(context), parent, false)

        )


    override fun getItemCount(): Int = allData.size


    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.setData(allData[position])
    }


    inner class NotesViewHolder(

        private val binding: ListItemRecyclerBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: RecyclerNotesModel) {

            binding.txtTitleNotes.text = data.title

            binding.root.setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete Note ")
                    .setMessage("Do you want to move the note to the trash? ")
                    .setNegativeButton("Yes") { dialog, _ ->

                        val result = dao.editNotes(data.id, DBHelper.TRUE_STATE)
                        if (result) {


                            allData.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        } else
                            showText("")
                    }
                    .setPositiveButton("No") { dialog, _ -> }
                    .create()
                    .show()

                true
            }

            binding.root.setOnClickListener {
                val intent = Intent(context, AddNotesActivity::class.java)
                intent.putExtra("notesId", data.id)
                context.startActivity(intent)
            }


        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(data: ArrayList<RecyclerNotesModel>) {

        allData = data

        notifyDataSetChanged()


    }

    private fun showText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


}