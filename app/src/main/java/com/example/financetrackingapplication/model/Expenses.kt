package com.example.financetrackingapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses(

    @ColumnInfo(name="expenses")
    var expenses: String,

    @ColumnInfo(name="amountOfExpenses")
    var amountOfExpenses: Int,

    @ColumnInfo(name="reasonExpenses")
    var reasonExpenses: String,

    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

)