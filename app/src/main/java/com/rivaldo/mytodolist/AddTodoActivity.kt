package com.rivaldo.mytodolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_todo.*

class AddTodoActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_ID = "Extra_Id"
        const val EXTRA_JUDUL = "Extra_Judul"
        const val EXTRA_DESKRIPSI = "Extra_Deskripsi"
        const val EXTRA_PRIORITAS = "Extra_Prioritas"
        const val EXTRA_DONE = "Extra_Done"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 5
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Catatan"
            editTextTitle.setText(intent.getStringExtra(EXTRA_JUDUL))
            editTextDeskripsi.setText(intent.getStringExtra(EXTRA_DESKRIPSI))
            number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITAS, 1)
        } else {
            title = "Tambah Catatan"
        }

        btnSimpan.setOnClickListener {
            saveTodo()
        }

    }
    private fun saveTodo() {
        var data: Intent? = null
        if (editTextTitle.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Judul Todo List Harus Diisi ! ", Toast.LENGTH_SHORT).show()
            return
        } else {
            data = Intent().apply {
                putExtra(EXTRA_JUDUL, editTextTitle.text.toString())
                putExtra(EXTRA_DESKRIPSI, editTextDeskripsi.text.toString())
                putExtra(EXTRA_PRIORITAS, number_picker_priority.value)
                if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
                    putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
                }
            }

        }
        if (data == null){
            setResult(Activity.RESULT_CANCELED)
            Toast.makeText(this, "Aksi Dibatalkan", Toast.LENGTH_SHORT).show()
            finish()
        } else{
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }
}