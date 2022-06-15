package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentSimilarMoviesBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.adapters.SimilarMoviesAdapter
import com.uniwa.moviender.ui.viewmodel.SessionActivityViewModel
import com.uniwa.moviender.ui.viewmodel.SimilarMoviesViewModel

class SimilarMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSimilarMoviesBinding
    private val viewModel: SimilarMoviesViewModel by viewModels()
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_similar_movies, null, false)

        binding.searchTv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    viewModel.searchByTitle(p0.toString())
                } else {
                    viewModel.clearSearchResult()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SimilarMoviesAdapter(this@SimilarMoviesFragment)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            searchResultMovies.adapter = adapter
            viewModel = this@SimilarMoviesFragment.viewModel
        }

        viewModel.searchedResults.observe(viewLifecycleOwner) { newList ->
            adapter.submitList(newList) {
                binding.searchResultMovies.layoutManager?.scrollToPosition(0)
            }
        }
    }

    fun startSession(view: View) {
        viewModel.response.observe(this) { response ->
            if (response.sessionId != null) {
                sharedViewModel.sessionId = response.sessionId
                findNavController().navigate(R.id.action_similarMoviesFragment_to_votingFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.session_already_exists),
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_similarMoviesFragment_to_hubActivity)
                ActivityNavigator(requireContext()).popBackStack()
            }
        }
        val movie = view.tag as Movie
        viewModel.startSession(
            sharedViewModel.getUid(),
            sharedViewModel.friendUid,
            movie.movielensId
        )
    }
}