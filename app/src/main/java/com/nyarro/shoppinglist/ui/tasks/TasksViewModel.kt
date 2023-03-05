package com.nyarro.shoppinglist.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.nyarro.shoppinglist.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
 private val taskDao: TaskDao
) : ViewModel(){
}