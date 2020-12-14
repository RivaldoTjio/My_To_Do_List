package com.rivaldo.mytodolist.todo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo (
    var title: String,
    var description: String,
    var done: Boolean,
    var priority: Int
        ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}