package com.example.financetrackingapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.financetrackingapplication.adapter.ExpensesAdapter
import com.example.financetrackingapplication.database.ExpensesDAO
import com.example.financetrackingapplication.database.ExpensesDatabase
import com.example.financetrackingapplication.databinding.FragmentExpensesBinding
import com.example.financetrackingapplication.model.Expenses
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView



class ExpensesFragment : Fragment() {

    private var _binding : FragmentExpensesBinding ?=null
    private val binding get() = _binding!!

    private lateinit var expensesDatabase: ExpensesDatabase
    private lateinit var expenesesDao : ExpensesDAO

    private var selectedItems = listOf<Expenses>()

    private val mDisposable = CompositeDisposable()

    private lateinit var adapter : ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expensesDatabase = Room.databaseBuilder(requireContext(), ExpensesDatabase::class.java,"Expenses").build()
        expenesesDao = expensesDatabase.expensesDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentExpensesBinding.inflate(inflater,container,false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.expensesFloatingActionButton.setOnClickListener { addNewExpenses(it) }

        val itemTouchHelperCallback = object  : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val expenseToDelete = adapter.getExpenseAt(position)
                delete(expenseToDelete)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.expensesRecyclerView)

        getDatas()


    }

    private fun delete(expense: Expenses)
    {
        mDisposable.add(
            expenesesDao.delete(expense)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDatas)
        )
    }

    fun addNewExpenses(view : View)
    {
        val action = ExpensesFragmentDirections.actionExpensesFragmentToAddNewExpensesFragment(bilgi=1,id=-1)
        findNavController().navigate(action)
    }

    private fun getDatas()
    {
        mDisposable.add(
            expenesesDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(expenses : List<Expenses>)
    {
        println(expenses.size)
            adapter = ExpensesAdapter(expenses) {
            selectedList ->
            selectedItems = selectedList
        }

        binding.expensesRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        getDatas()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}