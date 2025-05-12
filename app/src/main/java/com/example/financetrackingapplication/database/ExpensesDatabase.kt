package com.example.financetrackingapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financetrackingapplication.model.Expenses

@Database(entities = [Expenses::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {

    abstract fun expensesDao() : ExpensesDAO
}