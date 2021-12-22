package com.example.codepath_prework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: taskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : taskItemAdapter.OnLongClickListener {
            override fun onItemLongClick(position: Int) {
                // 1. Remove item from list.
                listOfTasks.removeAt(position)
                // 2. Notify Adapter about change.
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()

        //Look up in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //create adapter passing sample user data
        adapter = taskItemAdapter(listOfTasks, onLongClickListener)
        // Attach adapter to recyclerview
        recyclerView.adapter = adapter
        //set layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //allow the user to enter task
        // 1. Detect when user clicks Add button.
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab user-input
            val userInputTask = inputTextField.text.toString()

            // 2. Add string to list
            listOfTasks.add(userInputTask)

            // Notify adapter of the update
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 3. Clear text field
            inputTextField.setText("")

            saveItems()
        }
    }
    // Save the user-input data by writing and reading from file
    // Create a method to get the file
    fun getDataFile() : File {
        // Every line will represent a specific task
        return File(filesDir, "data.txt")
    }

    // Load the items by reading all lines.
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save items by writing them into the files.
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}