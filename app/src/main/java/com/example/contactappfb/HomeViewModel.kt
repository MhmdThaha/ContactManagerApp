package com.example.contactappfb.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactappfb.data.Contact
import com.example.contactappfb.item.ConDetails
import com.example.contactappfb.item.ContactEditDestination
import com.example.contactappfb.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.contactappfb.item.toContactDetails



/**
 * ViewModel to retrieve all items in the Room database.
 */
@HiltViewModel
class HomeViewModel @Inject constructor (var contactRepository: ContactRepository,
                                         //savedStateHandle: SavedStateHandle,
    ) :
    ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    val homeUiState: StateFlow<ContactsUiState> =
        contactRepository.getAllContactStream()
            .map { ContactsUiState(it) }
//map means: “take this thing and turn it into another thing.”
            .stateIn(
                scope = viewModelScope,//“Keep this StateFlow alive as long as ViewModel is alive.”
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),//“Only collect data when UI is watching.” //   If UI goes away:
                //   Wait 5 seconds //  Stop collecting // Save battery & memory
                initialValue = ContactsUiState()//“Before any data arrives, give UI a default value.”
            )
    /*
  private val itemId: Int = checkNotNull(savedStateHandle
      [ ContactEditDestination.itemIdArg])
    val uiState: StateFlow< ContUiState> =
        contactRepository.getContactStream(itemId)
            .filterNotNull()
            .map {
                ContUiState(
                    outOfStock = true,
                    itemDetails = it.toContactDetails()

                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ContUiState()
            )
    */

}

/**
 * Ui State for HomeScreen
 */
data class ContactsUiState(val itemList: List<Contact> = listOf())
//data class ContactsUiState(val itemList: List<Contact> = listOf())
//data class ContUiState( val outOfStock: Boolean = true,//we passed here default
//                        val itemDetails: ConDetails = ConDetails(),)
//outOfStock: true/false
//itemDetails: contains properties like name, price, quantity
//This is just a container for data that the screen shows.)