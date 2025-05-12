package com.example.financetrackingapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.financetrackingapplication.adapter.IncomesAdapter
import com.example.financetrackingapplication.database.IncomesDAO
import com.example.financetrackingapplication.database.IncomesDatabase
import com.example.financetrackingapplication.databinding.FragmentIncomeBinding
import com.example.financetrackingapplication.model.Incomes
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class IncomeFragment : Fragment() {

    private var _binding : FragmentIncomeBinding ?= null
    private val binding get() = _binding!!

    private lateinit var incomesDatabase: IncomesDatabase
    private lateinit var incomesDao: IncomesDAO

    private var selectedItems = listOf<Incomes>()

    private val mDisposable = CompositeDisposable()

    private lateinit var adapter : IncomesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incomesDatabase = Room.databaseBuilder(requireContext(), IncomesDatabase::class.java,"Incomes").build()
        incomesDao = incomesDatabase.incomesDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentIncomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incomesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.incomeFloatingActionButton.setOnClickListener { addNew(it) }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
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
                val deleteFromIncomes = adapter.getIncomesAt(position)
                delete(deleteFromIncomes)


            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.incomesRecyclerView)

        getDatas()
    }

    private fun delete(incomes : Incomes)
    {
        mDisposable.add(
            incomesDao.delete(incomes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getDatas)
        )
    }

    private fun getDatas()
    {
        mDisposable.add(
            incomesDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(incomes : List<Incomes>)
    {
            adapter = IncomesAdapter(incomes) {
            selectedList->
            selectedItems = selectedList
        }

        binding.incomesRecyclerView.adapter = adapter
    }

    fun addNew(view : View)
    {
        val action = IncomeFragmentDirections.actionIncomeFragmentToAddNewIncomesFragment(bilgi=1,id=-1)
        findNavController().navigate(action)
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