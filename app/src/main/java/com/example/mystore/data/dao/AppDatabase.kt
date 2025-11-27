package com.example.mystore.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystore.model.Usuario

@Database(entities = [Usuario::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun usuarioDao(): UsuarioDao

    companion object{
        @Volatile
        private var ISNTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return ISNTANCE ?: synchronized(this){
                Room.databaseBuilder(
                        context.applicationContext,
                    AppDatabase::class.java,
                        "mustore_db"
                        ).fallbackToDestructiveMigration()
                    .build()
                    .also { ISNTANCE = it }
            }
        }
    }
}