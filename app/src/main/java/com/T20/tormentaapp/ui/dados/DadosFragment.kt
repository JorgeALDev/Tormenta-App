package com.T20.tormentaapp.ui.dados

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.T20.tormentaapp.R
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.T20.tormentaapp.databinding.FragmentDadosBinding
import kotlin.random.Random

class DadosFragment : Fragment(R.layout.fragment_dados) {
    private lateinit var soundPool: SoundPool
    private var somDadoId: Int = 0

    private val dadosPendentes = mutableListOf<Dado>()
    private lateinit var pendentesAdapter: DadosPendentesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()

        somDadoId = soundPool.load(requireContext(), R.raw.rolagemdedados, 1)

        val binding = FragmentDadosBinding.bind(view)

        val colunas = resources.getInteger(R.integer.colunas_grid)
        binding.recyclerDados.layoutManager = GridLayoutManager(requireContext(), colunas)
        binding.recyclerDados.adapter = DadosAdapter(DadosData.todos) { dadoClicado ->
            adicionarPendente(dadoClicado)
        }

        pendentesAdapter = DadosPendentesAdapter(dadosPendentes) { posicaoRemovida ->
            removerPendente(posicaoRemovida)
        }
        binding.recyclerPendentes.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerPendentes.adapter = pendentesAdapter

        binding.btnRolarPendentes.setOnClickListener {
            rolarPendentes(binding)
        }
    }

    private fun adicionarPendente(dado: Dado) {
        dadosPendentes.add(dado)
        pendentesAdapter.notifyItemInserted(dadosPendentes.size - 1)
    }

    private fun removerPendente(posicao: Int) {
        dadosPendentes.removeAt(posicao)
        pendentesAdapter.notifyItemRemoved(posicao)
    }

    private fun rolarPendentes(binding: FragmentDadosBinding) {
        if (dadosPendentes.isEmpty()) return

        soundPool.play(somDadoId, 1f, 1f, 0, 0, 1f)

        binding.txtResultado.text = "..."
        binding.txtDetalhamento.text = ""

        val dadosParaRolar = dadosPendentes.toList()

        Handler(Looper.getMainLooper()).postDelayed({

            val rolagens = dadosParaRolar.map { dado ->
                val valor = Random.nextInt(1, dado.lados + 1)
                valor to dado.nome.lowercase()
            }

            val somaTotal = rolagens.sumOf { it.first }
            val detalhamento = rolagens.joinToString(" + ") { (valor, nomeDado) ->
                "$valor($nomeDado)"
            }

            binding.txtResultado.text = somaTotal.toString()
            binding.txtDetalhamento.text = detalhamento

            dadosPendentes.clear()
            pendentesAdapter.notifyDataSetChanged()
        }, 400)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundPool.release()
    }
}