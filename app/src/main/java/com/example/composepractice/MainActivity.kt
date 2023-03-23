package com.example.composepractice

import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
//import androidx.window.core.layout.WindowSizeClass
import com.example.composepractice.ui.theme.ComposePracticeTheme
import com.example.composepractice.ui.theme.ComposeTutorialTheme
import com.example.composepractice.ui.theme.Shapes
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize

interface SampleInterface { fun log(message: String) }

class MainActivity : ComponentActivity(), SampleInterface {

    private val LocalInterface = staticCompositionLocalOf<SampleInterface> { error("Not provided") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableInterface(sampleInterface = this)
            ComposePracticeTheme {
                // 使用主題中“背景”顏色的表面容器
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MessageCard(Message("Android", "Jetpack Compose"))
                }
            }
        }
    }

    override fun log(message: String) {
        Log.v("Activity", message)
    }

    @Composable
    fun ComposableInterface(sampleInterface: SampleInterface) {
        CompositionLocalProvider(
            LocalInterface provides sampleInterface
        ) {
            LocalInterface.current.log("ComposableInterface") // 在任何需要的級別調用
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
        val shapes = MaterialTheme.shapes
        val colors = MaterialTheme.colors
        val typography = MaterialTheme.typography
        val rememberCoroutineScope = rememberCoroutineScope()
        Column {
            ComposePracticeTheme(darkTheme = false) {
                Greeting("Preview Composable")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Row(modifier = Modifier.padding(all = 4.dp)) {
                val modifier1 = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, colors.secondary, CircleShape)
                val modifier2 = Modifier.size(20.dp)
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Contact profile picture",
                    modifier = modifier1.then(modifier2)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = msg.author,
                        color = colors.secondaryVariant,
                        style = typography.subtitle2
                    )
                    Text(text = msg.body,
                        color = colors.secondaryVariant,
                        style = typography.body2
                    )
                }
            }
            ComposeTutorialTheme(darkTheme = true) {
                // 在我們的消息周圍添加填充
                Row(modifier = Modifier.padding(all = 8.dp)) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "Contact profile picture",
                        modifier = Modifier
                            // 將圖像大小設置為 40 dp
                            .size(40.dp)
                            // 將圖像裁剪成圓形
                            .clip(CircleShape)
                    )
                    // 在圖像和列之間添加一個水平空間墊片
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = msg.author,
                            color = colors.primaryVariant
                        )
                        // 在作者和消息文本之間添加一個垂直空間
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = msg.body,
                            color = colors.primaryVariant
                        )
                    }
                }
            }
            ComposeTutorialTheme {
                Conversation(SampleData.conversationSample, colors = colors, shapes = shapes, typography = typography, rememberCoroutineScope  = rememberCoroutineScope)
            }
//      Cupcake（紙杯蛋糕）、Donut（甜甜圈）、Eclair（閃電泡芙）、Froyo（優格冰淇淋）、Gingerbread（薑餅）、Honeycomb（蜂巢）[來源請求]、Ice Cream Sandwich（冰淇淋三明治）、Jelly Bean（雷根糖）、KitKat（奇巧巧克力）、Lollipop（棒棒糖）、Marshmallow（棉花糖）、Nougat（牛軋糖）、Oreo（奧利奧）、Pie（派）
            val listText by remember {
                mutableStateOf(
                    listOf(
                        "Cupcake", "Donut", "Eclair", "Froyo"
                    )
                )
            }
            ListText(list = listText, colors = colors, typography = typography)
            val listVersion by remember {
                mutableStateOf(
                    listOf(
                        "Arctic Fox", "Bumblebee", "Chipmunk", "Dolphin"
                    )
                )
            }
            ListName(header = "Android Studio Version", names = listVersion, colors = colors, shapes = shapes, typography = typography)
            EditTextScreen(shapes, typography)
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                ParcelizeScreen(typography = typography)
                MapSaverScreen(typography = typography)
                ListSaverScreen(typography = typography)
                Box(
                    modifier = Modifier
                        .background(Color.Black, Shapes.small)
                        .padding(8.dp, 0.dp)
                ) {
                    Spacer(
                        Modifier
                            .matchParentSize()
                            .background(Color.LightGray))
                    Text(text = "modifier order",
                        color = colors.error,
                        style = typography.body2
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .background(Color.Black, Shapes.small)
                ) {
                    Spacer(
                        Modifier
                            .matchParentSize()
                            .background(Color.Gray))
                    Text(text = "modifier order",
                        color = colors.error,
                        style = typography.body2
                    )
                }
            }
            SampleStateButton()
            RememberCoroutineScope(rememberCoroutineScope = rememberCoroutineScope, colors = colors)
            LaunchedEffect(null, Dispatchers.IO) {
                Log.v("LaunchedEffect", "這個block執行在協程${Thread.currentThread().name}中")
            }
            DisposableEffect(
                onStart = {
                Log.v("DisposableEffect", "這個block執行在協程${Thread.currentThread().name}中")
            },
                onStop = {
                Log.v("DisposableEffect", "這個block執行在協程${Thread.currentThread().name}中")
            })
