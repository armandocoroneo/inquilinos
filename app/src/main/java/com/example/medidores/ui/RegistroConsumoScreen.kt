package com.example.medidores.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkBg = Color(0xFF121212)
val CardBg = Color(0xFF1E1E1E)
val InputBg = Color(0xFF2A2A2A)
val GreenBtn = Color(0xFF2E7D32)
val RedBtn = Color(0xFFD32F2F)
val BlueBtn = Color(0xFF1976D2)
val TextGray = Color(0xFFB0B0B0)

data class Registro(
    val medidor: String,
    val fecha: String,
    val lecturaAnterior: Double,
    val lecturaPosterior: Double,
    val precioKw: Double,
    val descripcion: String
) {
    val consumo: Double get() = lecturaPosterior - lecturaAnterior
    val total: Double get() = consumo * precioKw
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroConsumoScreen() {
    var medidor by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("31/07/2026") }
    var lecturaAnterior by remember { mutableStateOf("") }
    var lecturaPosterior by remember { mutableStateOf("") }
    var precioKw by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    val registros = remember { mutableStateListOf<Registro>() }

    val historialMedidorActual = registros.filter {
        it.medidor.trim().equals(medidor.trim(), ignoreCase = true) && medidor.isNotBlank()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.List, null, tint = Color.White, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Registro Consumo", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Row {
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)), shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) { Text("Entrar", color = Color.White, fontSize = 11.sp) }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = RedBtn), shape = RoundedCornerShape(4.dp), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) { Text("Salir", color = Color.White, fontSize = 11.sp) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardBg), shape = RoundedCornerShape(6.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Agregar nuevo", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

                FormLabel("Medidor:")
                CustomTextField(value = medidor, onValueChange = { medidor = it })

                FormLabel("Fecha:")
                CustomTextField(value = fecha, onValueChange = { fecha = it }, trailingIcon = { Icon(Icons.Default.DateRange, null, tint = BlueBtn) })

                FormLabel("Lectura anterior:")
                CustomTextField(value = lecturaAnterior, onValueChange = { lecturaAnterior = it }, isNumeric = true)

                FormLabel("Lectura posterior:")
                CustomTextField(value = lecturaPosterior, onValueChange = { lecturaPosterior = it }, isNumeric = true)

                FormLabel("Precio por kW:")
                CustomTextField(value = precioKw, onValueChange = { precioKw = it }, isNumeric = true)

                FormLabel("Descripción:")
                CustomTextField(value = descripcion, onValueChange = { descripcion = it }, singleLine = false)

                Spacer(modifier = Modifier.height(12.dp))
                Text("📷 Foto (opcional)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = BlueBtn), shape = RoundedCornerShape(4.dp)) { Text("Cámara", fontSize = 13.sp) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = BlueBtn), shape = RoundedCornerShape(4.dp)) { Text("Galería", fontSize = 13.sp) }
                }

                if (errorMsg.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMsg, color = RedBtn, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val anterior = lecturaAnterior.toDoubleOrNull()
                        val posterior = lecturaPosterior.toDoubleOrNull()
                        val precio = precioKw.toDoubleOrNull()
                        when {
                            medidor.isBlank() -> errorMsg = "Ingresá el nombre del medidor"
                            anterior == null -> errorMsg = "Lectura anterior inválida"
                            posterior == null -> errorMsg = "Lectura posterior inválida"
                            precio == null -> errorMsg = "Precio por kW inválido"
                            posterior < anterior -> errorMsg = "La lectura posterior no puede ser menor a la anterior"
                            else -> {
                                errorMsg = ""
                                registros.add(
                                    Registro(
                                        medidor = medidor,
                                        fecha = fecha,
                                        lecturaAnterior = anterior,
                                        lecturaPosterior = posterior,
                                        precioKw = precio,
                                        descripcion = descripcion
                                    )
                                )
                                lecturaAnterior = ""
                                lecturaPosterior = ""
                                precioKw = ""
                                descripcion = ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenBtn),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Guardar registro", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CardBg), shape = RoundedCornerShape(6.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = true, onCheckedChange = {}, colors = CheckboxDefaults.colors(checkedColor = RedBtn))
                    Text("Consumo: altura = consumo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    IndicatorDot(Color(0xFF4CAF50), "Bajo")
                    Spacer(modifier = Modifier.width(12.dp))
                    IndicatorDot(Color(0xFFFFEB3B), "Medio")
                    Spacer(modifier = Modifier.width(12.dp))
                    IndicatorDot(Color(0xFFFF5722), "Alto")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            if (medidor.isBlank()) "📊 Historial" else "📊 Historial de \"$medidor\"",
            color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        TableLayout(historialMedidorActual)
    }
}

@Composable fun FormLabel(text: String) { Text(text = text, color = Color.LightGray, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, isNumeric: Boolean = false, singleLine: Boolean = true, trailingIcon: @Composable (() -> Unit)? = null) {
    TextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth().height(if (singleLine) 56.dp else 100.dp), colors = TextFieldDefaults.textFieldColors(containerColor = InputBg, focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedIndicatorColor = BlueBtn, unfocusedIndicatorColor = Color.Transparent), shape = RoundedCornerShape(4.dp), singleLine = singleLine, trailingIcon = trailingIcon, keyboardOptions = KeyboardOptions(keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text))
}

@Composable fun IndicatorDot(color: Color, label: String) { Row(verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(8.dp).background(color, shape = CircleShape)); Spacer(modifier = Modifier.width(4.dp)); Text(label, color = TextGray, fontSize = 12.sp) } }

@Composable
fun TableLayout(registros: List<Registro>) {
    Column(modifier = Modifier.fillMaxWidth().background(CardBg, shape = RoundedCornerShape(4.dp)).padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth().background(InputBg).padding(8.dp)) {
            listOf("Fecha", "Medidor", "Consumo", "Total").forEach { Text(it, color = BlueBtn, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)) }
        }
        if (registros.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { Text("No hay registros en esta página", color = TextGray, fontSize = 12.sp) }
        } else {
            registros.forEach { r ->
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text(r.fecha, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text(r.medidor, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text("%.1f".format(r.consumo), color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    Text("%.2f".format(r.total), color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
