package com.example.contactappfb.item

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.contactappfb.ContactTopAppBar
import com.example.contactappfb.navigation.NavigationDestination
import com.example.contactappfb.ui.theme.ContactAppFBTheme
//import com.google.android.gms.cast.framework.media.ImagePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import kotlinx.coroutines.launch


object ContactEditDestination : NavigationDestination {
    override val route = "item_edit"
    const val itemIdArg = "itemId"
    val routeWithArgs = "${route}/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContactEditViewModel = hiltViewModel(),
) {
    val selectedImageUri =
        viewModel.itemUiState.ContactDetails.imageUri
            ?.let { Uri.parse(it) }

    val context = LocalContext.current
    val pickMedia =
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { selectedUri ->
                context.contentResolver.takePersistableUriPermission(
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            }
            viewModel.updateImage(uri)
        }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ContactTopAppBar(
                title = "EditScreen",
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            ImagePicker(
                selectedImageUri= selectedImageUri,
                imagePicker = pickMedia,
            )
            EntryBody(
                contactUiState = viewModel.itemUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
            )
        }
    }

    @Composable
    fun ImagePicker(
        selectedImageUri: Uri?,
        imagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedImageUri == null) {
                Button(
                    onClick = {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

}

@Composable
fun ImagePicker(
    selectedImageUri: Uri?,
    imagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    ContactAppFBTheme {
        ContactItemEditScreen(navigateBack = {}, onNavigateUp = {})
    }
}