package com.example.composepractice.components

import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density

class SquashedOval : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            // 我們創建一個從容器寬度的 ¼ 開始，到容器寬度的 ¾ 結束的橢圓.
            addOval(
                androidx.compose.ui.geometry.Rect(
                    left = size.width / 4f,
                    top = 0f,
                    right = size.width * 3 / 4,
                    bottom = size.height
                )
            )
        }
        return Outline.Generic(path = path)
    }
}