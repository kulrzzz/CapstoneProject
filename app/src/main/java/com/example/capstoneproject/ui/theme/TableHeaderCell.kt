package com.example.capstoneproject.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun TableHeaderCell(text: String, width: Dp, textSize: TextUnit) {
    Text(
        text = text,
        fontSize = textSize,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .width(width)
            .padding(horizontal = 4.dp)
    )
}
