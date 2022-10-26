package bayern.kickner.composeutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import bayern.kickner.composeutils.ui.theme.ComposeUtilsTheme
import bayern.kickner.nexus_compose.Dropdown

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeUtilsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        Greeting("Android")

                        var selected by remember {
                            mutableStateOf("")
                        }

                        Dropdown(label = "Test", items = listOf("Apfel", "Banane", "Kokosnus", "Kartoffel"), onItemClicked = {
                            selected = it
                        }) {
                            if(it == null) {
                                Text(text = "Bitte auswählen")
                            } else {
                                Text(text = it)
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeUtilsTheme {
        Greeting("Android")
    }
}