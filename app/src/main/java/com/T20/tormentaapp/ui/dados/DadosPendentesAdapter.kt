package com.T20.tormentaapp.ui.dados

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.T20.tormentaapp.R

class DadosPendentesAdapter (
    private val pendentes: MutableList<Dado>,
    private val onRemoverClick: (Int) -> Unit
): RecyclerView.Adapter<DadosPendentesAdapter.PendenteViewHolder>(){

    class PendenteViewHolder(val textView: TextView) :
            RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendenteViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dado_pendente, parent, false) as TextView
        return PendenteViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PendenteViewHolder, position: Int) {
        val dado = pendentes[position]
        holder.textView.text = dado.nome
        holder.textView.setOnClickListener {
            onRemoverClick(position)
        }
    }

    override fun getItemCount(): Int = pendentes.size
}