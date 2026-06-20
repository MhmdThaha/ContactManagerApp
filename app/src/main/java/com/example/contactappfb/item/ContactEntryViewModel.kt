package com.example.contactappfb.item

import android.provider.ContactsContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.contactappfb.data.Contact
import com.example.contactappfb.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.String

@HiltViewModel
class ContactEntryViewModel@Inject constructor (var contactRepository: ContactRepository)
    : ViewModel() {
    var contactUiState by mutableStateOf(ContactUiState())
        private set
//itemUiState = what the screen should show (name, price, qty)
//mutableStateOf = Compose will redraw the UI if state changes

    /**
     * Updates the [itemUiState] with the value provided in the argument.
     * This method also triggers
     * a validation for input values.
     */
    fun updateUiState(up: ConDetails) {
contactUiState = ContactUiState(ContactDetails=up,isEntryValid =  validateInput(up))
    }
    private fun validateInput(
        uiState: ConDetails = contactUiState.ContactDetails // default = current form data
    ): Boolean {
        return with(uiState) { // makes accessing fields cleaner: uiState.name → name
            name.isNotBlank()             // true if name has text
                    &&                    // AND → all must be true
                    phoneNumber.isNotBlank()            // true if price field not empty
                    &&                    // AND → all must be true
                    email.isNotBlank()         // true if quantity field not empty
        }
    }
    suspend fun saveItem() {
        if (validateInput()) {//
          /*  val contact = ConDetails(
                name = contactUiState.ContactDetails.name,
                phoneNumber = contactUiState.ContactDetails.phoneNumber,
                email = contactUiState.ContactDetails.email
            )*/

            contactRepository.insertContact(   contactUiState.ContactDetails.toContact())//contact)
        }//Checks if input is valid Converts the form data to an Item Inserts it into the database
    }
}
data class ContactUiState(
    val ContactDetails: ConDetails = ConDetails(),
    val isEntryValid: Boolean = false
)

data class ConDetails(
    var id:Int =0,
    var name: String= "",
    var phoneNumber: String = "",
    var email: String= "",
    var imageUri: String?=null,
//Room / database → String
//UI → Uri..for image
)

fun ConDetails.toContact(): Contact {
    return Contact(
        name = name,
        phoneNumber = phoneNumber,
        email = email,
       id = id,
        isActive = true,
        imageUri = imageUri,
        dateOfCreation = System.currentTimeMillis(),

    )
}
fun Contact.toContactUiState(isEntryValid: Boolean = false): ContactUiState {
    return ContactUiState(
        ContactDetails = this.toContactDetails(),
        isEntryValid = isEntryValid
    )
}
fun Contact.toContactDetails(): ConDetails {

    return ConDetails(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        imageUri = imageUri//Uri?.toString()   // IMPORTANT


//var dateOfCreation : Long,

    )
}
//fun Contacts.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
//    itemDetails = this.toItemDetails(),
//    isEntryValid = isEntryValid
//)F=