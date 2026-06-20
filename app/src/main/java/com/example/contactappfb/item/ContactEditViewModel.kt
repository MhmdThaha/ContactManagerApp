package com.example.contactappfb.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactappfb.data.Contact
//import com.example.contactappfb.home.ContUiState
import com.example.contactappfb.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactEditViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,//It's a key-value map that survives process death
    // and configuration changes. Hilt provides it automatically — you don't create it, you don't put it in any Module. Just add it as a parameter and Hilt handles it.

    private val itemsRepository: ContactRepository
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    var itemUiState by mutableStateOf(ContactUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ContactEditDestination.itemIdArg])
    init {//runs automatically when ViewModel starts
        // init runs automatically when the ViewModel is created.
        // Meaning: "When Edit screen starts, do this automatically."
        viewModelScope.launch {//        // Start a coroutine because database work must NOT run on main thread.
            itemUiState = itemsRepository.getContactStream(itemId)//itemId comes from navigation argument
                //We fetch item from DB using getItemStream(itemId)
                // Ask database: "Give me the item with this id"
                // It returns a Flow (data that can emit values over time)
                .filterNotNull()// // Sometimes Flow can emit null.
                // This line says: "Ignore null. Wait until real item comes."
                .first()//means “give me item one time”//// Flow keeps listening forever.
                // But for Edit screen, we need item only once.
                // first() = "give me the first real item and stop"
                .toContactUiState(true)
            //  // Convert database Item → UI state (ItemUiState)
            //            // true means: enable Save button initially
        }
    }
    private fun validateInput(uiState: ConDetails = itemUiState.ContactDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank()
        }
    }
    fun updateUiState(conDetails: ConDetails) {//Whenever user types, update the form values AND update whether Save button should be enabled.”
        itemUiState =
            ContactUiState(ContactDetails = conDetails, isEntryValid = validateInput(conDetails))
        //Throw away old UI state, store the new one”.
    }
    //URI selected
    //convert to String ↓
    //store in UI state ↓
    //Compose recomposes
    fun updateImage(uri: Uri?) {

        val updatedDetails =
            itemUiState.ContactDetails.copy(
                imageUri = uri?.toString()
            )

        itemUiState =
            itemUiState.copy(ContactDetails = updatedDetails)

    }
    suspend fun updateItem() {
        if (validateInput(itemUiState.ContactDetails)) {
            itemsRepository.updateContact(itemUiState.ContactDetails.toContact())
        }
    }
  /*  private val itemIds: Int = checkNotNull(savedStateHandle
        [ ContactEditDestination.itemIdArg])
    val uiState: StateFlow<ContUiState> =
        itemsRepository.getContactStream(itemIds)
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
            )*/

}
data class ContUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ConDetails = ConDetails()
)