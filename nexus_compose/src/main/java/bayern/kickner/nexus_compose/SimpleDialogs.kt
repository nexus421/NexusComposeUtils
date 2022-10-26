package bayern.kickner.nexus_compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoDialog(show: Boolean, msg: String, onButtonClick: () -> Unit) =
    Dialog(
        show = show,
        msg = msg,
        title = "Info",
        borderColor = Color.Blue,
        onButtonClick = onButtonClick
    )

@Composable
fun ErrorDialog(show: Boolean, msg: String, onButtonClick: () -> Unit) =
    Dialog(
        show = show,
        msg = msg,
        title = "Fehler",
        borderColor = Color.Red,
        onButtonClick = onButtonClick
    )

@Composable
fun HintDialog(show: Boolean, msg: String, onButtonClick: () -> Unit) =
    Dialog(
        show = show,
        msg = msg,
        title = "Hinweis",
        borderColor = Color.Yellow,
        onButtonClick = onButtonClick
    )

@Composable
fun SuccessDialog(show: Boolean, msg: String, onButtonClick: () -> Unit) =
    Dialog(
        show = show,
        msg = msg,
        title = "Erfolg",
        borderColor = Color.Green,
        onButtonClick = onButtonClick
    )

@Composable
fun DefaultDialog(show: Boolean, msg: String, title: String?, onButtonClick: () -> Unit) =
    Dialog(
        show = show,
        msg = msg,
        title = title,
        borderColor = Color.Gray,
        onButtonClick = onButtonClick
    )

/**
 * Dialog zur Anzeige einer einfachen Hinweismeldung.
 * Die entsprechenden Implementierungen darüber zeigen einen farbigen Rahmen, je nach Art der Meldung.
 *
 * Wichtig: Die Anzeige muss selbstständig geregelt werden.
 * Bsp.:
 * var showDialog by remember { mutableStateOf(false) }
 * ... showDialog = true ...
 * if(showDialog){
 *      InfoDialog(msg = "Message") { showDialog = false }
 * }
 */
@Composable
private fun Dialog(
    show: Boolean,
    msg: String,
    title: String? = null,
    borderSize: Dp = 3.dp,
    borderColor: Color = Color.White,
    textSizeMsg: TextUnit = 18.sp,
    onButtonClick: () -> Unit
) {
    if (show) {
        AlertDialog(onDismissRequest = { }, confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(borderColor),
                onClick = { onButtonClick() }) {
                Text(
                    text = "OK",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }, text = {
            Column {
                if (!title.isNullOrBlank())
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        fontSize = (textSizeMsg.value).sp
                    )
                Text(text = msg, fontSize = textSizeMsg)
            }
        }, shape = MaterialTheme.shapes.large,
            modifier = Modifier.border(borderSize, borderColor, shape = MaterialTheme.shapes.large)
        )
    }
}

/**
 * Anzeigen eines einfaches Dialogs mit beliebigem Content
 */
@Composable
fun SimpleDialog(
    show: Boolean,
    title: String? = null,
    onFinished: () -> Unit,
    content: @Composable () -> Unit
) {
    if (show) {
        AlertDialog(onDismissRequest = {}, confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(), onClick = { onFinished() }) {
                Text(
                    text = "OK", fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
            }
        }, title = {
            if (title != null)
                Text(text = title, modifier = Modifier.padding(bottom = 16.dp))
        }, text = content, shape = MaterialTheme.shapes.large,
            modifier = Modifier.border(3.dp, Color.White, shape = MaterialTheme.shapes.large)
        )
    }
}

/**
 * Anzeige einer einfachen Texteingabe in Form eines Dialogs.
 */
@Composable
fun ManualInputDialog(
    show: Boolean,
    title: String? = null,
    inputHint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFinished: (String) -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    if (show) {
        var error by remember { mutableStateOf("") }
        var input by remember { mutableStateOf("") }

        AlertDialog(onDismissRequest = {
            if (onDismiss != null) onDismiss()
        }, confirmButton = {
            Button(colors = ButtonDefaults.buttonColors(), onClick = {
                if (input.isBlank()) {
                    error = "Eingabe prüfen"
                } else onFinished(input)
            }) {
                Text(
                    text = "OK",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }, text = {
            Column {
                if (title != null) {
                    Text(
                        text = title, modifier = Modifier
                            .fillMaxWidth(), textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedTextField(
                    value = input,
                    label = {
                        Text(text = inputHint, fontSize = 20.sp)
                    },
                    onValueChange = {
                        input = it
                        error = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    isError = error.isNotBlank()
                )

                if (error.isNotBlank()) {
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }, shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .border(3.dp, Color.White, shape = MaterialTheme.shapes.large)
                .fillMaxWidth()
        )
    }
}

@Composable
fun TwoButtonDialog(
    show: Boolean,
    title: String = "App beenden",
    msg: String = "Möchten Sie die App beenden?",
    leftButtonLabel: String,
    rightButtonLabel: String,
    leftButton: () -> Unit,
    rightButton: () -> Unit
) {
    if (show) {
        AlertDialog(onDismissRequest = {}, buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Button(colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { leftButton() }) {
                    Text(
                        text = leftButtonLabel,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    onClick = { rightButton() }) {
                    Text(
                        text = rightButtonLabel,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                }
            }
        }, title = {
            Text(text = title, modifier = Modifier.padding(bottom = 16.dp), fontSize = 20.sp)
        }, text = {
            Text(text = msg, fontSize = 20.sp)
        }, shape = MaterialTheme.shapes.large,
            modifier = Modifier.border(3.dp, Color.White, shape = MaterialTheme.shapes.large)
        )
    }
}
