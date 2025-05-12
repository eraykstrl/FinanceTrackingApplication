package com.example.financetrackingapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.example.financetrackingapplication.R
import com.example.financetrackingapplication.database.IncomesDAO
import com.example.financetrackingapplication.database.IncomesDatabase
import com.example.financetrackingapplication.databinding.FragmentAddNewExpensesBinding
import com.example.financetrackingapplication.databinding.FragmentAddNewIncomesBinding
import com.example.financetrackingapplication.model.Incomes
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class AddNewIncomesFragment : Fragment() {

    private var _binding : FragmentAddNewIncomesBinding ?= null
    private val binding get() = _binding!!

    private lateinit var incomesDatabase : IncomesDatabase
    private lateinit var incomesDao : IncomesDAO

    private var choosenIncomes : Incomes ?= null

    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        incomesDatabase = Room.databaseBuilder(requireContext(), IncomesDatabase::class.java,"Incomes").build()
        incomesDao = incomesDatabase.incomesDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNewIncomesBinding.inflate(inflater,container,false)

        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val info = AddNewIncomesFragmentArgs.fromBundle(it).bilgi

            if(info == 0)
            {

                val id = AddNewIncomesFragmentArgs.fromBundle(it).id
                mDisposable.add(
                    incomesDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )

                setupUpdateSave(id)

            }

            else
            {
                choosenIncomes = null
                binding.titleEditText.setText("")
                binding.amountEditText.setText("")
                binding.reasonEditText.setText("")

                setupAutoSave()
            }
        }

    }

    private fun setupUpdateSave(id : Int)
    {
        val focusChangeListener = View.OnFocusChangeListener { _, hasFocus->
            if(!hasFocus)
            {
                tryToUpdate(id)
            }

        }

        binding.titleEditText.onFocusChangeListener = focusChangeListener
        binding.reasonEditText.onFocusChangeListener = focusChangeListener
        binding.amountEditText.onFocusChangeListener = focusChangeListener
    }

    private fun tryToUpdate(id : Int)
    {
        val title = binding.titleEditText.text.toString()
        val amountText = binding.amountEditText.text.toString()
        val reason = binding.reasonEditText.text.toString()

        if(title.isNotEmpty() && amountText.isNotEmpty())
        {
            val amount = amountText.toIntOrNull() ?: return

            val updatedIncomes = Incomes(title,amount,reason,id)

            mDisposable.add(
                incomesDao.update(updatedIncomes)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }



    private fun setupAutoSave()
    {
        val focusChangeListener = View.OnFocusChangeListener { _, hasFocus->
            if(!hasFocus)
            {
                tryToAddNewIncomes()
            }

        }

        binding.titleEditText.onFocusChangeListener = focusChangeListener
        binding.reasonEditText.onFocusChangeListener = focusChangeListener
        binding.amountEditText.onFocusChangeListener = focusChangeListener
    }

    private fun tryToAddNewIncomes()
    {
        val title = binding.titleEditText.text.toString()
        val reason = binding.reasonEditText.text.toString()
        val amountText = binding.amountEditText.text.toString()

        if(title.isNotEmpty() && amountText.isNotEmpty())
        {
            val amount = amountText.toIntOrNull() ?: return

            val newIncomes = Incomes(title,amount,reason)

            mDisposable.add(
                incomesDao.insert(newIncomes)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
            println("buradasin")
        }
    }

    private fun handleResponse(incomes : Incomes)
    {
        _binding?.let { binding->
            binding.titleEditText.setText(incomes.incomes)
            binding.amountEditText.setText(incomes.amounOfIncomes.toString())
            binding.reasonEditText.setText(incomes.reasonIncomes)

            choosenIncomes = incomes
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}