package com.example.mynotes.mynotes.mynotes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.mynotes.mynotes.adapter.recycler.RecyclerBinAdapter
import com.example.mynotes.mynotes.mynotes.akh.R
import com.example.mynotes.mynotes.mynotes.akh.databinding.ActivityRecyclerBinBinding
import com.example.mynotes.mynotes.mynotes.data.dao.NotesDao
import com.example.mynotes.mynotes.mynotes.data.dao.model.RecyclerNotesModel
import com.example.mynotes.mynotes.mynotes.data.local.DBHelper

class RecyclerBinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerBinBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: RecyclerBinAdapter
    private lateinit var alldata: ArrayList<RecyclerNotesModel> // ** اضافه کردن متغیر alldata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.exit2.setOnClickListener {

            finish()
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        // Initialize alldata
        alldata = ArrayList() // ** اضافه کردن مقداردهی اولیه به alldata

        // Initialize dao
        dao = NotesDao(DBHelper(this))

        // Fetch data from dao and set to alldata
        alldata.addAll(dao.getNotesForRecycler(DBHelper.TRUE_STATE)) // ** دریافت داده‌ها از dao و اضافه کردن به alldata

        // Initialize adapter with alldata
        adapter = RecyclerBinAdapter(this, alldata) // ** استفاده از alldata برای سازنده‌ی adapter

        // Set up RecyclerView
        binding.RecyclerNotesBIin.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.RecyclerNotesBIin.adapter = adapter

        binding.btnDeleteAll.setOnClickListener {

            if (alldata.isNotEmpty()){

                AlertDialog.Builder(this)
                    .setTitle("Delete All Notes")
                    .setMessage("Do you want to delete all notes permanently?")
                    .setNegativeButton("Yes") { dialog, _ ->
                        // Delete all trashed notes
                        val result = dao.deleteAllTrashedNotes()
                        if (result) {
                            Log.d("DeleteNotes", "All trashed notes successfully deleted.")
                            alldata.clear() // ** پاک کردن تمام داده‌ها از alldata
                            adapter.notifyDataSetChanged() // ** اطلاع رسانی به adapter برای به‌روزرسانی
                        } else {
                            Log.e("DeleteNotes", "Failed to delete trashed notes.")
                        }
                    }
                    .setPositiveButton("No") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()

            }else {
                Toast.makeText(this, "Is Empthy", Toast.LENGTH_SHORT).show()

            }

        }
    }
}

