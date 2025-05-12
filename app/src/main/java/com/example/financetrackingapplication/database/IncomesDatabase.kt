package com.example.financetrackingapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.financetrackingapplication.model.Incomes

@Database(entities = [Incomes::class], version = 1)
abstract class IncomesDatabase : RoomDatabase() {

    abstract fun incomesDao() : IncomesDAO
}