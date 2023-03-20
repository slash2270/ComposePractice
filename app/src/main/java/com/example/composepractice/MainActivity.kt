package com.example.composepractice

import android.os.Bundle
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composepractice.ui.theme.ComposePracticeTheme
import com.example.composepractice.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePracticeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MessageCard(Message("Android", "Jetpack Compose"))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!",
         color = Color.Green,
         style = MaterialTheme.typography.h6
    )
}

@Composable
fun MessageCard(msg: Message) {
    Column {
        ComposePracticeTheme(darkTheme = false) {
            Greeting("Preview Composable")
        }
        Row(modifier = Modifier.padding(all = 4.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(text = msg.author,
                     color = MaterialTheme.colors.secondaryVariant,
                     style = MaterialTheme.typography.subtitle2
                )
                Text(text = msg.body,
                     color = MaterialTheme.colors.secondaryVariant,
                     style = MaterialTheme.typography.body2
                )
            }
        }
        ComposeTutorialTheme(darkTheme = true) {
            // Add padding around our message
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier
                        // Set image size to 40 dp
                        .size(40.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                )
                // Add a horizontal space between the image and the column
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = msg.author,
                         color = MaterialTheme.colors.primaryVariant
                    )
                    // Add a vertical space between the author and message texts
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = msg.body,
                         color = MaterialTheme.colors.primaryVariant
                    )
                }
            }
        }
        ComposeTutorialTheme {
            Conversation(SampleData.conversationSample)
        }
        val listText by remember {
            mutableStateOf(
                listOf(
                    "张三", "李四", "王五", "陳六","黄七"
                )
            )
        }
        ListText(list = listText)
        val listVersion by remember {
            mutableStateOf(
                listOf(
                    "Arctic Fox", "Bumblebee", "Chipmunk", "Dolphin"
                )
            )
        }
        ListName(header = "Android Studio Version", names = listVersion)
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            ItemView(message)
        }
    }
}

@Composable
fun ItemView(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))

        // 我們跟踪消息是否在此展開
        // 多變的
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor 會逐漸從一種顏色更新到另一種顏色
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )
        val extraPadding by animateDpAsState(
            if (isExpanded) 48.dp else 0.dp
        )

        // 當我們點擊這個列時，我們切換 isExpanded 變量
        Column(modifier = Modifier
            .clickable { isExpanded = !isExpanded }
            .weight(1f)
            .padding(bottom = extraPadding)) {
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor 顏色將從初級到表面逐漸變化
                color = surfaceColor,
                // animateContentSize 會逐漸改變 Surface 的大小
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // 如果消息被展開，我們顯示它的所有內容
                    // 否則我們只顯示第一行
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun ListText(list: List<String>) {
    Row(horizontalArrangement = Arrangement.SpaceAround) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Item: ",
            color = Color.Gray
        )
        for (item in list) {
            Text(
                text = "$item, ",
                color = MaterialTheme.colors.primarySurface,
                style = MaterialTheme.typography.subtitle2
            )
        }
        Text(
            text = "Count: ",
            color = Color.Gray,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            text = "${list.size}",
            color = MaterialTheme.colors.primarySurface,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

/**
 * 顯示用戶可以單擊的單個名稱
 */
@Composable
fun ListName(
    header: String,
    names: List<String>,
    // onNameClicked: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )
    val extraPadding by animateDpAsState(
        if (isExpanded) 12.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Column {
        // 這將在 [header] 更改時重新組合，但不會在 [names] 更改時重新組合
        Text(
            header,
            color = Color.Magenta,
            style = MaterialTheme.typography.subtitle1
        )
        Divider()
        // LazyColumn 是 RecyclerView 的 Compose 版本。
        // 傳遞給 items() 的 lambda 類似於 RecyclerView.ViewHolder。
        LazyRow {
            items(names) { name ->
                // 當一個項目的 [name] 更新時，該項目的適配器
                // 將重組。 當 [header] 更改時，這不會重新組合
                // NamePickerItem(name, onNameClicked)
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 1.dp,
                    // surfaceColor 顏色將從初級到表面逐漸變化
                    color = surfaceColor,
                    // animateContentSize 會逐漸改變 Surface 的大小
                    modifier = Modifier
                        .animateContentSize()
                        .padding(1.dp)
                ) {
                    Row(modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .padding(horizontal = extraPadding.coerceAtLeast(0.dp))) {
                        Text(
                            text = name,
                            modifier = Modifier.padding(all = 4.dp),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            style = MaterialTheme.typography.body2
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

/**
 * 顯示用戶可以單擊的單個名稱
 */
@Composable
private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
    Text(name, Modifier.clickable(onClick = { onClicked(name) }))
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    MessageCard(
        msg = Message("Colleague", "Hey, take a look at Jetpack Compose, it's great!")
    )
}

data class Message(val author: String, val body: String)

object SampleData {
    val conversationSample = listOf(
        Message(
            "Colleague",
            "Test...Test...Test..."
        ),
        Message(
            "Colleague",
            "List of Android versions:\n" +
                    "Android KitKat (API 19)\n" +
                    "Android Lollipop (API 21)\n" +
                    "Android Marshmallow (API 23)\n" +
                    "Android Nougat (API 24)\n" +
                    "Android Oreo (API 26)\n" +
                    "Android Pie (API 28)\n" +
                    "Android 10 (API 29)\n" +
                    "Android 11 (API 30)\n" +
                    "Android 12 (API 31)\n"
        ),
        Message(
            "Colleague",
            "I think Kotlin is my favorite programming language.\n" +
                    "It's so much fun!"
        ),
        Message(
            "Colleague",
            "Searching for alternatives to XML layouts..."
        ),
//        Message(
//            "Colleague",
//            "Hey, take a look at Jetpack Compose, it's great!\n" +
//                    "It's the Android's modern toolkit for building native UI." +
//                    "It simplifies and accelerates UI development on Android." +
//                    "Less code, powerful tools, and intuitive Kotlin APIs :)"
//        ),
//        Message(
//            "Colleague",
//            "It's available from API 21+ :)"
//        ),
//        Message(
//            "Colleague",
//            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
//        ),
//        Message(
//            "Colleague",
//            "Android Studio next version's name is Arctic Fox"
//        ),
//        Message(
//            "Colleague",
//            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
//        ),
//        Message(
//            "Colleague",
//            "I didn't know you can now run the emulator directly from Android Studio"
//        ),
//        Message(
//            "Colleague",
//            "Compose Previews are great to check quickly how a composable layout looks like"
//        ),
//        Message(
//            "Colleague",
//            "Previews are also interactive after enabling the experimental setting"
//        ),
//        Message(
//            "Colleague",
//            "Have you tried writing build.gradle with KTS?"
//        ),
    )
}