package com.apdef.mentari.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.apdef.mentari.models.Product

@Database(entities = arrayOf(Product::class), version = 1)
//@TypeConverters(ImagesTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {


    companion object {

        private var APPDATABASE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (APPDATABASE == null) {
                synchronized(AppDatabase::class) {
                    APPDATABASE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase::class.java, "blonjoDatabase3.db").allowMainThreadQueries()
                        .build()
                }
            }
            return APPDATABASE
        }
    }


    abstract fun productDao(): ProductDao

}
