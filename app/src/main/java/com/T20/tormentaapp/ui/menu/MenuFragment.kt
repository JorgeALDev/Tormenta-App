package com.T20.tormentaapp.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.T20.tormentaapp.MainActivity
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMenuBinding.bind(view)

        binding.btnLivros.setOnClickListener {
            (activity as? MainActivity)?.irParaAba(R.id.nav_livros)
        }
        binding.btnDados.setOnClickListener {
            (activity as? MainActivity)?.irParaAba(R.id.nav_dados)
        }
        binding.btnFichas.setOnClickListener {
            (activity as? MainActivity)?.irParaAba(R.id.nav_fichas)
        }
    }
}