package com.nyarro.shoppinglist.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nyarro.shoppinglist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks) {

    private val viewModel: TasksViewModel by viewModels()
}