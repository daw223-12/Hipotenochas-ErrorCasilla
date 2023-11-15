package com.example.hipotenochas

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import android.view.Gravity
import androidx.core.view.children

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var tamanho=8;
        //Hay que recordar que el tablero no deja de ser un grid
        val miGridLayout: Tablero = findViewById(R.id.miGridLayout)
        miGridLayout.generarTablero()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.game_menu, menu)

        val buscaMinas = 1

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.newGame -> {
                newGame()
                true
            }

            R.id.settings -> {
                showHelp()
                true
            }

            R.id.about -> {
                showAbout()
                true
            }

            R.id.personaje -> {
                personaje()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAbout() {
    }

    private fun showHelp() {
    }

    private fun newGame() {
    }

    private fun personaje() {
    }

    fun onClickHelp(item: MenuItem) {
        // Construir el cuadro de diálogo
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.instruccionesTitulo))
        builder.setMessage(getString(R.string.instrucciones))
        // Configurar el botón "OK"
        builder.setPositiveButton("OK") { dialog, _ ->
            // Cerrar el cuadro de diálogo cuando se hace clic en "OK"
            dialog.dismiss()
        }

        // Mostrar el cuadro de diálogo
        val dialog = builder.create()
        dialog.show()
    }
    fun onClickSettings(item: MenuItem) {
        val options = arrayOf(getString(R.string.settingsFacil), getString(R.string.settingsMedio), getString(R.string.settingsDificil))

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.settingsTitulo))
            .setItems(options) { _, which ->
                // Acciones según la opción seleccionada
                when (which) {
                    0 -> {findViewById<Tablero>(R.id.miGridLayout).tamanho(8)}
                    1 -> {findViewById<Tablero>(R.id.miGridLayout).tamanho(12)}
                    2 -> {findViewById<Tablero>(R.id.miGridLayout).tamanho(16)}
                }
            }
            .setNegativeButton("Cancelar") { _, _ ->
                showToast("Cancelaste la selección")
            }

        val dialog = builder.create()
        dialog.show()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun onClickComenzar(intem: MenuItem) {
        val miGridLayout: Tablero = findViewById(R.id.miGridLayout)
        miGridLayout.generarTablero()
    }
}