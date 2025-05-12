package com.example.financetrackingapplication.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.financetrackingapplication.R
import com.example.financetrackingapplication.database.ExpensesDAO
import com.example.financetrackingapplication.database.ExpensesDatabase
import com.example.financetrackingapplication.databinding.FragmentAddNewExpensesBinding
import com.example.financetrackingapplication.model.Expenses
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class AddNewExpensesFragment : Fragment() {

    private var _binding : FragmentAddNewExpensesBinding ?= null
    private val binding get() = _binding!!

    private lateinit var expensesDatabase: ExpensesDatabase
    private lateinit var expensesDao: ExpensesDAO

    private val mDisposable = CompositeDisposable()

    private var choosenExpense : Expenses ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        expensesDatabase = Room.databaseBuilder(requireContext(),ExpensesDatabase::class.java,"Expenses").build()
        expensesDao = expensesDatabase.expensesDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNewExpensesBinding.inflate(inflater,container,false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val info = AddNewExpensesFragmentArgs.fromBundle(it).bilgi

            if(info == 0)
            {
                val id = AddNewExpensesFragmentArgs.fromBundle(it).id

                mDisposable.add(
                    expensesDao.finById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )

                setupUpdateSave(id)

            }

            else
            {
                choosenExpense=null

                binding.titleEditText.setText("")
                binding.amountEditText.setText("")
                binding.reasonEditText.setText("")
                setupAutoSave()

            }
        }
    }


    private fun handleResponse(expense: Expenses) {
        _binding?.let { binding ->
            binding.titleEditText.setText(expense.expenses)
            binding.amountEditText.setText(expense.amountOfExpenses.toString())
            binding.reasonEditText.setText(expense.reasonExpenses)
            choosenExpense = expense
        }
    }

    private fun setupUpdateSave(id : Int)
    {
        val focusChangeListener = View.OnFocusChangeListener { _, hasFocus->
            if(!hasFocus)
            {
                updateList(id)
            }

        }

        binding.titleEditText.onFocusChangeListener = focusChangeListener
        binding.amountEditText.onFocusChangeListener = focusChangeListener
        binding.reasonEditText.onFocusChangeListener = focusChangeListener
    }

    private fun updateList(id : Int)
    {

        val title = binding.titleEditText.text.toString().trim()
        val reason = binding.reasonEditText.text.toString().trim()
        val amountText = binding.amountEditText.text.toString().trim()


        if(title.isNotEmpty() && amountText.isNotEmpty())
        {

            val amount = amountText.toIntOrNull() ?: return
            val updatedExpense = Expenses(title,amount,reason,id)

            mDisposable.add(
                expensesDao.update(updatedExpense)
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
                tryToSaveAllFieldsFilled()
            }

        }

        binding.titleEditText.onFocusChangeListener = focusChangeListener
        binding.reasonEditText.onFocusChangeListener = focusChangeListener
        binding.amountEditText.onFocusChangeListener = focusChangeListener
    }

    private fun tryToSaveAllFieldsFilled()
    {
        val title = binding.titleEditText.text.toString().trim()
        val reason = binding.reasonEditText.text.toString().trim()
        val amountText = binding.amountEditText.text.toString().trim()

        println("fonksiyona ilk girdi")
        if(title.isNotEmpty() && amountText.isNotEmpty())
        {
            val amount = amountText.toIntOrNull() ?: return
            val newExpense = Expenses(title,amount,reason)
            mDisposable.add(
                expensesDao.insert(newExpense)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        println("Kayıt başarılı") // Confirm successful insertion
                    }, { error ->
                        println("Hata oluştu: ${error.message}")
                    })
            )



        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}