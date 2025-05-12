package com.example.financetrackingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackingapplication.databinding.RecylclerExpensesRowBinding
import com.example.financetrackingapplication.model.Expenses
import com.example.financetrackingapplication.view.ExpensesFragmentDirections
import io.reactivex.rxjava3.disposables.CompositeDisposable

class ExpensesAdapter(val expensesList : List<Expenses>, val onSelectionChanged : (List<Expenses>) -> Unit) : RecyclerView.Adapter<ExpensesAdapter.ExpensesHolder>()
{
    private val mDisposable = CompositeDisposable()

    private val selectedItems = mutableListOf<Expenses>()

    class ExpensesHolder(val binding: RecylclerExpensesRowBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensesHolder {

        val recyclerExpensesRowBinding = RecylclerExpensesRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpensesHolder(recyclerExpensesRowBinding)
    }

    override fun onBindViewHolder(
        holder: ExpensesHolder,
        position: Int
    ) {


        val item = expensesList[position]

        val isSelected = selectedItems.contains(item)

        holder.binding.expensesRecyclerViewTextView.text=item.expenses

        holder.binding.expensesRecyclerViewTextView.setOnClickListener {
            val action = ExpensesFragmentDirections.actionExpensesFragmentToAddNewExpensesFragment(bilgi=0,id=item.id)
            it.findNavController().navigate(action)
        }

        holder.binding.expensesRecyclerViewTextView.setOnLongClickListener {
            if(isSelected)
            {
                selectedItems.remove(item)

            }
            else
            {
                selectedItems.add(item)
            }

            notifyItemChanged(position)
            onSelectionChanged(selectedItems)
            true
        }

    }

    override fun getItemCount(): Int {

        return expensesList.size
    }

    fun getExpenseAt(position: Int) : Expenses
    {
        return expensesList[position]
    }


}