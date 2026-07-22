package com.T20.tormentaapp.ui.livros

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.R

class LivrosFragment : Fragment(R.layout.fragment_livros_host) {

    private val listaFragment = LivrosListFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.container_livros) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.container_livros, listaFragment)
                .commit()
        }
    }

    fun abrirLivro(url: String) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_livros, LivroWebViewFragment.newInstance(url))
            .addToBackStack(null)
            .commit()
    }
}