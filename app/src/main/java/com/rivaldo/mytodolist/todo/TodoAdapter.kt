package com.rivaldo.mytodolist.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rivaldo.mytodolist.R
import kotlinx.android.synthetic.main.todo_item.view.*
import java.text.FieldPosition

class TodoAdapter : androidx.recyclerview.widget.ListAdapter<Todo, TodoAdapter.TodoHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
                return oldItem.title == newItem.title && oldItem.description == newItem.description
                        && oldItem.priority == newItem.priority && oldItem.done == newItem.done
            }
        }
    }


    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : TodoHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int){
        val currentTodo: Todo = getItem(position)
        holder.tvTitle.text = currentTodo.title
        holder.tvDescription.text = currentTodo.description
        holder.checkbox.isChecked = currentTodo.done

    }

    fun getTodoAt(position: Int) : Todo {
        return getItem(position)
    }
    inner class TodoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position))
                }
            }
            itemView.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                val position = adapterPosition
                val currentItem = getItem(adapterPosition)
                if (position != RecyclerView.NO_POSITION) {
                    currentItem.done = isChecked
                    listener?.onItemChecked(currentItem)
                }
            }
        }
    var tvTitle: TextView = itemView.txt_title
        var tvDescription: TextView = itemView.txt_description
        var checkbox: CheckBox = itemView.checkBox
    }
    interface OnItemClickListener {
        fun onItemClick(todo: Todo)
        fun onItemChecked(todo: Todo)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}