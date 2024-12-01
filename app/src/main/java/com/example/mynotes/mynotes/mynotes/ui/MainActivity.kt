package com.example.mynotes.mynotes.mynotes.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.mynotes.mynotes.adapter.recycler.NotesAdapter
import com.example.mynotes.mynotes.mynotes.akh.R
import com.example.mynotes.mynotes.mynotes.akh.databinding.ActivityMainBinding
import com.example.mynotes.mynotes.mynotes.data.local.DBHelper
import com.example.mynotes.mynotes.mynotes.data.dao.NotesDao
import com.example.mynotes.mynotes.mynotes.retrofit.ApiRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: NotesAdapter
    private var isIconOne = true
    private var isBackgroundBlack = false///////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)




        binding.content.toolbar.title = ""


        val searchView = findViewById<SearchView>(R.id.search_view)

// تنظیم حالت ابتدایی SearchView به باز (expanded) و آماده برای نوشتن
        searchView.isIconified = false
        searchView.clearFocus()

// تنظیم کلیک روی کل SearchView برای فعال کردن باکس نوشتن
        searchView.setOnClickListener {
            searchView.isIconified = false // اگر به هر دلیلی SearchView بسته باشد، بازش می‌کند
            searchView.clearFocus()
            searchView.requestFocus() // تمرکز را روی باکس جستجو می‌گذارد
        }

// تنظیم تغییر رفتار کلیک بر روی دکمه ضربدر (clear button)
        val searchEditText =
            searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                searchView.isIconified = false // SearchView را باز نگه می‌دارد
            }
        }

// تنظیم listener برای جستجو
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val filteredNotes = dao.searchNotes(it, DBHelper.FALSE_STATE)
                    adapter.changeData(filteredNotes)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredNotes = dao.searchNotes(it, DBHelper.FALSE_STATE)
                    adapter.changeData(filteredNotes)
                }
                return true
            }
        })




        binding.content.fabButton.setOnClickListener {

            ApiRepository.inestance.sendText(

                "DIIAWi78OOyRzWoPkwOW6PHA0cx0OyoihdspRjSv",
                "Click"

            )

            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("newNotes", true)
            startActivity(intent)


        }


        var isAllfabvisibility = false





        initRecycler()

        setSupportActionBar(binding.content.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            binding.content.toolbar,
            R.string.open,
            R.string.close
        )

        toggle.isDrawerIndicatorEnabled = true

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {


                R.id.trash_menu -> {
                    val intent = Intent(this, RecyclerBinActivity::class.java)
                    startActivity(intent)
                    true
                }



                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val data = dao.getNotesForRecycler(DBHelper.FALSE_STATE)
        adapter.changeData(data)
    }


    private fun initRecycler() {
        dao = NotesDao(DBHelper(this))

        // ایجاد و تنظیم آداپتور
        adapter = NotesAdapter(this, dao)

        // استفاده از GridLayoutManager با 2 ستون برای RecyclerView
        val gridLayoutManager = GridLayoutManager(this, 2) // تنظیم تعداد ستون‌ها به 2
        binding.content.recyclerNotes.layoutManager = gridLayoutManager
        binding.content.recyclerNotes.adapter = adapter
    }

}



















    
