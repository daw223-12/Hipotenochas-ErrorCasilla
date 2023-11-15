package com.example.hipotenochas


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.widget.GridLayout
import androidx.core.view.children

class Tablero : GridLayout {
    var numHipotenochas: Int = 10
    val activityMain = R.layout.activity_main
    var ubicacionHipotenochas = ArrayList<Int>()

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    init {
        // Configuración específica del tablero, si es necesaria
        rowCount = 8
        columnCount = 8
    }

    fun tamanho(tamanho: Int) {
        this.removeAllViews()
        this.rowCount = tamanho;
        this.columnCount = tamanho;
        this.generarTablero()
    }

    fun generarTablero() {
        var cont = 0;
        // Esta funcion vacía el grid de todos sus hijos. Fundamental para luego rellenarlo
        this.removeAllViews()
        Log.d("VACIA", this.rowCount.toString())
        // Iterar sobre las filas y columnas
        for (fila in 0 until this.rowCount) {
            for (columna in 0 until this.columnCount) {
                // Crear un Casilla para cada celda
                val casilla = Casilla(getContext(), null)
                //Params
                casilla.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                }
                casilla.id = columna * 1000 + fila
                casilla.text = casilla.id.toString()
                casilla.gravity = android.view.Gravity.CENTER
                casilla.textSize = (180 / this.rowCount).toFloat();

                // Le pongo un borde ahí to chulo
                val border = GradientDrawable()
                border.setColor(Color.WHITE)
                border.setStroke(2, Color.BLACK)
                casilla.background = border
                //Listeners
                casilla.setOnClickListener { revelarCasillaArriba(casilla) }
                casilla.setOnLongClickListener {
                    casilla.revelarCasillaArriba()
                    true
                }

                // Agregar el Casilla a la cuadrícula
                this.addView(casilla)
            }
        }
        // Ubicamos las hipotenochas
        if (this.rowCount == 8) numHipotenochas = 10
        if (this.rowCount == 12) numHipotenochas = 30
        if (this.rowCount == 16) numHipotenochas = 60
        this.ubicacionHipotenochas = generarHipotenochas(numHipotenochas);
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                if (ubicacionHipotenochas.isEmpty()) Log.e("MIERDA", "MIERDON")
            }
        }

    }

    //HABRA QUE SUSTITUIR LOS TIPOS FALSE Y TRUE POR HIPOTENOCHAS
    // Genera el ArrayList con las posiciones de las hipotenochas y activa la hipotenocha
    private fun generarHipotenochas(numHipotenochas: Int): ArrayList<Int> {
        val ubicacionHipotenochas = ArrayList<Int>()
        var cont = 0;
        while (cont < numHipotenochas) {
            for (fila in 0 until this.rowCount) {
                Log.d("GENERANDO HIPOTENOCHA", cont.toString())
                for (columna in 0 until this.columnCount) {
                    val casilla = findViewById<Casilla>((fila * 1000) + columna)
                    if ((0..numHipotenochas).random() == 3 && !casilla.hipotenocha && cont < numHipotenochas) {
                        cont++
                        ubicacionHipotenochas.add(casilla.id)
                        casilla.hipotenocha = true
                    }
                }
            }
        }
        return ubicacionHipotenochas
    }

    // Se le pasa la casilla que se quiere revelar. Relevarla será:
    // Mostrar las hipotenochas de alrededor. Mostrar hipotenocha si la hay. Gestionar que pasa con lo que revelas
    //Le pasas la casilla a relevar y si entra desde un longClick (buscando hipotenocha- true) o un click (buscando normal - false)
    fun revelarCasilla(casilla: Casilla, bol: Boolean): Int {
        var cont = 0
        if (!casilla.hipotenocha && !bol)//Si no tengo hipotenocha
        {
            for (i in -1 until 2) {
                for (j in -1 until 2) {
                    if (!(j == 0 && i == 0)) {
                        val posicionRevisar =
                            (((casilla.posicionArray()[0] + i) * 1000) + j + casilla.posicionArray()[1])
                        val casillaContigua = findViewById<Casilla>(posicionRevisar)
                        if (casillaContigua != null) {
                            if (casillaContigua.hipotenocha) cont++
                            Log.d(
                                "CASILLA EXTREMO IZQ",
                                posicionRevisar.toString()
                            )
                        } else {
                                Log.e(
                                    "CASILLA NO ENCONTRADA",
                                    "Tablero-revelarCasilla" + posicionRevisar.toString()
                                )
                        }
                    }

                }

            }
            casilla.text = cont.toString()
            casilla.revelada = true
            if (cont == 0) {
                for (i in -1 until 2) {
                    if (casilla.posicionArray()[0] + i in 0..this.rowCount - 1) {
                        for (j in -1 until 2) {
                            if (!(j == 0 && i == 0) && casilla.posicionArray()[0] + j in 0..this.rowCount - 1) {
                                val posicionRevisar =
                                    (((casilla.posicionArray()[0] + i) * 1000) + j + casilla.posicionArray()[1])
                                val casillaContigua = findViewById<Casilla>(posicionRevisar)
                                if (casillaContigua != null) {
                                    if (!casillaContigua.revelada) {
                                        revelarCasilla(casillaContigua, false)
                                    }
                                } else {
                                    Log.e(
                                        "CASILLA NO ENCONTRADA", "Tablero-revelarCasilla"
                                    )
                                }
                            }
                        }
                    }

                }
            }
        } else if (casilla.hipotenocha && bol) {
            casilla.text = "H"
        } else {
            mostrarAlerta()
        }

        return cont
    }

    // Ambas funciones son un copia y pega, cambiando el this por la casilla pasada por parámetro
    // La ubicada en la clase Casilla se llamará con un onLongClick(). La otra con un click normal
    //Tampoco me ha funcionado pasandole por parámetro una casilla en casilla
    fun revelarCasillaArriba(casilla: Casilla)
    {
        val posicionRevisar =
            (((casilla.posicionArray()[0]) * 1000) -1  + casilla.posicionArray()[1])
        val casillaContigua = findViewById<Casilla>(posicionRevisar)
        // Si encuentra la casilla muestra un log de que está bien y el id, si un log error
        if (casillaContigua != null) {
            Log.d("CASILLA ENCONTRADA", "Casilla-revelarCasillaArriba() P-"+posicionRevisar.toString())
        } else {
            Log.e("CASILLA NO ENCONTRADA", "Casilla-revelarCasillaArriba() P-"+posicionRevisar.toString())
        }
    }

    private fun mostrarAlerta() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.derrotaTitulo)
        builder.setMessage(R.string.derrota)
        builder.setPositiveButton("Sí") { dialog, _ ->
            dialog.dismiss()
            generarTablero()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            this.children.forEach {

            }
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
