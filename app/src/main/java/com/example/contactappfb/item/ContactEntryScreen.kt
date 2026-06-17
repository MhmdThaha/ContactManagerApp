package com.example.jccontact.item

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jccontact.home.ContactTopAppBar
import com.example.jccontact.home.HomeViewModel
import kotlinx.coroutines.launch
import com.example.jccontact.R
import com.example.jccontact.navigation.NavigationDestination
import java.util.Currency
import java.util.Locale


object ContactEntryDestination : NavigationDestination {
    override val route = "item_entry"
    const val itemIdArg = "itemId"
   // override val titleRes = R.string.item_entry_title
   val routeWithArgs = "${route}/{$itemIdArg}"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ContactEntryViewModel = hiltViewModel(),
) {

    Scaffold(
        topBar = {
            ContactTopAppBar(
                       title = "Add",//stringResource(ItemEntryDestination.titleRes),
                       canNavigateBack = canNavigateBack,
                       navigateUp = onNavigateUp
                   )
        }
    ) { innerPadding ->
        val coroutineScope = rememberCoroutineScope()
        EntryBody(
            contactUiState = viewModel.contactUiState,//Send the current UI data state (e.g., item name, price, quantity) to the UI so it can show it in text fields.
            onItemValueChange = viewModel::updateUiState,//It gives the UI a function to call later when the user types, instead of calling it immediately.
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                } },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}



@Composable
fun EntryBody(
    contactUiState: ContactUiState,                      // current data for the text fields
    onItemValueChange: (ConDetails) -> Unit,// // function to update data when user types
    onSaveClick: () -> Unit,                        // function to run when Save is pressed
    modifier: Modifier = Modifier                   // styling modifiers (padding, width, etc.)

) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        ItemInputForm(
            conDetails = contactUiState.ContactDetails,// // send current field values to the form
            onValueChange = onItemValueChange,// send update function to the form
            modifier = Modifier.fillMaxWidth()     // make form use full horizontal space

        )
        Button(
            onClick = onSaveClick,                 // run save function when button pressed
            enabled = contactUiState.isEntryValid,    // only clickable when fields are valid
            shape = MaterialTheme.shapes.small,    // UI styling for button shape
            modifier = Modifier.fillMaxWidth()     // button takes full width
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}
@Composable                                  // This function builds a UI section in Compose
fun ItemInputForm(
    conDetails: ConDetails,              // Holds current text values for name/price/quantity
    modifier: Modifier = Modifier,           // Allows styling (padding, width, etc.) from outside
    onValueChange: (ConDetails) -> Unit = {}, // Function to send updated values back upward
    enabled: Boolean = true                  // Enables/disables input fields
) {
    Column(                                  // Stack all fields vertically
        modifier = modifier,                 // Apply styling passed in
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_medium)
        )                                    // Add vertical space between fields
    ) {
        OutlinedTextField(
            value = conDetails.name,        // Show current name text
            onValueChange = { onValueChange(conDetails.copy(name = it)) },
            // When user types -> copy itemDetails with new name -> send update up
            label = { Text(text ="name") }, // Field label
            leadingIcon = {

                    Icon(imageVector = Icons.Filled.Person, contentDescription = "")


            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),                               // Set background colors for field states
            modifier = Modifier.fillMaxWidth(), // Field uses full width
            enabled = enabled,               // Disable if enabled == false
            singleLine = true                // One-line input only
        )
        OutlinedTextField(
            value = conDetails.phoneNumber,       // Show current price text
            onValueChange = { onValueChange(conDetails.copy( phoneNumber = it)) },
            // When user types -> copy itemDetails with new price -> send update up
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            // Show decimal keyboard
            label = { Text(text = "mobile number") }, // Field label
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            // Set background colors for field states
//            leadingIcon = {
//                Text(Currency.getInstance(Locale.getDefault()).symbol)
//            },                               // Currency symbol (₹, $, etc.)
            modifier = Modifier.fillMaxWidth(), // Field uses full width
            enabled = enabled,               // Disable if enabled == false
            singleLine = true                // One-line input only
        )
        OutlinedTextField(
            value = conDetails.email,    // Show current quantity text
            onValueChange = { onValueChange(conDetails.copy(email = it)) },
            // When user types -> copy itemDetails with new quantity -> send update up
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            // Show number keyboard
            label = { Text(text="email") }, // Field label
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),                               // Set background colors for field states
            modifier = Modifier.fillMaxWidth(), // Field uses full width
            enabled = enabled,               // Disable if enabled == false
            singleLine = true                // One-line input only
        )
        if (enabled) {                       // Show note only when editing is allowed
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_medium)
                )                            // Small left padding
            )
        }
    }
}