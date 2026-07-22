package com.T20.tormentaapp.ui.livros

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentLivrosListBinding

class LivrosListFragment : Fragment(R.layout.fragment_livros_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLivrosListBinding.bind(view)

        val colunas = resources.getInteger(R.integer.colunas_grid)
        binding.recyclerLivros.layoutManager = GridLayoutManager(requireContext(), colunas)
        binding.recyclerLivros.adapter = LivrosAdapter(LivrosData.todos) { livroClicado ->
            (parentFragment as? LivrosFragment)?.abrirLivro(livroClicado.url)
        }
    }
}