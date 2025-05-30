package com.example.capstoneproject.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun TableHeaderCell(
    text: String,
    width: Dp,
    fontSize: TextUnit,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    fontWeight: FontWeight = FontWeight.SemiBold,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier

) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = textColor,
        textAlign = textAlign,
        modifier = modifier
            .width(width)
            .padding(horizontal = 8.dp),
        maxLines = 1
    )
}

@Composable
fun TableBodyCell(
    text: String,
    width: Dp,
    fontSize: TextUnit,
    textAlign: TextAlign = TextAlign.Center,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize,
        textAlign = textAlign,
        modifier = modifier
            .width(width)
            .padding(horizontal = 8.dp),
        maxLines = 1
    )
}
