package com.rivaldo.mytodolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rivaldo.mytodolist.todo.Todo
import com.rivaldo.mytodolist.todo.TodoAdapter
import com.rivaldo.mytodolist.todo.TodoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object{
        const val ADD_TODO_REQUEST = 1
        const val EDIT_TODO_REQUEST = 2
    }
    @InternalCoroutinesApi
    private lateinit var todoViewModel: TodoViewModel

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonAddTodo.setOnClickListener {
            startActivityForResult(
            Intent(this, AddTodoActivity::class.java), ADD_TODO_REQUEST
            )
        }

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        val adapter = TodoAdapter()
        recyclerview.adapter = adapter

        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        todoViewModel.getAllTodo().observe(this, Observer<List<Todo>> {
            adapter.submitList(it)
        })
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                todoViewModel.delete(adapter.getTodoAt(viewHolder.adapterPosition))
                Toast.makeText(baseContext, "Catatan dihapus!", Toast.LENGTH_SHORT).show()
            }
            }
        ).attachToRecyclerView(recyclerview)
        adapter.setOnItemClickListener(object : TodoAdapter.OnItemClickListener {
            override fun onItemClick(todo: Todo) {
                val intent = Intent(baseContext, AddTodoActivity::class.java)
                intent.putExtra(AddTodoActivity.EXTRA_ID,todo.id )
                intent.putExtra(AddTodoActivity.EXTRA_JUDUL, todo.title)
                intent.putExtra(AddTodoActivity.EXTRA_DESKRIPSI, todo.description)
                intent.putExtra(AddTodoActivity.EXTRA_PRIORITAS, todo.priority)
                startActivityForResult(intent, EDIT_TODO_REQUEST)

            }

            override fun onItemChecked(todo: Todo) {
                todo.done = true
                todoViewModel.update(todo)
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    @InternalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId) {
            R.id.delete_all_todo -> {
                todoViewModel.deleteAllTodo()
                Toast.makeText(this, "Semua Todo Dihapus", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @InternalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TODO_REQUEST && resultCode == Activity.RESULT_OK) {
            val newTodo = Todo(
                data!!.getStringExtra(AddTodoActivity.EXTRA_JUDUL).toString(),
                data.getStringExtra(AddTodoActivity.EXTRA_DESKRIPSI).toString(),
                false,
                data.getIntExtra(AddTodoActivity.EXTRA_PRIORITAS, 1)
            )
            todoViewModel.insert(newTodo)
            Toast.makeText(this, "Catatan disimpan!", Toast.LENGTH_SHORT).show()
        }
//        logic update data database
        else if (requestCode == EDIT_TODO_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddTodoActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Pembaharuan gagal!", Toast.LENGTH_SHORT).show()
            }
            val updateNote = Todo(
                data!!.getStringExtra(AddTodoActivity.EXTRA_JUDUL).toString(),
                data.getStringExtra(AddTodoActivity.EXTRA_DESKRIPSI).toString(),
                false,
                data.getIntExtra(AddTodoActivity.EXTRA_PRIORITAS, 1)
            )
            updateNote.id = data.getIntExtra(AddTodoActivity.EXTRA_ID, -1)
            todoViewModel.update(updateNote)
        } else {
            Toast.makeText(this, "Catatan tidak disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}