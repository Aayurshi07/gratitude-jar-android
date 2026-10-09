package com.example.gratitudejar

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.json.JSONArray

private val marbleColors = listOf(
    Color(0xFFE57373),
    Color(0xFFFFB74D),
    Color(0xFFFFD54F),
    Color(0xFF81C784),
    Color(0xFF64B5F6),
    Color(0xFFBA68C8)
)

private fun colorFor(note: String): Color {
    val index = (note.hashCode() and Int.MAX_VALUE) % marbleColors.size
    return marbleColors[index]
}

private const val PREFS_NAME = "gratitude_jar"
private const val KEY_NOTES = "notes"

private fun loadNotes(prefs: SharedPreferences): List<String> {
    val raw = prefs.getString(KEY_NOTES, null) ?: return emptyList()
    return try {
        val array = JSONArray(raw)
        List(array.length()) { array.getString(it) }
    } catch (e: Exception) {
        emptyList()
    }
}

private fun saveNotes(prefs: SharedPreferences, notes: List<String>) {
    prefs.edit().putString(KEY_NOTES, JSONArray(notes).toString()).apply()
}

@Composable
fun JarScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    var text by remember { mutableStateOf("") }
    val notes = remember { mutableStateListOf<String>().apply { addAll(loadNotes(prefs)) } }
    var memory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("My Gratitude Jar", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(if (notes.isEmpty()) "Your jar is empty" else "Marbles in jar: ${notes.size}")
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("What are you grateful for?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    notes.add(0, text.trim())
                    saveNotes(prefs, notes)
                    text = ""
                },
                enabled = text.isNotBlank()
            ) {
                Text("Drop a marble")
            }
            OutlinedButton(
                onClick = { memory = notes.random() },
                enabled = notes.isNotEmpty()
            ) {
                Text("Pull a memory")
            }
        }

        memory?.let { picked ->
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("A good moment", style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(picked, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notes) { note ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(colorFor(note), CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(note)
                    }
                }
            }
        }
    }
}