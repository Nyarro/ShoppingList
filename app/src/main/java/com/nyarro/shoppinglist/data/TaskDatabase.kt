package com.nyarro.shoppinglist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nyarro.shoppinglist.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Milk", important = true))
                dao.insert(Task("Bread", important = true))
                dao.insert(Task("Chicken", completed = true))
                dao.insert(Task("Apple"))
                dao.insert(Task("Pasta"))
                dao.insert(Task("Potato"))
                dao.insert(Task("Soft drink"))
                dao.insert(Task("Butter", important = true))
            }
        }
    }
}