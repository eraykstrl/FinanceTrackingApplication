package com.example.financetrackingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.financetrackingapplication.databinding.RecyclerIncomesRowBinding
import com.example.financetrackingapplication.model.Expenses
import com.example.financetrackingapplication.model.Incomes
import com.example.financetrackingapplication.view.IncomeFragmentDirections

class IncomesAdapter(val incomesList : List<Incomes> , val onSelectionChanged : (List<Incomes>) -> Unit) : RecyclerView.Adapter<IncomesAdapter.IncomesHolder>()
{
    private val selectedItems = mutableListOf<Incomes>()

    class IncomesHolder(val binding : RecyclerIncomesRowBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IncomesHolder {

        val recyclerIncomesRowBinding = RecyclerIncomesRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return IncomesHolder(recyclerIncomesRowBinding)
    }

    override fun onBindViewHolder(
        holder: IncomesHolder,
        position: Int
    ) {

        val item = incomesList[position]

        holder.binding.incomesRecylcerViewTextView.text = item.incomes

        val isSelected = selectedItems.contains(item)

        holder.binding.incomesRecylcerViewTextView.setOnClickListener {

            val action = IncomeFragmentDirections.actionIncomeFragmentToAddNewIncomesFragment(bilgi=0,item.id)
            it.findNavController().navigate(action)
        }

        holder.binding.incomesRecylcerViewTextView.setOnLongClickListener {
            if(isSelected)
            {
                selectedItems.remove(item)
            }

            else {
                selectedItems.add(item)
            }

            notifyItemChanged(position)
            onSelectionChanged(selectedItems)
            true
        }



    }

    override fun getItemCount(): Int {

        return incomesList.size
    }

    fun getIncomesAt(position: Int) : Incomes
    {
        return incomesList[position]
    }

}