package com.example.kissmoney.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kissmoney.compromissos.Compromisso
import com.example.kissmoney.compromissos.CompromissoDao
import com.example.kissmoney.compromissos.mensal.CompromissoMensal
import com.example.kissmoney.compromissos.mensal.CompromissoMensalDao
import com.example.kissmoney.contas.Conta
import com.example.kissmoney.contas.ContaDao
import com.example.kissmoney.contas.mensal.MovimentacaoMensal
import com.example.kissmoney.contas.mensal.MovimentacaoMensalDao
import com.example.kissmoney.ganhos.Ganho
import com.example.kissmoney.ganhos.GanhoDao
import com.example.kissmoney.ganhos.mensal.GanhoMensal
import com.example.kissmoney.ganhos.mensal.GanhoMensalDao
import com.example.kissmoney.mes.Mes
import com.example.kissmoney.mes.MesDao
import com.example.kissmoney.meta.Meta
import com.example.kissmoney.meta.MetaDao
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [Compromisso::class, CompromissoMensal::class, Conta::class, MovimentacaoMensal::class,
        Ganho::class, GanhoMensal::class, Meta::class, Mes::class],
    version = 2,
    exportSchema = false
)
abstract class KissmoneyDatabase  : RoomDatabase(){

    abstract fun mesDao(): MesDao
    abstract fun metaDao(): MetaDao
    abstract fun compromissoDao(): CompromissoDao
    abstract fun compromissoMensalDao(): CompromissoMensalDao
    abstract fun contaDao(): ContaDao
    abstract fun movimentacaoMensalDao(): MovimentacaoMensalDao
    abstract fun ganhoDao(): GanhoDao
    abstract fun ganhoMensalDao(): GanhoMensalDao

    companion object {
        @Volatile
        private var INSTANCE: KissmoneyDatabase? = null


        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): KissmoneyDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KissmoneyDatabase::class.java,
                    "kissmoney_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(LibertyDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class LibertyDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

        }


    }

}
