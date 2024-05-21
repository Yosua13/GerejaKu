package com.example.projecttingkat2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projecttingkat2.model.Gereja

@Database(entities = [Gereja::class], version = 2, exportSchema = false)

abstract class GerejaKuDb : RoomDatabase() {
    abstract val gerejaDao: GerejaDao

    companion object {
        @Volatile
        private var INSTANCE: GerejaKuDb? = null

//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS berita_acara (" +
//                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//                            "todo TEXT NOT NULL," +
//                            "kalender TEXT NOT NULL," +
//                            "jam TEXT NOT NULL)"
//                )
//            }
//        }
        fun getInstance(context: Context): GerejaKuDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GerejaKuDb::class.java,
                        "gerejakuu.db"
                    )
//                        .addMigrations(MIGRATION_1_2)  // Tambahkan migrasi ke sini
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}