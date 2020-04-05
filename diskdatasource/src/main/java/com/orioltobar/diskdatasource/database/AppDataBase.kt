package com.orioltobar.diskdatasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.orioltobar.diskdatasource.Converters
import com.orioltobar.diskdatasource.dao.ExampleDao
import com.orioltobar.diskdatasource.models.MovieDbModel

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
            Room.databaseBuilder(context, AppDataBase::class.java, "androidklean-db")
                .fallbackToDestructiveMigration()
                .build()
    }
}