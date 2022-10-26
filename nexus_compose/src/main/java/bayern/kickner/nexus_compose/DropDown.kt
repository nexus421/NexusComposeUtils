package bayern.kickner.nexus_compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

/**
 * Einfaches DropDown-Feld
 *
 * @param preSelected Wert der im DropDown am Anfang angezeigt werden soll (Pre selected Item to show)
 * @param items Alle Items die im Dropdown angezeigt werden sollen (List of Items which should be available in the DropownMenu)
 * @param onItemClicked Wird aufgerufen, wenn auf ein Item im DropDown geklickt wird. Bekommt das aktuelle Item übergeben. (Called when Dropdown item is clicked)
 * @param content Definiert, wie ein einzelnes DropDown-Feld aussehen soll. Bekommt das aktuelle Item übergeben. Wenn das
 * übergebene Item null ist, so handelt es sich nicht um ein DropDown-Feld, sondern das Feld, welches die
 * aktuelle Auswahl anzeigt. Hier bietet sich dann ein "Bitte auswählen" an. (Defines the look of an DropdownMenuItem with the item as parameter. If the parameter is null,
 * it is not a DropdownMenuItem. It is the View which shows the current selected item. Normally it is good to make here a simple Text-View with "Please choose x")
 *
 * @author MK und MS
 */
@Composable
fun <T> Dropdown(
    label: String? = null,
    preSelected: T? = null,
    items: List<T>,
    onItemClicked: (T) -> Unit,
    content: @Composable RowScope.(T?) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.large)
            .padding(8.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        val rotationState by animateFloatAsState(
            targetValue = if (expanded) 0f else 180f
        )
        var textfieldSize by remember { mutableStateOf(Size.Zero) }
        Column {
            if (label != null)
                Text(text = label, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedButton(
                content = {
                    content(preSelected)
                    Icon(
                        Icons.Default.KeyboardArrowUp, "Dropdown",
                        Modifier
                            .clickable { expanded = !expanded }
                            .rotate(rotationState),
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textfieldSize = coordinates.size.toSize()
                    },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.onPrimary),
                shape = MaterialTheme.shapes.large,
                onClick = { expanded = !expanded },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
//                    .requiredSizeIn(maxHeight = with(LocalDensity.current) { (LocalConfiguration.current.screenHeightDp.toFloat()).toDp() })
            ) {
                items.forEach {
                    DropdownMenuItem(onClick = {
                        expanded = false
                        onItemClicked(it)
                    }) {
                        content(it)
                    }
                }
            }
        }
    }
}
