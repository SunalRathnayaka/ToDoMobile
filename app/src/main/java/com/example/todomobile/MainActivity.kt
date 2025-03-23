package com.example.todomobile

import DatabaseHelper
import TodoAdapter
import TodoItem
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.todomobile.R // Ensure this import resolves correctly

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: TodoAdapter
    private lateinit var tasks: MutableList<TodoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        tasks = databaseHelper.getAllTasks().toMutableList()

        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        val taskEditText = findViewById<EditText>(R.id.taskEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        adapter = TodoAdapter(tasks,
            onTaskUpdate = { task -> databaseHelper.updateTask(task) },
            onTaskDelete = { id -> databaseHelper.deleteTask(id) }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            val taskText = taskEditText.text.toString()
            if (taskText.isNotEmpty()) {
                val id = databaseHelper.addTask(taskText)
                tasks.add(TodoItem(id, taskText, false))
                adapter.notifyItemInserted(tasks.size - 1)
                taskEditText.text.clear()
            }
        }
    }
}