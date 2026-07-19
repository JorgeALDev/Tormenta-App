package com.T20.tormentaapp.ui.livros

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.T20.tormentaapp.databinding.ItemLivroBinding

class LivrosAdapter (
    private val livros: List<Livro>,
    private val onLivroClick: (Livro) -> Unit
) : RecyclerView.Adapter<LivrosAdapter.LivroViewHolder>(){

    class LivroViewHolder(val binding: ItemLivroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LivroViewHolder {
        val binding = ItemLivroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LivroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LivroViewHolder, position: Int) {
        val livro = livros[position]
        holder.binding.txtTitulo.text = livro.titulo
        holder.binding.imgCapa.setImageResource(livro.capa)
        holder.binding.root.setOnClickListener {
            onLivroClick(livro)
        }
    }

    override fun getItemCount(): Int = livros.size
}