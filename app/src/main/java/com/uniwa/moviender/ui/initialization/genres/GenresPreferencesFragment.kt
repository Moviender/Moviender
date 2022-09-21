package com.uniwa.moviender.ui.initialization.genres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentGenresPreferencesBinding
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class GenresPreferencesFragment : Fragment() {

    private lateinit var binding: FragmentGenresPreferencesBinding
    private val viewModel: GenresPreferencesViewModel by viewModels()
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_genres_preferences,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GenresPreferencesFragment.viewModel
            genresPreferencesFragment = this@GenresPreferencesFragment
            genrePreferencesRv.adapter = GenresPreferencesAdapter(this@GenresPreferencesFragment)
        }

        observeProgress()
    }

    private fun observeProgress() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.progress.collectLatest {
                binding.initializationProgress.progress = it

                if (it == 100)
                    navigateToHub()
            }
        }
    }

    fun onCheckedChanged(genre: Int, checked: Boolean) {
        if (checked) {
            viewModel.addGenre(genre)
        } else {
            viewModel.removeGenre(genre)
        }
    }

    fun finishInitialization() {
        binding.initializationProgress.show()

        sendInitializationData()
    }

    private fun sendInitializationData() {
        viewModel.sendInitializationData(sharedViewModel.getUserRatings(), sharedViewModel.getUid())
    }

    private fun navigateToHub() {
        findNavController().navigate(R.id.action_genresPreferencesFragment_to_hubNavigation)
    }

}