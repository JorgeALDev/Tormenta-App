package com.T20.tormentaapp.ui.livros

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentLivrosListBinding

class LivrosListFragment : Fragment(R.layout.fragment_livros_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLivrosListBinding.bind(view)

        binding.recyclerLivros.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerLivros.adapter = LivrosAdapter(LivrosData.todos) { livroClicado ->
            val action = LivrosListFragmentDirections.actionLivrosListFragmentToLivroWebViewFragment(livroClicado.url)
            findNavController().navigate(action)
        }
    }
}