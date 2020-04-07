package com.napptilians.diskdatasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.napptilians.diskdatasource.Converters
import com.napptilians.diskdatasource.dao.ExampleDao
import com.napptilians.diskdatasource.models.MovieDbModel

@Database(version = 1, entities = [MovieDbModel::class])
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun movieDao(): ExampleDao

    companion object {

        // Singleton pattern.
        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, "doy-db")
                .fallbackToDestructiveMigration()
                .build()
    }
}