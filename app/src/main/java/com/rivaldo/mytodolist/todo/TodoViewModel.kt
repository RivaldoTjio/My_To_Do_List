package com.rivaldo.mytodolist.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    @InternalCoroutinesApi
    private var repository: TodoRepository = TodoRepository(application)
    private var allTodo: LiveData<List<Todo>> = repository.getAllTodo()

    fun insert(todo: Todo) {
        repository.insert(todo)
    }
    fun update(todo: Todo){
        repository.update(todo)
    }
    fun delete(todo: Todo) {
        repository.delete(todo)
    }
    fun deleteAllTodo() {
        repository.deleteAllTodo()
    }
    fun getAllTodo(): LiveData<List<Todo>>{
        return allTodo
    }
}