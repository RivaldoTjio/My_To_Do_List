package com.rivaldo.mytodolist.todo

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object{
        private var instance: TodoDatabase? = null


        @InternalCoroutinesApi
        fun getInstance(context: Context) : TodoDatabase? {
            if (instance == null) {
                synchronized(TodoDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java, "todo_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }
        fun destroyInstance() {
            instance = null
        }
        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }
    class PopulateDbAsyncTask(db: TodoDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val todoDao = db?.todoDao()
        override fun doInBackground(vararg params: Unit?) {
            todoDao?.insert(Todo("Tugas Pemrograman Mobile","Pemrograman Mobile Praktikum 10", false, 1))
        }
    }
}