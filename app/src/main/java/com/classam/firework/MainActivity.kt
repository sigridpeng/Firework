package com.classam.firework

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import com.classam.firework.firework.FireworkNormal
import com.classam.firework.firework.FireworkOrb

class MainActivity3 : AppCompatActivity() {
    private var viewIndex = 0
    private lateinit var clMain: ConstraintLayout
    private var composeId = 0
    private var randomFireworkNumber: Int = 0
    private var randomLineColorNumber: Int = 0
    private var randomOrbColorNumber: Int = 0
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clMain = findViewById(R.id.cl_main)
        clMain.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Add a ComposeView at the clicked position.
                randomFireworkNumber = (0..1).random()
                randomLineColorNumber = (0..5).random()
                randomOrbColorNumber = (0..5).random()
                viewIndex++
                setComposeView(event.x.toInt(), event.y.toInt())
                return@setOnTouchListener true
            }
            return@setOnTouchListener false }
    }

    private fun setComposeView(x: Int, y: Int) {
        clMain.addView(ComposeView(this).apply {
            composeId = resources.getIdentifier(viewIndex.toString(), "id", packageName)
            id = composeId
            setContent {
                val configuration = LocalConfiguration.current
                val density = LocalDensity.current.density
                val screenHeightPx = (configuration.screenHeightDp * density).toInt()
                // The ratio of the fireworks width to the screen width is 4:5.
                val fireworkWidth = configuration.screenWidthDp / 1.25f
                val fireworkHeight = configuration.screenHeightDp
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {

                    Box(modifier = Modifier.graphicsLayer {
                        // Set the x-coordinate of the click position
                        // as the center of the fireworks component's width.
                        translationX = (x-(dpToPx(fireworkWidth / 2)))
                        // Set the y-coordinate of the click position
                        // at a distance of 1/4 from the top of the fireworks component.
                        translationY = ((y-screenHeightPx/4)).toFloat()
                    }){
                        val colorLine = randomColor(randomLineColorNumber)
                        val colorOrb = randomColor(randomOrbColorNumber)
                        if (randomFireworkNumber == 0) {
                            FireworkNormal(
                                width = fireworkWidth.dp,
                                height = fireworkHeight.dp,
                                colorLine = colorLine,
                                colorOrb = colorOrb)
                        } else {
                            FireworkOrb(
                                width = fireworkWidth.dp,
                                height = fireworkHeight.dp,
                                colorLine = colorLine,
                                colorOrb = colorOrb,
                                orbRadius = (randomOrbColorNumber+1).dp)
                        }
                    }
                }
            }
        })
    }
}

fun Context.dpToPx(dpValue: Float): Float {
    return dpValue * (resources.displayMetrics.densityDpi / 160f)
}

fun randomColor(randomNumber: Int): Color {
    val color = when(randomNumber) {
        0 -> Color.White
        1 -> Color.Cyan
        2 -> Color.Green
        3 -> Color.Blue
        4 -> Color.Yellow
        5 -> Color.Red
        else -> { Color.Transparent }
    }
    return color
}