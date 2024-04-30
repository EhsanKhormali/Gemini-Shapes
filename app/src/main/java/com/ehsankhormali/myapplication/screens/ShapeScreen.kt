package com.ehsankhormali.myapplication.screens

import android.graphics.Paint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ehsankhormali.myapplication.R

@Composable
fun ShapeScreen(shapeViewModel: ShapeViewModel = viewModel()) {
    var pointLocations by remember { mutableStateOf<List<Pair<Float, Float>>>(emptyList()) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by shapeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val assetManager = LocalContext.current.assets
        val paint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 200f
            color = Color.White.toArgb()
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background.copy(red = 0.8f))
        ) {

            if (uiState.requestState is ShapeScreenRequestState.Loading) {
                drawContext.canvas.nativeCanvas.drawText(
                    "Loading...",
                    center.x + 25,
                    center.y + 90,
                    paint
                )
            } else if (uiState.requestState is ShapeScreenRequestState.Success && uiState.shape=="triangle" && uiState.points.size==3) {
                val coefficient = drawContext.size.height/200f
                val points=uiState.points.map {
                    Offset(center.x+it.x*coefficient-100*coefficient,it.y*coefficient)
                }

                drawLine(color = Color.Red,start = points[0], end = points[1])
                drawLine(color = Color.Red,start = points[1], end = points[2])
                drawLine(color = Color.Red,start = points[2], end = points[0])
            //drawCircle(radius = 200f, color = Color.Blue)
            } else if (uiState.requestState is ShapeScreenRequestState.Success && uiState.shape=="rectangle" && uiState.points.size==4 ){
                val coefficient = drawContext.size.height/200f
                val points=uiState.points.map {
                    Offset(center.x+it.x*coefficient-100*coefficient,it.y*coefficient)
                }
                drawLine(color = Color.Red,start = points[0], end = points[1])
                drawLine(color = Color.Red,start = points[1], end = points[2])
                drawLine(color = Color.Red,start = points[2], end = points[3])
                drawLine(color = Color.Red,start = points[3], end = points[0])

            }

        }
        OutlinedTextField(
            onValueChange = { shapeViewModel.updateUserPrompt(it) }, value = shapeViewModel.userPrompt,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = stringResource(id = R.string.prompt_placeholder))}
        )
        OutlinedIconButton(onClick = {
            val stringBuilder = StringBuilder()
                .append("give me the name of the shape and all points needed to draw that shape in the 200 by 200 axis for this given prompt the point need to be random and in the form of (x,y)\n")
                .append("give me the answer like this pattern\n shape name: shape name\n points:points to draw the shape\n")
                .append("for example: shape name: triangle points: (0,0),(100,100),(50,100)\n")
                .append("shape name: circle points: (50,50) radios: 70 \n")
                .append("shape name: rectangle points: (0,0),(100,0) (100,100) (0,100)")
                .append("prompt: ")
                .append(shapeViewModel.userPrompt)

            shapeViewModel.sendPrompt(stringBuilder.toString())
        }, shape = CircleShape, modifier = Modifier.size(40.dp)) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
        }
        var textColor = MaterialTheme.colorScheme.onSurface
        if (uiState.requestState is ShapeScreenRequestState.Error) {
            textColor = MaterialTheme.colorScheme.error
            result = (uiState.requestState as ShapeScreenRequestState.Error).errorMessage
        } else if (uiState.requestState is ShapeScreenRequestState.Success) {
            textColor = MaterialTheme.colorScheme.onSurface
            result = (uiState.requestState as ShapeScreenRequestState.Success).outputText
        }
        val scrollState = rememberScrollState()
        Text(
            text = result,
            textAlign = TextAlign.Start,
            color = textColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                //.fillMaxSize()
                .verticalScroll(scrollState)
        )
        //ExtractPointLocations(text = result)
    }
}

/*@Composable
*/

