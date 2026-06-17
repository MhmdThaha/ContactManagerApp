package com.example.jccontact.repository

import com.example.jccontact.data.Contact
import kotlinx.coroutines.flow.Flow


interface ContactRepository{
    fun getAllContactStream(): Flow<List<Contact>>
    //Get every item from DB and keep watching for changes.

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getContactStream(id: Int): Flow<Contact?>
//Get one item by its id and keep watching for changes.
    /**
     * Insert item in the data source
     */
    suspend fun insertContact(contact: Contact)
//Get one item by its id and keep watching for changes.
    /**
     * Delete item from the data source
     */
    suspend fun deleteContact(contact: Contact)
    //Remove a row from the DB.
    /**
     * Update item in the data source
     */
    suspend fun updateContact(contact: Contact)
    //Modify a row in the DB.
}