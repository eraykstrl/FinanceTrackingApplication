package com.example.financetrackingapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.financetrackingapplication.R
import com.example.financetrackingapplication.databinding.FragmentRouterBinding


class RouterFragment : Fragment() {

    private var _binding : FragmentRouterBinding ?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRouterBinding.inflate(inflater,container,false)

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.incomesMaterialCardView.setOnClickListener { routerToIncomes(it) }
        binding.expensesMaterialCardView.setOnClickListener { routerToExpenses(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun routerToIncomes(view : View)
    {
        val action = RouterFragmentDirections.actionRouterFragmentToIncomeFragment()
        findNavController().navigate(action)
    }

    fun routerToExpenses(view : View)
    {
        val action = RouterFragmentDirections.actionRouterFragmentToExpensesFragment()
        findNavController().navigate(action)
    }




}