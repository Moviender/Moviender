package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentInitializationBinding
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.ui.adapters.MoviesRowAdapter
import com.uniwa.moviender.ui.viewmodel.InitializationViewModel
import com.uniwa.moviender.ui.viewmodel.StartupActivityViewModel

class InitializationFragment : Fragment() {

    private lateinit var binding: FragmentInitializationBinding
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
    private val viewModel: InitializationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_initialization, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            initializationFragment = this@InitializationFragment
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@InitializationFragment.viewModel
            moviesGrid.adapter = MoviesRowAdapter(this@InitializationFragment.viewModel)
        }

        viewModel.getStarterMovies()
    }

    fun finishMoviesRating() {
        sharedViewModel.setUserRatings(
            UserRatings(
                sharedViewModel.getUid(),
                viewModel.getRatings()
            )
        )

        navigateToGenrePreferences()
    }

    private fun navigateToGenrePreferences() {
        findNavController().navigate(R.id.action_initializationFragment_to_genresPreferencesFragment)
    }

}