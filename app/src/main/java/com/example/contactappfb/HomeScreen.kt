package com.example.jccontact.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.jccontact.data.Contact
import com.example.jccontact.R
import com.example.jccontact.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"

}
/*List → ID from item
Details → ID from ViewModel state
Edit → ID from navigation argument*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
 //   navigateToItemUpdate: (Int) -> Unit,
    navigateToEditItem: (Int) -> Unit,
    vModel: HomeViewModel = hiltViewModel(),//= viewModel()(factory = AppViewModelProvider.Fatory)

    modifier: Modifier = Modifier,
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val homies by vModel.homeUiState.collectAsState()

    //var selectedImageUri by remember {        // State variable remembered across recompositions
      //  mutableStateOf<Uri?>(null)            // Holds the selected image URI (null = no image yet)
    //}
    //val uri = Contact.//imageUri?.let { Uri.parse(it) }

    Scaffold(
        modifier = modifier.nestedScroll(
            scrollBehavior.nestedScrollConnection,

            //   floatingActionButtonPosition = FabPosition.Center
        ),
        // Connects scrolling content with TopAppBar animation

        topBar = {
            ContactTopAppBar(
                title = "Contact",//stringResource(HomeDestination.titleRes),
                // Uses app name as title

                canNavigateBack = false,
                // Home screen has no back button

                scrollBehavior = scrollBehavior
                // Applies scroll behavior to TopAppBar
            )
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = {
                    navigateToItemEntry()
                } ,//navigateToItemEntry,
                // Clicking FAB navigates to Add Item screen

                shape = MaterialTheme.shapes.medium,
                // Uses theme-defined shape

                modifier = Modifier.padding(
                    dimensionResource(id = R.dimen.padding_large)
                )
                // Adds spacing around FAB
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    // Shows + icon

                    contentDescription = stringResource(R.string.item_entry_title)
                    // Accessibility text
                )

            }

        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        // Padding given by Scaffold (top bar + FAB space)

        HomeBody(
            itemList = homies.itemList,
            // Passes list of items from ViewModel state

            onItemClick = {    it->
                navigateToEditItem(it)} ,//navigateToItemUpdate,
            // Passes navigation lambda down

            modifier = modifier.fillMaxSize(),
            // Makes body fill screen

            contentPadding = innerPadding,
            // Applies scaffold padding to content
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Contact>,
    // List of inventory items to display

    onItemClick: (Int) -> Unit,
    // Callback when user clicks an item (itemId)

    modifier: Modifier = Modifier,

    contentPadding: PaddingValues = PaddingValues(0.dp),
    // Padding from Scaffold
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        // Centers children horizontally

        modifier = modifier,
    ) {

        if (itemList.isEmpty()) {
            // If there are no items in database
           // EmptyAnimation()
            Text(
                text = stringResource(R.string.no_item_description),
                // Shows "No items" message

                textAlign = TextAlign.Center,
                // Centers text

                style = MaterialTheme.typography.titleLarge,
                // Applies text style

                modifier = Modifier.padding(contentPadding),
                // Respects scaffold padding
            )

        } else {
            // If items exist

            ContactList(
                itemList = itemList,
                // Passes list to LazyColumn

                onItemClick = { onItemClick(it.id) },
                // Extracts itemId and forwards it

                contentPadding = contentPadding,
                // Passes padding down

                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen.padding_small
                    )
                )
                // Adds horizontal spacing
            )
        }


    }}


@Composable
private fun ContactList(
    itemList: List<Contact>,
    // Items to render in list

    onItemClick: (Contact) -> Unit,
    // Callback with clicked Item

    contentPadding: PaddingValues,

    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        // Vertical scrolling list

        contentPadding = contentPadding
        // Applies padding
    ) {

        items(items = itemList, key = { it.id }
            // Stable key for efficient recomposition
        ) { item ->

            ContactItem(
                contact = item,
                // Passes item to card

                modifier = Modifier
                    .padding(
                        dimensionResource(id = R.dimen.padding_small)
                    )
                    // Space between cards

                    .clickable {
                        onItemClick(item)
                    }
                // Handles item click
            )
        }
    }
}



@Composable
private fun ContactItem(
    contact: Contact,
    // Single inventory item

    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier,
        // Card wrapper

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
        // Slight shadow
    ) {
        val uri = contact.imageUri?.let { Uri.parse(it) }//string ->uri
//DB stores String
//Image needs Uri ..

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

//            Image(
//                painter = rememberAsyncImagePainter(contact.imageUri),
//                contentDescription = null,
//                modifier = Modifier.size(80.dp),
//                contentScale = ContentScale.Crop
//            )

            AsyncImage(
                model = uri,//contact.imageUri,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column (modifier = Modifier.weight(1f)) {

                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, // big word become ...
                         fontSize = 16.sp,
                        )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = contact.phoneNumber,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = contact.email,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                           // modifier = Modifier.padding(16.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (contact.phoneNumber.isNotBlank()) {

                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${contact.phoneNumber}")
                                }


                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = Color.Blue
                        )
                    }
                }
            }
        }


    }
}


//
//@Composable                                   // Marks this function as a Compose UI function
//fun ImagePhotoPicker() {                      // Composable that shows button + selected image
//
//    var selectedImageUri by remember {        // State variable remembered across recompositions
//        mutableStateOf<Uri?>(null)            // Holds the selected image URI (null = no image yet)
//    }
//
//    val imagePicker =
//        rememberLauncherForActivityResult(   // Launcher used to open another activity and get result
//            contract = ActivityResultContracts.GetContent()    // Contract that opens system file/gallery picker
//        ) { uri ->                                             // Callback triggered after user selects an image
//            selectedImageUri =
//                uri                             // Store selected image URI into state
//        }
//
//
//        Button(                                            // Button UI element
//            onClick = { imagePicker.launch("image/*") }    // Open gallery when button is clicked
//        ) {
//            if(selectedImageUri==null) {
//                Text("Add Photo")
//            }else{
//                selectedImageUri?.let { uri ->                     // Only run if image URI is not null
//                    Image(                                         // Compose image element
//                        painter = rememberAsyncImagePainter(uri),  // Load image from URI using Coil
//                        contentDescription = null,                 // Accessibility description (null if unnecessary)
//                        modifier = Modifier.size(150.dp)           // Limit image size so it displays properly
//                    )
//                }
//            }
//        }
//
//
//
//    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {

    val sampleContact = Contact(
        id = 1,
        name = "John Doe",
        phoneNumber = "9876543210",
        email = "john@example.com",
        imageUri = null,//"https://picsum.photos/300",
        isActive =true,
        dateOfCreation = 1012001L ,
    )

    ContactItem(
        contact = sampleContact
    )
}