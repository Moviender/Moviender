package com.uniwa.moviender.ui.session.movies.genreBased

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentSessionGenresBinding
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class SessionGenresFragment : Fragment() {

    private lateinit var binding: FragmentSessionGenresBinding
    private val viewModel: SessionGenresViewModel by viewModels()
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_session_genres, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SessionGenresFragment.viewModel
            sessionGenresFragment = this@SessionGenresFragment
            genresRv.adapter = SessionGenresAdapter(this@SessionGenresFragment)
        }
    }

    fun onCheckedChanged(genre: Int, checked: Boolean) {
        if (checked) {
            viewModel.addGenre(genre)
        } else {
            viewModel.removeGenre(genre)
        }
    }

    fun startSession() {
        observeSessionStarted()
        viewModel.startSession(sharedViewModel.getUid(), sharedViewModel.friendUid)
    }

    private fun observeSessionStarted() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sessionId.collectLatest { response ->
                if (response.sessionId != null) {
                    sharedViewModel.sessionId = response.sessionId
                    findNavController().navigate(R.id.action_sessionGenresFragment_to_votingFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.session_already_exists),
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_sessionGenresFragment_to_hub_navigation)
                }
            }
        }
    }
}