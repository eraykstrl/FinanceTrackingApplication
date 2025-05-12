package com.example.financetrackingapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetrackingapplication.model.Expenses
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ExpensesDAO {

    @Query("SELECT * FROM Expenses")
    fun getAll() : Flowable<List<Expenses>>

    @Insert
    fun insert(expenses : Expenses) : Completable

    @Delete
    fun delete(expenses : Expenses) : Completable

    @Query("SELECT * FROM Expenses WHERE id = :id")
    fun finById(id : Int) : Flowable<Expenses>

    @Update
    fun update(expenses : Expenses) : Completable

}