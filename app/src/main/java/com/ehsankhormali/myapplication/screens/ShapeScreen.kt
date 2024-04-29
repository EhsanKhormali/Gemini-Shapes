package com.ehsankhormali.myapplication.screens

import android.graphics.Point
import android.media.MediaRecorder
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ehsankhormali.myapplication.R

@Composable
fun ShapeScreen(shapeViewModel: ShapeViewModel= viewModel()){
    var poits:List<Point> = emptyList()
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by shapeViewModel.uiState.collectAsState()
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier= Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background.copy(red = 0.5f))
        ){
            drawCircle(radius = 200f, color = Color.Blue)
        }
        OutlinedTextField(onValueChange = {prompt=it}, value =prompt,
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth()
        )
        OutlinedIconButton(onClick = {
            val stringBuilder=StringBuilder()
                .append("give me the name of the shape and all points needed to draw that shape in the 200 by 200 axis for this given prompt the point need to be in the form of (x,y) and random points in that axis ")
                .append("give me the answer like this pattern\n shape name: shape name\n points:points to draw the shape\n")
                .append("for example: shape name: triangle points: (0,0),(100,100),(50,100)\n")
                .append("shape name: circle points: (50,50) radios: 70 \n")
                .append("shape name: rectangle points: (0,0),(100,0) (100,100) (0,100)")
                .append("prompt: ")
                .append(prompt)

            shapeViewModel.sendPrompt(stringBuilder.toString())
        }, shape = CircleShape, modifier = Modifier.size(40.dp)) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription ="")
        }

        if (uiState is ShapeUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is ShapeUiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as ShapeUiState.Error).errorMessage
            } else if (uiState is ShapeUiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as ShapeUiState.Success).outputText
            }
            val scrollState = rememberScrollState()
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}
