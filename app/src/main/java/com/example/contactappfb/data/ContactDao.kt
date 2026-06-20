package com.example.contactappfb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Query("SELECT * FROM contacts_table")
    fun getAllContact(): Flow<List<Contact>>

    @Query("SELECT * from contacts_table WHERE id = :id")
    fun getItem(id: Int): Flow<Contact>
    @Insert
    suspend fun insert(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Update
    suspend fun update(contact: Contact)
}