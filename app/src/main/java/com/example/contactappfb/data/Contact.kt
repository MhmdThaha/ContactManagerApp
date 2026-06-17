package com.example.jccontact.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jccontact.item.ConDetails

@Entity(tableName = "contacts_table")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var phoneNumber: String,
    var email: String,
    var isActive: Boolean,
    var dateOfCreation : Long,
    var imageUri : String? = null
)

