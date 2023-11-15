package com.example.hipotenochas

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import java.lang.System.console


class Casilla(context: Context, attrs: AttributeSet?): AppCompatTextView (context, attrs){

    //Declaracion de variables
    val padre = findViewById<Tablero>(R.id.miGridLayout)
    val posicion: Array<Int> = posicionArray() //La posicion se saca del id. Los ids son los dos primeros digitos la columna. Los dos siguientes la fila
    var hipotenocha: Boolean;
    var revelada: Boolean;
        init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Casilla,
            0, 0).apply {

            try {
                hipotenocha = getBoolean(R.styleable.Casilla_Hipotenocha, false)
                revelada = getBoolean(R.styleable.Casilla_Revelada, false)
            } finally {
                recycle()
            }
        }

    }

    /**
     * @return Devuelve el numero de minas que tiene alrededor.
     */
    //Estoy usando el buscaminas de js. Tengo que hacer la funcion cuentaminas. Tiene más sentido hacer primero las views y lo que crea las views.
    //Tablero será lo que cree las views. Creará un objeto botón con este objeto.
    //Cuando el usuario haga click, tendrá que generarse un objeto de estos y usar sus metodos. Otra forma es tenerlos todos siempre abiertos. Lo + fácil se hará
    //En el activity main habrá que hacer el menú desplegable y llamar a la función crearTablero.
    //Estoy pensando que el objeto tablero esté siempre on
    //Tengo que ver como funciona la memoria

    fun posicionArray(): Array<Int> {
        var array = Array<Int>(2) { 0 }
        array[0]=this.id/1000
        array[1]=this.id%100
        return array
    }
    // No funciona. No puedo acceder a otras casillas por el id. No sé por qué
    fun revelarCasilla(): Int {
        var cont = 0
        if (!this.hipotenocha)//Si no tengo hipotenocha
        {
            for (i in -1 until 2) {
                if (this.posicionArray()[0] + i in 0..7) {
                    for (j in -1 until 2) {
                        if (!(j == 0 && i == 0) && this.posicionArray()[0] + j in 0..7) {
                            val posicionRevisar =
                                (((this.posicionArray()[0] + i) * 1000) + j + this.posicionArray()[1])
                            val casillaContigua = findViewById<Casilla>(posicionRevisar)
                            if (casillaContigua != null) {
                                if (casillaContigua.hipotenocha) cont++
                            } else {
                                Log.e(
                                    "CASILLA NO ENCONTRADA", "Casilla-revelarCasilla"+posicionRevisar.toString()
                                )
                            }
                        }
                    }
                }

            }
            this.text = cont.toString()
        }
        return cont
    }


    // Ambas funciones son un copia y pega, cambiando el this por la casilla parámetro
    // La ubicada en la clase Casilla se llamará con un onLongClick(). La otra con un click normal
    //Tampoco me ha funcionado pasandole por parámetro una casilla (Por tanto siendo exactamente igual a la de Tablero)
    fun revelarCasillaArriba()
    {
        val posicionRevisar =
            (((this.posicionArray()[0]) * 1000) -1  + this.posicionArray()[1])
        val casillaContigua = findViewById<Casilla>(posicionRevisar)
        // Si encuentra la casilla muestra un log de que está bien y el id, si un log error
        if (casillaContigua != null) {
            Log.d("CASILLA ENCONTRADA", "Casilla-revelarCasillaArriba() P-"+posicionRevisar.toString())
        } else {
            Log.e("CASILLA NO ENCONTRADA", "Casilla-revelarCasillaArriba() P-"+posicionRevisar.toString())
        }
    }

    fun isHipotenocha(): Boolean {
        return hipotenocha
    }

}