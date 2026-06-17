package com.example.jccontact.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Contact::class], version = 3)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun todoDao(): ContactDao

    companion object {

        fun getDatabase(context: Context): ContactDatabase {
            return Room.databaseBuilder(
                context,
                ContactDatabase::class.java,
                "contact_db"
            ).build()
        }
    }
}