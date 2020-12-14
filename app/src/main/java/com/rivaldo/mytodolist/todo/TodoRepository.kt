package com.rivaldo.mytodolist.todo

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class TodoRepository(application: Application) {
    private var todoDao: TodoDao
    private var allTodo: LiveData<List<Todo>>

    init {
        val database: TodoDatabase = TodoDatabase.getInstance(
            application.applicationContext
        )!!
        todoDao = database.todoDao()
        allTodo = todoDao.getAllTodo()
    }

    fun insert(todo: Todo) {
        val insertNoteAsyncTask = InsertTodoAsyncTask(todoDao).execute(todo)
    }
    fun update(todo: Todo){
        val updateTodoAsyncTask = UpdateTodoAsyncTask(todoDao).execute(todo)
    }
    fun delete(todo: Todo) {
        val deleteTodoAsyncTask = DeleteTodoAsyncTask(todoDao).execute(todo)
    }
    fun deleteAllTodo() {
        val deleteAllTodoAsyncTask = DeleteAllTodoAsyncTask(todoDao).execute()
    }
    fun getAllTodo(): LiveData<List<Todo>> {
        return  allTodo
    }

    companion object{
        private class InsertTodoAsyncTask(todoDao: TodoDao) : AsyncTask<Todo, Unit, Unit>(){
            val todoDao = todoDao
            override fun doInBackground(vararg params: Todo?) {
                todoDao.insert(params[0]!!)
            }
        }
        private class UpdateTodoAsyncTask(todoDao: TodoDao) : AsyncTask<Todo, Unit, Unit>() {
            val todoDao = todoDao
            override fun doInBackground(vararg params: Todo?) {
                todoDao.update(params[0]!!)
            }
        }
        private class DeleteTodoAsyncTask(todoDao: TodoDao) : AsyncTask<Todo, Unit,Unit>() {
            val todoDao = todoDao
            override fun doInBackground(vararg params: Todo?) {
                todoDao.delete(params[0]!!)
            }
        }
        private class DeleteAllTodoAsyncTask(todoDao: TodoDao) : AsyncTask<Unit, Unit, Unit>() {
            val todoDao = todoDao
            override fun doInBackground(vararg params: Unit?) {
                todoDao.deleteAllTodo()
            }
        }
    }
}