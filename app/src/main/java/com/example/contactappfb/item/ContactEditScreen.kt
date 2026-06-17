package com.example.jccontact.item

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.jccontact.home.ContactTopAppBar
import com.example.jccontact.navigation.NavigationDestination
import com.example.jccontact.ui.theme.JcContactTheme
import kotlinx.coroutines.launch


object ContactEditDestination : NavigationDestination {
    override val route = "item_edit"
    const val itemIdArg = "itemId"
    // override val titleRes = R.string.item_entry_title
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
    val selectedImageUri=
        viewModel.itemUiState.ContactDetails.imageUri
            ?.let { Uri.parse(it) }
   // same ..if (contact.imageUri != null) { Uri.parse(contact.imageUri) } else { null }
    /*DB stores image as String
↓
Convert String → Uri
↓
Used to show image*/
//
//    val imagePicker =
//        rememberLauncherForActivityResult(   // Launcher used to open another activity and get result
//            contract = ActivityResultContracts.GetContent()    // Contract that opens system file/gallery picker
//        ) { uri ->                                             // Callback triggered after user selects an image
//           // selectedImageUri =
//                viewModel.updateImage(uri)// Store selected image URI into state
//        }
    val context = LocalContext.current// Get current app context and store in variable//Without it:
    //You cannot open apps or use system features//Context = tool to talk to Android system (intents, toast, files)
    val pickMedia =
        rememberLauncherForActivityResult(//
            ActivityResultContracts.PickVisualMedia() //Tool to open gallery and pick image
       //Collection of predefined actions (gallery, camera, permission, etc.)..//PickVisualMedia → profile image
            //Future apps
            //GetContent → upload resume/pdf
            //RequestPermission → camera/location
            //TakePicture → capture photo
            //.OpenDocument()->Designed for any file type ,Opens full file browser//Shows ALL files including .sqlite
        ) { uri ->//uri = location of selected image (or null)
            uri?.let { selectedUri -> // let ="take this value and do something with it"
                context.contentResolver.takePersistableUriPermission( //Allow app to access image even after restart
                    // context //Access to Android system ,//contentResolver //Tool to access external data (images, files) //takePersistableUriPermission //Save permission permanently
                    selectedUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                    //Intent.
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
                selectedImageUri = selectedImageUri,
                imagePicker = pickMedia,
            )
        EntryBody(
contactUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState, //{ },
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be updated in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel. updateItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
              //  .verticalScroll(rememberScrollState())
        )
    }
}}

@Composable
fun ImagePicker(selectedImageUri: Uri?,
                imagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>){ //ActivityResultLauncher<String>){
    Column(                                                // Vertical layout container
        modifier = Modifier.fillMaxSize(),                 // Make column take the full screen
        verticalArrangement = Arrangement.Center,          // Center items vertically
        horizontalAlignment = Alignment.CenterHorizontally // Center items horizontally
    ) {
        if (selectedImageUri == null) {

            Button(
                onClick = { imagePicker.launch(PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )) }
            ) {
                Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
//                       .clickable { imagePicker.launch(PickVisualMediaRequest(
//                          ActivityResultContracts.PickVisualMedia.ImageOnly
//                        ))}
                )
            }

        } else {

            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = null, modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                  //  .clickable { imagePicker.launch("image/*") },
                contentScale = ContentScale.Crop
            )

        }

    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    JcContactTheme {
        ContactItemEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
    }
}