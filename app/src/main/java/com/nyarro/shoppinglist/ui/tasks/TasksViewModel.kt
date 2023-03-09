package com.nyarro.shoppinglist.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nyarro.shoppinglist.data.PreferencesManager
import com.nyarro.shoppinglist.data.SortOrder
import com.nyarro.shoppinglist.data.Task
import com.nyarro.shoppinglist.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.nio.channels.Channel

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksEventChannel = kotlinx.coroutines.channels.Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPrefences) ->
        taskDao.getTasks(query, filterPrefences.sortOrder, filterPrefences.hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) {}

    fun onTaskChekedChanged(task: Task, isCheked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isCheked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }
}
