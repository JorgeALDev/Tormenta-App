package com.T20.tormentaapp

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.T20.tormentaapp.ui.dados.DadosFragment
import com.T20.tormentaapp.ui.fichas.FichasListFragment
import com.T20.tormentaapp.ui.livros.LivrosFragment
import com.T20.tormentaapp.ui.menu.MenuFragment

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentInicio: Fragment
    private lateinit var fragmentLivros: Fragment
    private lateinit var fragmentDados: Fragment
    private lateinit var fragmentFichas: Fragment
    private lateinit var fragmentAtivo: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if (savedInstanceState == null) {
            fragmentInicio = MenuFragment()
            fragmentLivros = LivrosFragment()
            fragmentDados = DadosFragment()
            fragmentFichas = FichasListFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.container_principal, fragmentFichas, "fichas").hide(fragmentFichas)
                .add(R.id.container_principal, fragmentDados, "dados").hide(fragmentDados)
                .add(R.id.container_principal, fragmentLivros, "livros").hide(fragmentLivros)
                .add(R.id.container_principal, fragmentInicio, "inicio")
                .commit()

            fragmentAtivo = fragmentInicio
        } else {
            // Activity recriada (rotação, troca de tema) — recupera os Fragments JÁ existentes
            fragmentInicio = supportFragmentManager.findFragmentByTag("inicio")!!
            fragmentLivros = supportFragmentManager.findFragmentByTag("livros")!!
            fragmentDados = supportFragmentManager.findFragmentByTag("dados")!!
            fragmentFichas = supportFragmentManager.findFragmentByTag("fichas")!!
            fragmentAtivo = listOf(fragmentInicio, fragmentLivros, fragmentDados, fragmentFichas)
                .first { !it.isHidden }
        }

        val bottomNav = findViewById<NavigationBarView>(R.id.bottom_nav)
        bottomNav.itemIconTintList = null
        bottomNav.setOnItemSelectedListener { item ->
            val novoFragment = when (item.itemId) {
                R.id.nav_inicio -> fragmentInicio
                R.id.nav_livros -> fragmentLivros
                R.id.nav_dados -> fragmentDados
                R.id.nav_fichas -> fragmentFichas
                else -> fragmentInicio
            }
            trocarAba(novoFragment)
            true
        }

        atualizarVisibilidadeNav()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val livrosFragment = fragmentLivros as? LivrosFragment
                if (livrosFragment != null && livrosFragment.childFragmentManager.backStackEntryCount > 0) {
                    livrosFragment.childFragmentManager.popBackStack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun trocarAba(novoFragment: Fragment) {
        if (novoFragment == fragmentAtivo) return
        supportFragmentManager.beginTransaction().hide(fragmentAtivo).show(novoFragment).commit()
        fragmentAtivo = novoFragment
        atualizarVisibilidadeNav()
    }

    private fun atualizarVisibilidadeNav() {
        findViewById<NavigationBarView>(R.id.bottom_nav).visibility =
            if (fragmentAtivo == fragmentInicio) View.GONE else View.VISIBLE
    }

    fun irParaAba(itemId: Int) {
        findViewById<NavigationBarView>(R.id.bottom_nav).selectedItemId = itemId
    }
}