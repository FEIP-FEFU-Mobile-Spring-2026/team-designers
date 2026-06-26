package com.feip.pinkpanther.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val showCheckoutMessage by viewModel.showCheckoutMessage.collectAsState()
    val showClearDialog by viewModel.showClearDialog.collectAsState()

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "🛒",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Корзина пуста",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Добавьте товары из каталога",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Заголовок
            Text(
                text = "Корзина",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF69B4),
                modifier = Modifier.padding(16.dp)
            )

            // Список товаров
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(cartItems) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                if (item.sizeName.isNotEmpty()) {
                                    Text(
                                        text = "Размер: ${item.sizeName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = viewModel.formatPrice(item.priceInKopecks * item.quantity),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF69B4)
                                )
                            }

                            // Количество
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.decreaseQuantity(index) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, "Уменьшить", modifier = Modifier.size(18.dp))
                                }
                                Text(
                                    text = "${item.quantity}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { viewModel.increaseQuantity(index) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, "Увеличить", modifier = Modifier.size(18.dp))
                                }
                            }

                            // Удалить
                            IconButton(onClick = { viewModel.removeItem(index) }) {
                                Icon(Icons.Default.Delete, "Удалить", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            // Нижняя панель
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Итого:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = viewModel.formatPrice(viewModel.getTotalPrice()),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF69B4)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.checkout() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4))
                    ) {
                        Text("Оформить", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = { viewModel.showClearDialog() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Очистить корзину", color = Color.Red)
                    }
                }
            }
        }
    }

    // Диалог подтверждения очистки
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissClearDialog() },
            title = { Text("Очистить корзину?") },
            text = { Text("Все товары будут удалены из корзины.") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearCart() }) {
                    Text("Очистить", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissClearDialog() }) {
                    Text("Отмена")
                }
            }
        )
    }

    // Сообщение об успешном оформлении
    if (showCheckoutMessage) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCheckoutMessage() },
            title = { Text("Заказ оформлен! 🎉") },
            text = { Text("Ваш заказ успешно оформлен. Спасибо за покупку!") },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissCheckoutMessage() }) {
                    Text("ОК", color = Color(0xFFFF69B4))
                }
            }
        )
    }
}