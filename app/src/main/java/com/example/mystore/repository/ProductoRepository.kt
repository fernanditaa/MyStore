package com.example.mystore.repository

import com.example.mystore.model.Producto
import com.example.mystore.R
class ProductoRepository {

    private val Productos = listOf(
        Producto(1,"TurboAbuela","Personaje del anime 'DanDaDan' ", "18cm",20000.0,R.drawable.turboabuela, 1),
        Producto(2,"Recuerdo en forma de corazón","Souvenir, recuerdo para Matrimonio, todos los souvenris son por docena", "8cm",13000.0, R.drawable.corazon, 2),
        Producto(3,"Boina","Tejido en Lana Hipoalergenica para todo tipo de piel", "22cm de circunferencia", 28000.0, R.drawable.boina, 3),
        Producto(4,"Zoey","Personaje de la pelicula Las guerreras del K-pop","15 cm", 15000.0,R.drawable.soey,1),
        Producto(5,"Chupete tejido", "Recuerdo tejido en forma de chupete", "8 cm", 9000.0, R.drawable.chupetes,2 ),
        Producto(6,"Gorro estilo Slouchy", "Gorro de lana con patrón de trenzas y caída posterior.","36 cm", 18000.0,R.drawable.gorro1, 3 ),
        Producto(7,"Coraje", "Amigurimi de la serie 'Coraje, el perro covarde'", "25 cm", 25000.0, R.drawable.coraje, 1),
        Producto(8,"Vestidos", "Mini vestidos ideales para recuerdos'", "5 cm", 7000.0, R.drawable.vestidos, 2),
        Producto(9,"Gorro de gatito", "Bello gorro tejido con forma de orejas de gato", "35 cm", 12000.0, R.drawable.gorro2, 3),
        Producto(10,"Rumi", "Personaje de la pelicula Las guerreras del K-pop", "15 cm", 15000.0, R.drawable.rumi, 1),
        Producto(11,"Sombreros", "Mini sombreros para recuerdos", "5 cm", 7000.0, R.drawable.sombreros, 2),
        Producto(12,"Gorro Gohan", "Gorro característico de Gohan, de la serie Dragon Ball Z", "20 cm", 10000.0, R.drawable.gorro3, 3),
        Producto(13,"Mira", "Personaje de la pelicula Las guerreras del K-pop", "15 cm", 15000.0, R.drawable.mira, 1),
        Producto(14,"Moños", "Pequeños moños ideales para recuerdos ","4 cm", 9000.0, R.drawable.monos, 2),
        Producto(15,"Gorro candy", "Gorro tejido multicolor estilo 'Candy' con pompón XL", "35 cm", 15000.0, R.drawable.gorro4, 3),
        Producto(16,"Kurama", "Personaje de la serie 'Naruto'", "25 cm", 35000.0, R.drawable.kurama, 1),
        Producto(17,"Cruces", "Recuerdos en forma de cruz ideales para Bautizos o primera comunión", "8 cm", 9000.0, R.drawable.cruz, 2),
        Producto(18,"Gorro", "Gorro rosa con orejeras y tres pompones.", "25 cm", 8000.0, R.drawable.gorro5, 3),
        Producto(19,"Harry Styles", "Amigurimi perzonalizado del cantante Harry Styles", "25 cm", 25000.0, R.drawable.styles, 1),
        Producto(20,"body", "Bellos recuerdos tejidos con forma de boy de bebé", "7 cm", 7000.0, R.drawable.body1, 2),
        Producto(21,"Gorro turbante", "Turbante gris con brillos y flor para bebé.", "25 cm", 10000.0, R.drawable.gorro6, 3)




    )
//Devolvemos la lista de los productos con los datos simulados
    suspend fun getProducto(): List<Producto>{
        kotlinx.coroutines.delay(1000)// tiempo de carga de 1 segundo de espera
        return Productos // devuelve los productos
    }

    // devuelve el producto por el ID
    suspend fun getProductoById(id: Int): Producto?{
        kotlinx.coroutines.delay(500)
        return Productos.find { it.id == id }// busca el producto por el ID
    }
}