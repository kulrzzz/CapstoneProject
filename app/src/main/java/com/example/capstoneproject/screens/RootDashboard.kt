// RootDashboard.kt
package com.example.capstoneproject.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneproject.navigation.Screen

@Composable
fun RootDashboard(onNavigate: (Screen) -> Unit, onLogout: () -> Unit) {
    val menuItems = listOf(
        "CRUD Admin" to Screen.CrudAdmin,
        "CRUD Ruangan" to Screen.CrudRuangan
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Root Dashboard",
            style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        menuItems.forEach { (label, screen) ->
            Button(
                onClick = { onNavigate(screen) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = label, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onLogout, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
            Text("Logout", color = Color.White)
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1024dp,height=768dp,dpi=240"
)
@Composable
fun RootDashboardTabletPreview() {
    MaterialTheme {
        RootDashboard(
            onNavigate = {},
            onLogout = {}
        )
    }
}
