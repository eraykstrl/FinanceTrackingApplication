package com.example.financetrackingapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Incomes(

    @ColumnInfo(name="incomes")
    var incomes : String,

    @ColumnInfo(name="amountOfIncomes")
    var amounOfIncomes: Int,

    @ColumnInfo(name="reasonIncomes")
    var reasonIncomes : String,

    @PrimaryKey(autoGenerate = true)
    var id : Int =0

)