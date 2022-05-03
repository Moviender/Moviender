package com.uniwa.moviender.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentInitializationBinding

import com.uniwa.moviender.model.LoginViewModel
import com.uniwa.moviender.ui.MovieGridAdapter

class InitializationFragment : Fragment() {

    private lateinit var binding: FragmentInitializationBinding

    private val sharedViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_initialization, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            initializationFragment = this@InitializationFragment
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            moviesGrid.adapter = MovieGridAdapter(sharedViewModel)
        }

        sharedViewModel.getStarterMovies()
    }

    fun finishInitilization() {
        sendRatingsToDB()
        saveUidToDataStore()
        navigateToHub()
    }

    private fun sendRatingsToDB() {
        sharedViewModel.sendRatings()
    }

    private fun saveUidToDataStore() {
        sharedViewModel.saveUID(requireContext())
    }

    private fun navigateToHub() {
        findNavController().navigate(R.id.action_initializationFragment_to_hubActivity)
    }

}