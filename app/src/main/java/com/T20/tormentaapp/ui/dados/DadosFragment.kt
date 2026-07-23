package com.T20.tormentaapp.ui.dados

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.T20.tormentaapp.R
import com.T20.tormentaapp.databinding.FragmentDadosBinding
import kotlin.random.Random

class DadosFragment : Fragment(R.layout.fragment_dados) {

    private lateinit var soundPool: SoundPool
    private var somDadoId: Int = 0

    private val dadosPendentes = mutableListOf<Dado>()
    private lateinit var pendentesAdapter: DadosPendentesAdapter
    private var modificador = 0

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

        binding.btnModificadorMenos.setOnClickListener {
            modificador--
            atualizarModificador(binding)
        }
        binding.btnModificadorMais.setOnClickListener {
            modificador++
            atualizarModificador(binding)
        }

        binding.edtModificador.setOnFocusChangeListener { _, temFoco ->
            if (temFoco) binding.edtModificador.selectAll()
        }

        binding.edtModificador.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                modificador = s?.toString()?.replace("+", "")?.toIntOrNull() ?: 0
            }
        })
    }

    private fun atualizarModificador(binding: FragmentDadosBinding) {
        val texto = if (modificador >= 0) "+$modificador" else "$modificador"
        binding.edtModificador.setText(texto)
        binding.edtModificador.setSelection(texto.length)
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
        val modificadorAtual = modificador

        Handler(Looper.getMainLooper()).postDelayed({

            val rolagens = dadosParaRolar.map { dado ->
                val valor = Random.nextInt(1, dado.lados + 1)
                valor to dado.nome.lowercase()
            }

            val somaDados = rolagens.sumOf { it.first }
            val somaTotal = somaDados + modificadorAtual

            val detalhamentoDados = rolagens.joinToString(" + ") { (valor, nomeDado) ->
                "$valor($nomeDado)"
            }
            val detalhamento = when {
                modificadorAtual > 0 -> "$detalhamentoDados + $modificadorAtual"
                modificadorAtual < 0 -> "$detalhamentoDados - ${-modificadorAtual}"
                else -> detalhamentoDados
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