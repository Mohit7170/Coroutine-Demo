package com.app.cannedmessage.ui.activties

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.cannedmessage.R
import com.app.cannedmessage.model.MessageModel
import com.app.cannedmessage.ui.theme.CannedMessageTheme
import com.app.cannedmessage.viewmodel.MessageViewModel


class CannedMessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CannedMessageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colors.background
                ) {

                    var addCanned by remember { mutableStateOf(false) }

                    val viewModel = ViewModelProvider(this)[MessageViewModel::class.java]

                    val items = remember { mutableStateListOf<MessageModel>() }

                    viewModel.getAllMessage(applicationContext).observe(this, Observer {
                        items.clear()
                        items.addAll(it)
                        Log.d(TAG, "SelectCannedMessage: Data -- $it")
                    })

                    if (addCanned) AddCannedMessage(viewModel) {
                        addCanned = !addCanned
                    } else {
                        SelectCannedMessage(viewModel, items) {
                            addCanned = !addCanned
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun AddCannedMessage(viewModel: MessageViewModel, action: () -> Unit) {
        val activity = LocalContext.current as Activity
        val kc = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        val placeholder = "Enter your message here..."
        var text by remember {
            mutableStateOf(TextFieldValue(""))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            TopBar(
                title = "Add Canned Message",
                trailIcon = R.drawable.ic_save,
                leasIconAction = { action() },
                trailIconAction = {
                    viewModel.addMessage(context = activity, MessageModel(message = text.text))
                    action()
                })


            BasicTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        kc?.hide()
                        focusManager.clearFocus()
                    }),
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface
                ),
                cursorBrush = SolidColor(colors.onSurface),
                decorationBox = {
                    if (text.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = colors.onSurface
//                            color = colorResource(id = R.color.placeholder)
                        )
                    }
                    it()
                },
            )
        }
    }

    companion object {
        private const val TAG = "CannedMessageActivity"
    }

    @Composable
    private fun SelectCannedMessage(
        viewModel: MessageViewModel,
        items: SnapshotStateList<MessageModel>,
        action: () -> Unit
    ) {

        val activity = LocalContext.current as Activity

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {

            TopBar(
                title = "Select Canned Message",
                trailIcon = R.drawable.ic_add_iv,
                leasIconAction = { activity.finish() },
                trailIconAction = { action() })

            LazyColumn {
                items(items) {
                    SingleRow(item = it, onClickAction = { item ->
                        viewModel.deleteMessage(activity, item)
                    })
                }
            }
        }
    }

    @Composable
    private fun TopBar(
        title: String,
        @DrawableRes trailIcon: Int,
        leasIconAction: () -> Unit,
        trailIconAction: () -> Unit
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { leasIconAction() }) {
                Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back Icon")
            }

            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            IconButton(onClick = { trailIconAction() }) {
                Icon(
                    painterResource(id = trailIcon),
                    contentDescription = "Add Message Button",
                    tint = Color.Unspecified
                )
            }
        }

    }

    @Composable
    private fun SingleRow(item: MessageModel, onClickAction: (MessageModel) -> Unit) {

        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight(0.08f)) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {
                    onClickAction(item)
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_rmove_iv),
                        contentDescription = "Delete Message Image",
                        tint = Color.Unspecified
                    )
                }

                Text(text = item.message, fontSize = 14.sp)

            }

            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(start = 48.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview2() {
//    MyApplicationTheme {
//        SelectCannedMessage()
//    }
//}