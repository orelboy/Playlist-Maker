package com.practicum.playlist_maker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlist_maker.databinding.FragmentPlayListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFragment : Fragment() {
    private val viewModel: PlayListViewModel by viewModel()

    private var _binding: FragmentPlayListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // подписываемся на соответствующую LiveData todo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlayListFragment {
            return PlayListFragment()
        }
    }
}