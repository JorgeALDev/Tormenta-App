package com.T20.tormentaapp.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu){

    override fun onViewCreated(view: View, saveInstanceState: Bundle?){
        super.onViewCreated(view, saveInstanceState)

        val binding = FragmentMenuBinding.bind(view)

        binding.btnLivros.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_livrosListFragment)
        }

        binding.btnDados.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_dadosFragment)
        }
    }
}