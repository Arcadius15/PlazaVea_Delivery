package edu.pe.idat.plazaveadelivery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.pe.idat.plazaveadelivery.db.dao.TokenDAO
import edu.pe.idat.plazaveadelivery.db.dao.UsuarioDAO
import edu.pe.idat.plazaveadelivery.db.entity.TokenEntity
import edu.pe.idat.plazaveadelivery.db.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class, TokenEntity::class], version = 1)
abstract  class RoomDatabaseDelivery : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun tokenDAO(): TokenDAO

    companion object{
        @Volatile
        private var INSTANCE: RoomDatabaseDelivery? = null

        fun getDatabase(context: Context) : RoomDatabaseDelivery{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabaseDelivery::class.java,
                    "deliverydb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}