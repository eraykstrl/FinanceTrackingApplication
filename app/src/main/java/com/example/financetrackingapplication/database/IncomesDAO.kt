package com.example.financetrackingapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.financetrackingapplication.model.Incomes
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface IncomesDAO {

    @Query("SELECT * FROM Incomes")
    fun getAll() : Flowable<List<Incomes>>

    @Insert
    fun insert(incomes : Incomes) : Completable

    @Delete
    fun delete(incomes : Incomes) : Completable

    @Update
    fun update(incomes : Incomes) : Completable

    @Query("SELECT * FROM Incomes WHERE id = :id")
    fun findById(id : Int) : Flowable<Incomes>
}