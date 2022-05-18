package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentSessionGenresBinding
import com.uniwa.moviender.ui.adapters.SessionGenresAdapter
import com.uniwa.moviender.ui.viewmodel.SessionGenresViewModel

class SessionGenresFragment : Fragment() {

    private lateinit var binding: FragmentSessionGenresBinding
    private val viewModel: SessionGenresViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            genresRv.adapter = SessionGenresAdapter(this@SessionGenresFragment.viewModel)
        }
    }
}