package com.feip.pinkpanther.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.feip.pinkpanther.models.Product
import com.feip.pinkpanther.models.Size

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailSheet(
    product: Product,
    onDismiss: () -> Unit,
    formatPrice: (Int) -> String,
    getCategoryName: (String) -> String,
    onAddToCart: (Product, Size?) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showInfoDialog by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf(product.sizes.firstOrNull()?.name ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Изображение с тегами
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Кнопка закрытия
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.Close, "Закрыть", tint = Color.White, modifier = Modifier.size(20.dp))
                }

                // Теги
                if (product.tags.isNotEmpty()) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        product.tags.forEach { tag ->
                            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFFF69B4).copy(alpha = 0.85f)) {
                                Text(text = tag, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Название
            Text(text = product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(8.dp))

            // Категория и кнопка (i)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = getCategoryName(product.categoryId), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                IconButton(onClick = { showInfoDialog = true }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Info, "Информация", tint = Color(0xFFFF69B4), modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Цена
            Text(text = formatPrice(product.priceInKopecks), style = MaterialTheme.typography.headlineMedium, color = Color(0xFFFF69B4), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))

            Spacer(modifier = Modifier.height(20.dp))

            Divider(modifier = Modifier.padding(horizontal = 20.dp), color = Color(0xFFFFB6C1))

            Spacer(modifier = Modifier.height(20.dp))

            // Размеры
            if (product.sizes.isNotEmpty()) {
                Text(text = "Размер", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.sizes.forEach { size ->
                        val isSelected = size.name == selectedSize
                        Surface(
                            modifier = Modifier.clickable { selectedSize = size.name },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFFF69B4) else Color(0xFFFFB6C1).copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = size.name,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) Color.White else Color(0xFFC71585),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Кнопка "В корзину"
            Button(
                onClick = {
                    val size = product.sizes.find { it.name == selectedSize }
                    onAddToCart(product, size)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4))
            ) {
                Text(text = "В корзину", style = MaterialTheme.typography.titleMedium, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Описание
            Text(text = "Описание", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.getDescription(), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 20.dp), color = Color.DarkGray)
        }
    }

    // Диалог с характеристиками
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text("Характеристики", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    if (product.material.isNotEmpty()) {
                        Text("Материал: ${product.material}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (product.weight.isNotEmpty()) {
                        Text("Вес: ${product.weight}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (product.season.isNotEmpty()) {
                        Text("Сезон: ${product.season}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    if (product.countryOfOrigin.isNotEmpty()) {
                        Text("Страна: ${product.countryOfOrigin}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("ОК", color = Color(0xFFFF69B4))
                }
            }
        )
    }
}