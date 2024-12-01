package com.example.mynotes.mynotes.mynotes.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import com.example.mynotes.mynotes.mynotes.akh.R

import com.example.mynotes.mynotes.mynotes.akh.databinding.ActivityAddNotesBinding
import com.example.mynotes.mynotes.mynotes.data.local.DBHelper
import com.example.mynotes.mynotes.mynotes.data.dao.NotesDao
import com.example.mynotes.mynotes.mynotes.data.dao.model.DBNotesModel

@Suppress("NAME_SHADOWING")
class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding



    private var fontSize = 16f
    private val maxFontSize = 32f
    private val minFontSize = 16f
    private val textHistory = ArrayDeque<String>()
    private var isTextChanged = false
    private val handler = Handler(Looper.getMainLooper())




    private val saveTextRunnable = Runnable {
        if (isTextChanged) {
            textHistory.addLast(binding.edtDetail.text.toString())
            isTextChanged = false
        }
    }



    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val EditText: EditText = binding.edtDetail
        val changeFontButton: AppCompatImageView = binding.imagCheangeSize
        val undoButton: ImageView = binding.imgUndo

        textHistory.addLast(binding.edtDetail.text.toString())





        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)

            val heightDiff = binding.root.height - rect.height() // محاسبه ارتفاع کیبورد

            if (heightDiff > 200) { // اگر کیبورد باز شده باشد
                binding.btnSave.translationY = -heightDiff.toFloat() // دکمه را به سمت بالا ببریم
            } else {
                binding.btnSave.translationY = 0f // به موقعیت اولیه برگردانیم
            }
        }








        binding.edtDetail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                isTextChanged = true
                handler.removeCallbacks(saveTextRunnable)
                handler.postDelayed(saveTextRunnable, 500)
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.imgUndo.setOnClickListener {
            if (textHistory.size > 1) {
                textHistory.removeLast() // حذف آخرین وضعیت
                binding.edtDetail.setText(textHistory.last()) // بازگرداندن به وضعیت قبلی
                binding.edtDetail.setSelection(binding.edtDetail.text!!.length) // پیمایش به انتهای متن
            }
        }

        binding.imagCheangeSize.setOnClickListener {
            if (fontSize < maxFontSize) {
                fontSize += 4f // افزایش اندازه فونت
            } else {
                fontSize = minFontSize // برگشت به اندازه فونت اولیه
            }
            binding.edtDetail.textSize = fontSize // تغییر اندازه فونت
        }

        val type = intent.getBooleanExtra("newNotes", false)
        val id = intent.getIntExtra("notesId", 0)
        val dao = NotesDao(DBHelper(this))

        if (!type) {
            val notes = dao.getNotesById(id)
            val edit = Editable.Factory()
            binding.edtTitle.text = edit.newEditable(notes.title)
            binding.edtDetail.text = edit.newEditable(notes.detail)
        }

        binding.exit.setOnClickListener {
            val title = binding.edtTitle.text.toString().trim() // استخراج متن ورودی
            val detail = binding.edtDetail.text.toString().trim()

            if (binding.edtDetail.text.isNullOrEmpty() && binding.edtTitle.text.isNullOrEmpty()) {
                finish()
            } else {
                if (title.isNotEmpty()) {
                    val notes = DBNotesModel(0, title, detail, DBHelper.FALSE_STATE)
                    val result = if (type) {
                        dao.saveNotes(notes)
                    } else {
                        dao.editNotes(id, notes)
                    }
                    if (result) {
                        finish()
                    } else {
                        showText("خطا در ذخیره سازی یادداشت")
                    }
                } else {
                    finish()
                }
            }
        }


        binding.btnSave.setOnClickListener {
            val title = binding.edtTitle.text.toString().trim() // استخراج متن ورودی
            val detail = binding.edtDetail.text.toString().trim()

            if (title.isNotEmpty()) {
                val notes = DBNotesModel(0, title, detail, DBHelper.FALSE_STATE)
                val result = if (type) {
                    dao.saveNotes(notes)
                } else {
                    dao.editNotes(id, notes)
                }
                if (result) {
                    showText("Note saved.")
                    finish()
                } else {
                    showText("خطا در ذخیره سازی یادداشت") // * پیام برای خطا در ذخیره سازی
                }
            } else {
                showText("Title can't be empty") // * پیام جدید برای عنوان خالی
            }
        }//////صصصصصصصصصص
    }

    private fun showSaveChangesDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("ذخیره تغییرات")
        dialogBuilder.setMessage("آیا می‌خواهید تغییرات را ذخیره کنید؟")

        dialogBuilder.setPositiveButton("بله") { dialog, _ ->
            saveChanges() // * اضافه شده برای ذخیره تغییرات
            dialog.dismiss()
            finish()
        }

        dialogBuilder.setNegativeButton("خیر") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        dialogBuilder.setNeutralButton("انصراف") { dialog, _ ->
            dialog.dismiss()
        }

        // نمایش دیالوگ
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun saveChanges() { // * اضافه شده
        val title = binding.edtTitle.text.toString()
        val detail = binding.edtDetail.text.toString()
        val dao = NotesDao(DBHelper(this))
        val id = intent.getIntExtra("notesId", 0)

        if (title.isNotEmpty()) { // * اضافه شده برای بررسی عنوان
            val notes = DBNotesModel(0, title, detail, DBHelper.FALSE_STATE)
            if (intent.getBooleanExtra("newNotes", false)) {
                dao.saveNotes(notes)
            } else {
                dao.editNotes(id, notes)
            }
        } else {
            showText("عنوان نمیتواند خالی بماند") // * پیام جدید برای عنوان خالی
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(saveTextRunnable)
    }

    private fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun View.closeKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

