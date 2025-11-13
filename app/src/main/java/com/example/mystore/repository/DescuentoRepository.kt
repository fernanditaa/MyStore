package com.example.mystore.repository

import com.example.mystore.R
import com.example.mystore.model.Descuento
import kotlinx.coroutines.delay

class DescuentoRepository {

    private val Descuentos = listOf(
        Descuento(1,"Descuento de primera compra",
            "Aplica este cupón de descuento " +
                    "por tu primera compra y obten un 45% de descuento","BIENVENIDA45", R.drawable.bienbenida ),
        Descuento(2,"Descunto por compra",
        "Por compras superiores a $40.000  " ,"FESTIVO20", R.drawable.desccompra1 ),
        Descuento(3,"Descunto por compra","Por compras superiores a $50.000 ","PREMIUM25", R.drawable.desccompra2),
        Descuento(4,"Descunto de envío","Por compras superiores a $30.000 tendrás " +
                "un 10% de descuento con este cupón","ENVIO10", R.drawable.descenvio1),
        Descuento(5,"Descunto de envío","Por compras superiores a $50.000 tendrás " +
                "un 15% de descuento con este cupón","FIEL15", R.drawable.descenvio2),
    )

    //aca tambien usamos la funcion para la lista de los descuentos que vamos a usar

    suspend fun getDescuento(): List<Descuento>{
        delay(1000)
        return Descuentos
    }

    suspend fun getDescuentoById(id: Int): Descuento?{
        delay(500)
        return Descuentos.find { it.id == id }
    }
}
