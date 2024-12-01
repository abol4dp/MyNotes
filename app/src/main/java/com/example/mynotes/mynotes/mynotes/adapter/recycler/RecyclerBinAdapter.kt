package com.example.mynotes.mynotes.mynotes.adapter.recycler

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.mynotes.mynotes.akh.databinding.ListItemRecyclerBinBinding
import com.example.mynotes.mynotes.mynotes.data.dao.NotesDao
import com.example.mynotes.mynotes.mynotes.data.dao.model.RecyclerNotesModel
import com.example.mynotes.mynotes.mynotes.data.local.DBHelper


class RecyclerBinAdapter(
    private val context: Context,
    private val data: ArrayList<RecyclerNotesModel> // ** اصلاح نوع داده به ArrayList
) : RecyclerView.Adapter<RecyclerBinAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(
            ListItemRecyclerBinBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setData(data[position])
    }

    inner class RecyclerViewHolder(
        private val binding: ListItemRecyclerBinBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: RecyclerNotesModel) {
            binding.txtTitleNotes.text = data.title

            binding.imgDelete.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Delete Note")
                    .setMessage("Do you want to delete the note forever?")
                    .setNegativeButton("Yes") { dialog, _ ->
                        val result = NotesDao(DBHelper(context)).deleteNotes(data.id)
                        if (result) {

                            this@RecyclerBinAdapter.data.removeAt(adapterPosition) // ** استفاده از ArrayList
                            notifyItemRemoved(adapterPosition)
                        } else {

                        }
                    }
                    .setPositiveButton("No") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }

            binding.imaRestore.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Return the note")
                    .setMessage("Do you want to restore the note?")
                    .setNegativeButton("Yes") { dialog, _ ->
                        val result = NotesDao(DBHelper(context)).editNotes(data.id, DBHelper.FALSE_STATE)
                        if (result) {
                            showText("The note has been restored")
                            this@RecyclerBinAdapter.data.removeAt(adapterPosition) // ** استفاده از ArrayList
                            notifyItemRemoved(adapterPosition)
                        } else {

                        }
                    }
                    .setPositiveButton("NO") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }
    }

    private fun showText(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

