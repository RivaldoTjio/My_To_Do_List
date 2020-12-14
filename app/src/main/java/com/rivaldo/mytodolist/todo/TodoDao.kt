package com.rivaldo.mytodolist.todo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo:Todo)

    @Query("DELETE FROM todo_table")
    fun deleteAllTodo()

    @Query("SELECT * FROM todo_table WHERE done = 0 ORDER BY priority DESC ")
    fun getAllTodo(): LiveData<List<Todo>>
}