//        ShapeBrushStyle(avatarRes = R.mipmap.ic_launcher)
        }
    }



    @Composable
    fun Conversation(messages: List<Message>, colors: Colors, shapes: Shapes, typography: Typography, rememberCoroutineScope: CoroutineScope) {
        LazyColumn {
            items(
                items = messages,
//            key = { message -> message.author },
                itemContent = { message: Message ->
                    if (message.author.isNotEmpty()) {
                        ItemView(message, colors, shapes, typography, rememberCoroutineScope)
                    } else {
                        Text(text = "EmptyView")
                    }
                })
        }
    }

    @Composable
    fun ItemView(msg: Message, colors: Colors, shapes: Shapes, typography: Typography, rememberCoroutineScope: CoroutineScope) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, colors.secondaryVariant, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))

            // 我們跟踪消息是否在此展開 多變的
            var isExpanded by remember { mutableStateOf(false) }
            // surfaceColor 會逐漸從一種顏色更新到另一種顏色
            val surfaceColor by animateColorAsState(
                if (isExpanded) colors.secondary else colors.background,
            )
            // 當我們點擊這個列時，我們切換 isExpanded 變量
            Column(modifier = Modifier
                .clickable {
                    rememberCoroutineScope.launch(Dispatchers.IO) {
                        isExpanded = !isExpanded
                    }
                }
                .fillMaxWidth()) {
                Text(
                    text = msg.author,
                    color = colors.secondaryVariant,
                    style = typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = shapes.medium,
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
                        style = typography.body2
                    )
                }
            }
        }
    }

    @Composable
    fun ListText(list: List<String>, colors: Colors, typography: Typography) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Spacer(modifier = Modifier.weight(0.3f))
            Row(modifier = Modifier.weight(6.7f)) {
                Text(
                    text = "Item: ",
                    color = Color.Gray,
                )
                for (item in list) {
                    Text(
                        text = "$item, ",
                        color = colors.primarySurface,
                        style = typography.subtitle2,
                    )
                }
            }
            Row(modifier = Modifier.weight(3.0f)) {
                Text(
                    text = "Count: ",
                    color = Color.Gray,
                    style = typography.subtitle2,
                    modifier = Modifier.run { offset(x = (10).dp) }
                )
                Text(
                    text = "${list.size}",
                    color = colors.primarySurface,
                    style = typography.subtitle2,
                    modifier = Modifier.run { offset(x = (10).dp) }
                )
            }
        }
    }

    /**
     * 顯示用戶可以單擊的單個名稱
     */
    @Composable
    fun ListName(
        header: String,
        names: List<String>,
        colors: Colors,
        shapes: Shapes,
        typography: Typography
        // onNameClicked: (String) -> Unit
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) colors.secondary else colors.background,
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
                style = typography.subtitle1
            )
            Divider()
            // LazyColumn 是 RecyclerView 的 Compose 版本。
            // 傳遞給 items() 的 lambda 類似於 RecyclerView.ViewHolder。
            LazyRow(modifier = Modifier.paddingFromBaseline(top = 25.dp)) {
                items(names) { name ->
                    // 當一個項目的 [name] 更新時，該項目的適配器
                    // 將重組。 當 [header] 更改時，這不會重新組合
                    // NamePickerItem(name, onNameClicked)
                    Surface(
                        shape = shapes.medium,
                        elevation = 1.dp,
                        // surfaceColor 顏色將從初級到表面逐漸變化
                        color = surfaceColor,
                        // animateContentSize 會逐漸改變 Surface 的大小
                        modifier = Modifier
                            .animateContentSize()
                            .padding(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { isExpanded = !isExpanded }
                                .padding(horizontal = extraPadding.coerceAtLeast(0.dp))) {
                            Text(
                                text = name,
                                modifier = Modifier.padding(all = 4.dp),
                                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                style = typography.body2
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
//    @Composable
//    private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
//        Text(name, Modifier.clickable(onClick = { onClicked(name) }))
//    }

    @Composable
    fun EditTextScreen(shapes: Shapes, typography: Typography) {
        var name by rememberSaveable { mutableStateOf("") }
        EditTextContent(name = name, onNameChange = { name = it }, shapes, typography)
    }

    @Composable
    fun EditTextContent(name: String, onNameChange: (String) -> Unit, shapes: Shapes, typography: Typography) {
        Column(modifier = Modifier.padding(8.dp)) {
//        val name by remember { mutableStateOf("") }
            if (name.isNotEmpty()) {
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = typography.subtitle1
                )
            }
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name") },
                shape = shapes.small,
                colors = TextFieldDefaults.outlinedTextFieldColors(),
            )
        }
    }

    @Parcelize
    data class ParcelizeBean(val key: String, val value: String) : Parcelable

    @Composable
    fun ParcelizeScreen(typography: Typography) {
        val selected = rememberSaveable { mutableStateOf(ParcelizeBean("TW", "Parcelize")) }
        RestoreStateText(text = selected.value.value, typography = typography)
    }

    data class MapSaverBean(val key: String, val value: String)

    private val mapSaver = run {
        val modelKey = "TW"
        val countryKey = "MapSaver"
        mapSaver(
            save = { mapOf(modelKey to it.key, countryKey to it.value) },
            restore = { MapSaverBean(it[modelKey] as String, it[countryKey] as String) }
        )
    }

    @Composable
    fun MapSaverScreen(typography: Typography) {
        val selected = rememberSaveable(stateSaver = mapSaver) { mutableStateOf(MapSaverBean("TW", "MapSaver")) }
        RestoreStateText(text = selected.value.value, typography = typography)
    }

    data class ListSaverBean(val key: String, val value: String)

    private val listSaver = listSaver<ListSaverBean, Any>(
        save = { listOf(it.key, it.value) },
        restore = { ListSaverBean(it[0] as String, it[1] as String) }
    )

    @Composable
    fun ListSaverScreen(typography: Typography) {
        val selected = rememberSaveable(stateSaver = listSaver) { mutableStateOf(ListSaverBean("TW", "ListSaver")) }
        RestoreStateText(text = selected.value.value, typography = typography)
    }

    @Composable
    fun RestoreStateText(text: String, typography: Typography) {
        Text(
            text = text,
            color = Color.Red,
            style = typography.body2,
            maxLines = 1
        )
    }
    /**
     * bug?
     * */
//@OptIn(ExperimentalTextApi::class)
//@Composable
//fun ShapeBrushStyle(
//    @DrawableRes avatarRes: Int,
//    modifier: Modifier = Modifier,
//    res: Resources = LocalContext.current.resources
//) {
////    val bitmap = BitmapFactory.decodeResource(res, avatarRes)
//    val bitmap = (ResourcesCompat.getDrawable(res, R.drawable.ic_launcher_foreground, null) as BitmapDrawable).bitmap
//    val brush = remember(key1 = R.drawable.bitmap_launcher) {
//        ShaderBrush(
//            BitmapShader(
//                bitmap,
////                ImageBitmap.imageResource(res, R.drawable.bitmap_launcher).asAndroidBitmap(),
//                android.graphics.Shader.TileMode.REPEAT,
//                android.graphics.Shader.TileMode.REPEAT
//            )
//        )
//    }
//    Box(
//        modifier = modifier
//            .background(brush = brush)
//            .size(24.dp, 24.dp)
//    ) {
////        Text(
////            text = "ShaderBrush",
////            color = Color.Red,
////            style = TextStyle(fontSize = 12.sp),
////            maxLines = 1,
////        )
//    }
//}

    /**
     * 旋轉裝置
     * */
//@Composable
//fun rememberMyAppState(
//    windowSizeClass: WindowSizeClass
//): MyAppState {
//    return remember(windowSizeClass) {
//        MyAppState(windowSizeClass)
//    }
//}
//
//@Stable
//class MyAppState(
//    private val windowSizeClass: WindowSizeClass
//) {
//
//}

    /**
     * 將類型標記為穩定以支持跳過和智能重組。
     */
//@Stable
//interface UiState<T : Result<T>> {
//    val value: T?
//    val exception: Throwable?
//    val hasError: Boolean
//        get() = exception != null
//}

    @Composable
    fun SampleStateButton() {
        var count1 = 1
        var count2 by remember { mutableStateOf(1) }
        val interactionSource = remember { MutableInteractionSource() }
//        val isPressed by interactionSource.collectIsPressedAsState()
        var resp by remember {
            mutableStateOf("")
        }
        LaunchedEffect(count2, Dispatchers.IO) {
            delay(400)
            resp ="LaunchedEffect"
//        "Thread = ${Thread.currentThread().name}"
        }
        Row {
            val verticalArrangement = Arrangement.Center
            val horizontalAlignment = Alignment.CenterHorizontally
            val modifier = Modifier.weight(1.0f)
            val white = Color.White
            val style = TextStyle(fontSize = 11.sp)
            Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, modifier = modifier) {
                Text(text = resp, style = style)
                Button(
                    onClick = { count2++ }) {
                    Text("$count2", color = white, style = style)
                }
            }
            Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, modifier = modifier) {
                Text(text = "Normal", style = style)
                Button(
                    onClick = { count1++ }) {
                    Text("$count1", color = white, style = style)
                }
            }
            Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, modifier = modifier) {
                Text(text = "InteractionSource", style = style)
                Button(
                    onClick = { count2-- },
                    interactionSource = interactionSource) {
                    Text("$count2", color = white, style = style)
                }
            }
        }
    }

    @Composable
    fun RememberCoroutineScope(rememberCoroutineScope: CoroutineScope, colors: Colors) {
        // 創建綁定到 RememberCoroutineScope 生命週期的 CoroutineScope
        // `LaunchedEffect` 將取消並重新啟動
        // `scaffoldState.snackBarHostState` 變化
        val scaffoldState = rememberScaffoldState()
        val verticalArrangement = Arrangement.Center
        val horizontalAlignment = Alignment.CenterHorizontally
//        val modifier = Modifier.weight(1.0f)
        val white = Color.White
        val style = TextStyle(fontSize = 11.sp)
        Scaffold(scaffoldState = scaffoldState) {
            Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment, modifier = Modifier.padding(it)) {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = horizontalAlignment) {
                        Text(text = "rememberCoroutineScope", style = style)
                        Button(
                            onClick = {
                                // 在事件處理程序中創建一個新協程以顯示一個小吃店
                                rememberCoroutineScope.launch(Dispatchers.IO) {
                                    scaffoldState.snackbarHostState.showSnackbar("Something happened!", duration = SnackbarDuration.Short)
                                }
                            }
                        ) {
                            Text("SnackBar", color = white, style = style)
                        }
                    }
                    RememberUpdatedState(colors, style)
                    SideEffect(style, horizontalAlignment)
                    ProduceState(colors, style)
                }
            }
            LaunchedEffect(scaffoldState.snackbarHostState, Dispatchers.IO) {
                // 使用協程顯示 snackBar，當協程被取消時
                // snackBar 會自動關閉。 這個協程將在任何時候取消
                // `state.hasError` 為假，只有當 `state.hasError` 為真時才開始
                //（由於上面的 if-check），或者如果 `scaffoldState.snackBarHostState` 發生變化。
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Error message",
                    actionLabel = "Retry message",
                    duration =  SnackbarDuration.Short
                )
            }
        }
    }

    @Composable
    fun RememberUpdatedState(colors: Colors, style: TextStyle) {
        // 這將始終引用最新的 onTimeout 函數
        // LandingScreen 被重組為
        var timeOut by remember { mutableStateOf("wait 5 seconds...") }
        val currentOnTimeout by rememberUpdatedState("remember\nUpdatedState")
        // 創建一個匹配 LandingScreen 生命週期的效果。
        // 如果 LandingScreen 重組，延遲不應該重新開始。
        LaunchedEffect(true, Dispatchers.IO) {
            delay(5000)
            if (timeOut != currentOnTimeout) timeOut = currentOnTimeout
            Log.v("rememberUpdatedState", currentOnTimeout)
        }
        /* 登陸屏幕內容 */
        Text(
            text = timeOut,
            color = colors.secondaryVariant,
            style = style,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun DisposableEffect(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        onStart: () -> Unit, // 發送 'started' 分析事件
        onStop: () -> Unit // 發送“已停止”分析事件
    ) {
        // 在提供新的 lambda 時安全地更新當前的 lambda
        val currentOnStart by rememberUpdatedState(onStart)
        val currentOnStop by rememberUpdatedState(onStop)
        // 如果 `lifecycleOwner` 發生變化，處理並重置效果
        DisposableEffect(lifecycleOwner) {
            // 創建一個觸發我們記住的回調的觀察者
            // 用於發送分析事件
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    currentOnStart()
                } else if (event == Lifecycle.Event.ON_STOP) {
                    currentOnStop()
                }
            }
            // 將觀察者添加到生命週期
            lifecycleOwner.lifecycle.addObserver(observer)
            // 當效果離開 Composition 時，移除觀察者
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    @Composable
    fun SideEffect(style: TextStyle, horizontal: Alignment.Horizontal) {
        // 在每個成功的組合上，更新 FirebaseAnalytics
        // 來自當前用戶的用戶類型，確保未來的分析
        // 事件附加了這個元數據
        val color = Color.Black
        var count = 0
        Column(horizontalAlignment = horizontal) {
            SideEffect {
                Log.v("SideEffect", "這個block執行在協程${Thread.currentThread().name}中")
            }
            val names= arrayOf("List","Size")
            for (name in names){
                Text(text = name, color = color, style = style, textAlign = TextAlign.Center)
                count++
            }
            Text(text = "SideEffect\ncount:$count", color = color, style = style, textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun ProduceState(colors: Colors, style: TextStyle) {
        var timerStartStop by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val timer by produceState(initialValue = 0, timerStartStop) {
            val x = (1..10).random()
            var job: Job? = null
            Toast.makeText(context, "Start $x", LENGTH_SHORT).show()
            if (timerStartStop) {
                // 使用 MainScope 確保觸發 awaitDispose
                job = MainScope().launch {
                    while (true) {
                        delay(1000)
                        value++
                    }
                }
            }
            awaitDispose {
                Toast.makeText(context, "Done $x", LENGTH_SHORT).show()
                job?.cancel()
            }
        }
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Time $timer", style = style)
                Button(onClick = {
                    timerStartStop = !timerStartStop
                }) {
                    Text(text = if (timerStartStop) "Stop" else "Start", color = colors.background, style = style)
                }
            }
        }
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

/*
* Copyright (c) 2022, smuyyh@gmail.com All Rights Reserved.
* #                                                   #
* #                       _oo0oo_                     #
* #                      o8888888o                    #
* #                      88" . "88                    #
* #                      (| -_- |)                    #
* #                      0\  =  /0                    #
* #                    ___/`---'\___                  #
* #                  .' \\|     |# '.                 #
* #                 / \\|||  :  |||# \                #
* #                / _||||| -:- |||||- \              #
* #               |   | \\\  -  #/ |   |              #
* #               | \_|  ''\---/''  |_/ |             #
* #               \  .-\__  '-'  ___/-. /             #
* #             ___'. .'  /--.--\  `. .'___           #
* #          ."" '<  `.___\_<|>_/___.' >' "".         #
* #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
* #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
* #     =====`-.____`.___ \_____/___.-`___.-'=====    #
* #                       `=---='                     #
* #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
* #                                                   #
* #               佛祖保佑         永无BUG            #
* #                                                   #
*/