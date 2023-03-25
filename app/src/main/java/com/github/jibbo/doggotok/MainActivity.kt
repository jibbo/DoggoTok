package com.github.jibbo.doggotok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.jibbo.doggotok.ui.theme.MyApplicationTheme
import com.github.jibbo.doggotok.viewmodels.DogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dogViewModel: DogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MyApplicationTheme {
                        DogList(dogViewModel.pictureFlow, { dogViewModel.randomDog() }) {
                            DogImage(it)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun DogList(
    images: SnapshotStateList<Result<String>> = mutableStateListOf(),
    onScroll: () -> Unit = {},
    content: @Composable (param: Result<String>) -> Unit = {}
) {
    VerticalPager(
        pageCount = images.size,
        modifier = Modifier
            .background(Color.Black)
            .nestedScroll(remember {
                object : NestedScrollConnection {
                    override suspend fun onPostFling(
                        consumed: Velocity,
                        available: Velocity
                    ): Velocity {
                        onScroll()
                        return super.onPostFling(consumed, available)
                    }
                }
            })
    ) { index ->
        content(images[index])
    }
}

@Composable
fun DogImage(imageUrl: Result<String>) {
    if (imageUrl.isFailure) {
        Text(
            text = "Error While loading the image.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        return
    }
    val imageRequest = ImageRequest
        .Builder(LocalContext.current)
        .data(imageUrl.getOrNull())
        .error(R.drawable.baseline_image_24)
        .crossfade(true)
        .build()
    SubcomposeAsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        model = imageRequest,
        contentDescription = "a random dog",
        loading = { CircularProgressIndicator() }
    )
}
