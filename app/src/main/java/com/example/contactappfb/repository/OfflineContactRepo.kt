package com.example.contactappfb.repository

import com.example.contactappfb.data.Contact
import com.example.contactappfb.data.ContactDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class OfflineContactsRepository @Inject constructor(private var contactDao: ContactDao): ContactRepository {
    //OfflineItemsRepository doesn’t add logic yet—it just passes requests to the database in a clean way.
    override fun getAllContactStream(): Flow<List<Contact>> {
return contactDao.getAllContact()
    }

    override fun getContactStream(id: Int): Flow<Contact> {
      return contactDao.getItem(id)
    }

    override suspend fun insertContact(contact: Contact) {
      return contactDao.insert(contact)
    }

    override suspend fun deleteContact(contact: Contact) {
        return contactDao.delete(contact)
    }

    override suspend fun updateContact(contact: Contact) {
     return contactDao.update(contact)
    }

}