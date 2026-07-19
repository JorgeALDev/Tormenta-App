package com.T20.tormentaapp.ui.dados

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.T20.tormentaapp.databinding.ItemDadoBinding

class DadosAdapter (
    private val dados: List<Dado>,
    private val onDadoClick: (Dado) -> Unit
): RecyclerView.Adapter<DadosAdapter.DadoViewHolder>(){

    class DadoViewHolder(val binding: ItemDadoBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DadoViewHolder {
        val binding = ItemDadoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DadoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DadoViewHolder, position: Int) {
        val dado = dados[position]
        holder.binding.txtNomeDado.text = dado.nome
        holder.binding.imgDado.setImageResource(dado.icone)
        holder.binding.root.setOnClickListener {
            onDadoClick(dado)
        }
    }

    override fun getItemCount(): Int = dados.size
}