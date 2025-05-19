package com.example.a5

import android.R
import android.R.attr.onClick
import android.R.attr.text
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a5.ui.theme.A5Theme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.channels.SendChannel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val kierunkiViewModel = KierunkiViewModel()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A5Theme {
                var showDialog by remember { mutableStateOf(false) }

                Scaffold (floatingActionButton = { FloatingActionButton (onClick = { showDialog = true }) { Icon(Icons.Default.Add, "Add") } } )
                { padding ->
                    LazyColumn (modifier = Modifier.padding(padding)) {
                        items(kierunkiViewModel.kierunki.size) { index ->
                            val kierunek = kierunkiViewModel.kierunki[index]
                            KartaKierunku(
                                kierunek = kierunek,
                                onDelete = { kierunkiViewModel.removeKierunek(kierunek) },
                                onEdit = { updatedKierunek ->
                                    kierunkiViewModel.updateKierunek(updatedKierunek)
                                }
                            )
                        }
                    }


                    if (showDialog) {
                        InputDialog(
                            viewModel = kierunkiViewModel,
                            onDismiss = { showDialog = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun KartaKierunku(kierunek: KierunekPWR, onDelete: () -> Unit, onEdit: (KierunekPWR) -> Unit) {
    var showEditDialog by remember { mutableStateOf(false) }

    Row(modifier = (Modifier
        .padding(16.dp)
        .border(width = 1.dp, color = Color.Black)
        .fillMaxWidth())) {
        Text(kierunek.id.toString(), modifier = Modifier
            .background(color = Color.Red)
            .padding(16.dp))
        Column {
            Text(kierunek.skrot, modifier = Modifier
                .background(color = Color.Green)
                .padding(vertical = 4.dp)
                .width(150.dp))
            Text(kierunek.pelnaNazwa, modifier = Modifier
                .padding(vertical = 4.dp)
                .width(150.dp))
        }
        Button(onClick = { showEditDialog = true }) {
            Text("Edit", modifier = Modifier.padding(8.dp))
        }
        Button(onClick = { onDelete() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("X", modifier = Modifier.padding(8.dp))
        }
    }

    if (showEditDialog) {
        EditDialog(
            kierunek = kierunek,
            onDismiss = { showEditDialog = false },
            onSave = { updatedKierunek ->
                onEdit(updatedKierunek)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun InputDialog(viewModel: KierunkiViewModel, onDismiss: () -> Unit) {
    var skrot by remember { mutableStateOf("") }
    var nazwa by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj kierunek") },
        text = {
            Column {
                OutlinedTextField(
                    value = skrot,
                    onValueChange = { skrot = it },
                    label = { Text("Skrót") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nazwa,
                    onValueChange = { nazwa = it },
                    label = { Text("Nazwa") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.addKierunek(
                        skrot = skrot,
                        pelnaNazwa = nazwa
                    )
                    onDismiss()
                }
            ) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

@Composable
fun EditDialog(
    kierunek: KierunekPWR,
    onDismiss: () -> Unit,
    onSave: (KierunekPWR) -> Unit
) {
    var skrot by remember { mutableStateOf(kierunek.skrot) }
    var nazwa by remember { mutableStateOf(kierunek.pelnaNazwa) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Kierunek") },
        text = {
            Column {
                OutlinedTextField(
                    value = skrot,
                    onValueChange = { skrot = it },
                    label = { Text("Skrót") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nazwa,
                    onValueChange = { nazwa = it },
                    label = { Text("Pełna Nazwa") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(kierunek.copy(skrot = skrot, pelnaNazwa = nazwa))
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


data class KierunekPWR(
    val id: Int,
    val skrot: String,
    val pelnaNazwa: String
)

class KierunkiViewModel : ViewModel() {
    val kierunki = mutableStateListOf<KierunekPWR>()

    private var nextId = 1

    fun addKierunek(skrot: String, pelnaNazwa: String) {
        kierunki.add(KierunekPWR(id = nextId++, skrot = skrot, pelnaNazwa = pelnaNazwa))
    }

    fun removeKierunek(kierunek: KierunekPWR) {
        kierunki.remove(kierunek)
    }

    fun updateKierunek(kierunek: KierunekPWR){
        val index = kierunki.indexOfFirst { it.id == kierunek.id }
        if (index != -1) {
            kierunki[index] = kierunek
        }
    }
}