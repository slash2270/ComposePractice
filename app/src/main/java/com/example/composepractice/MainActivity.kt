package com.example.composepractice

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
//import androidx.window.core.layout.WindowSizeClass
import com.example.composepractice.ui.theme.ComposePracticeTheme
import com.example.composepractice.ui.theme.ComposeTutorialTheme
import com.example.composepractice.ui.theme.Shapes
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize
import kotlin.ranges.coerceAtLeast

interface SampleInterface { fun log(message: String) }

class MainActivity : ComponentActivity(), SampleInterface {

    private val localInterface = staticCompositionLocalOf<SampleInterface> { error("Not provided") }

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
            localInterface provides sampleInterface
        ) {
            localInterface.current.log("ComposableInterface") // 在任何需要的級別調用
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
        val style = TextStyle(fontSize = 11.sp)
        val rememberCoroutineScope = rememberCoroutineScope()
//        val toolbarHeight = 200.dp // 定义 ToolBar 的高度
//        val maxUpPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() - 56.dp.roundToPx().toFloat() } // ToolBar 最大向上位移量 56.dp 参考自 androidx.compose.material AppBar.kt 里面定义的 private val AppBarHeight = 56.dp
//        val minUpPx = 0f // ToolBar 最小向上位移量
//        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) } // 偏移折叠工具栏上移高度
        Scaffold(
                topBar = { TopAppBar(title = { Text("Compose Practice", color = Color.White) }, backgroundColor = Color(0xff0f9d58)) },
                content = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(it)) { // 作为父级附加到嵌套滚动系统)
                    repeat(1) {
                        Column {
                            ComposePracticeTheme(darkTheme = false) {
                                Greeting("Preview Composable")
                            }
                            Row(horizontalArrangement = Arrangement.SpaceAround) {
                                Row(modifier = Modifier.padding(all = 4.dp)) {
                                    val modifier1 = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, colors.secondary, CircleShape)
                                    val modifier2 = Modifier.size(20.dp)
                                    Image(
                                        painter = painterResource(R.drawable.ic_launcher_foreground),
                                        contentDescription = getString(R.string.contentDescription),
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
                                            contentDescription = getString(R.string.contentDescription),
                                            modifier = Modifier.size(40.dp) // 將圖像大小設置為 40 dp.clip(CircleShape) // 將圖像裁剪成圓形
                                        )
                                        // 在圖像和列之間添加一個水平空間墊片
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(text = msg.author,
                                                color = colors.primaryVariant,
                                                style = typography.subtitle2
                                            )
                                            // 在作者和消息文本之間添加一個垂直空間
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(text = msg.body,
                                                color = colors.primaryVariant,
                                                style = typography.body2
                                            )
                                        }
                                    }
                                }
                            }
                            ComposeTutorialTheme {
                                Conversation(SampleData.conversationSample, colors = colors, shapes = shapes, typography = typography, rememberCoroutineScope  = rememberCoroutineScope)
                            }
                           val listText by remember {
                                mutableStateOf(
                                    listOf(
                                        "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread" , "Honeycomb" , "Ice Cream Sandwich", "Jelly Bean", "KitKat" , "Lollipop" , "Marshmallow" , "Nougat" , "Oreo" , "Pie"
                                    )
                                )
                            }
                            ListText(list = listText, colors = colors, style = style)
                            val listVersion by remember {
                                mutableStateOf(
                                    listOf(
                                        "Arctic Fox", "Bumblebee", "Chipmunk", "Dolphin", "Electric", "Flamingo"
                                    )
                                )
                            }
                            ListName(header = "Android Studio Version:", names = listVersion, colors = colors, style = style, shapes = shapes)
                            EditTextScreen(shapes, typography)
                            val eightDp = Modifier.width(8.dp)
                            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                                ParcelizeScreen(style = style)
                                Spacer(modifier = eightDp)
                                SaverScreen(style = style)
                                Spacer(modifier = eightDp)
                                MapSaverScreen(style = style)
                                Spacer(modifier = eightDp)
                                ListSaverScreen(style = style)
                                Spacer(modifier = eightDp)
                                Box(modifier = Modifier
                                    .background(Color.Black, Shapes.small)
                                    .padding(8.dp, 0.dp)) {
                                    Spacer(
                                        Modifier
                                            .matchParentSize()
                                            .background(Color.Gray))
                                    Text(text = "modifier order",
                                        color = colors.error,
                                        style = style
                                    )
                                }
                                Spacer(modifier = eightDp)
                                Box(modifier = Modifier
                                    .padding(8.dp, 0.dp)
                                    .background(Color.Black, Shapes.small)) {
                                    Spacer(
                                        Modifier
                                            .matchParentSize()
                                            .background(Color.Gray))
                                    Text(text = "modifier order",
                                        color = colors.error,
                                        style = style
                                    )
                                }
                                StatusRead(colors = colors, style = style)
                                DrawLineDemo(colors = colors)
                                DrawOvalDemo(colors = colors)
                                DrawRectDemo(colors = colors)
                            }
                            SampleStateButton(style = style)
                            RememberCoroutineScope(rememberCoroutineScope = rememberCoroutineScope, colors = colors)
                            SnapShotFlow(colors = colors, style = style)
                            LazyListStateDemo(colors = colors, style = style)
                            ReorganizationLoopDemo(colors = colors, style = style)
                            SortList(colors = colors, style = style)
                            LaunchedEffect(null, Dispatchers.IO) {
                                Log.v("LaunchedEffect", "這個block執行在協程${Thread.currentThread().name}中")
                            }
                            DisposableEffect(
                                onStart = {
                                    Log.v("DisposableEffect", "這個block執行在協程${Thread.currentThread().name}中")
                                },
                                onStop = {
                                    Log.v("DisposableEffect", "這個block執行在協程${Thread.currentThread().name}中")
                                }
                            )
//        ShapeBrushStyle(avatarRes = R.mipmap.ic_launcher)
                        }   // 子组件内容
                    }
                }
            }
        )
    }

    @Composable
    fun Conversation(messages: List<Message>, colors: Colors, shapes: Shapes, typography: Typography, rememberCoroutineScope: CoroutineScope) {
        LazyColumn(modifier = Modifier.height(200.dp)) {
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
                contentDescription = getString(R.string.contentDescription),
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
    fun ListText(list: List<String>, colors: Colors, style: TextStyle) {
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Row(modifier = Modifier.weight(8.4f) ) {
                Text(
                    text = "Android SDK: ",
                    color = Color.Gray,
                    style = style,
                    modifier = Modifier.weight(2.2f)
                )
                LazyRow(modifier = Modifier.weight(7.8f)) {
                    items(list) { item ->
                        Text(
                            text = "$item, ",
                            color = colors.primarySurface,
                            style = style,
                        )
                    }
                }
            }
            Row(modifier = Modifier.weight(1.6f)) {
                Text(
                    text = "Count: ",
                    color = Color.Gray,
                    style = style,
                    modifier = Modifier.run { offset(x = (10).dp) }
                )
                Text(
                    text = "${list.size}",
                    color = colors.primarySurface,
                    style = style,
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
        style: TextStyle,
        shapes: Shapes,
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
        Row {
            // 這將在 [header] 更改時重新組合，但不會在 [names] 更改時重新組合
            Text(
                header,
                color = Color.Magenta,
                style = style,
                modifier = Modifier
                    .weight(3.2f, true)
                    .paddingFromBaseline(top = 2.5.dp, bottom = 2.5.dp)
            )
            // LazyColumn 是 RecyclerView 的 Compose 版本。
            // 傳遞給 items() 的 lambda 類似於 RecyclerView.ViewHolder。
            LazyRow(modifier = Modifier
                .paddingFromBaseline(top = 2.5.dp, bottom = 2.5.dp)
                .weight(6.8f, true)) {
                items(names) { name ->
                    // 當一個項目的 [name] 更新時，該項目的適配器
                    // 將重組。 當 [header] 更改時，這不會重新組合
                    // NamePickerItem(name, onNameClicked)
                    Surface(
                        shape = shapes.medium,
                        elevation = 1.dp,
                        color = surfaceColor, // surfaceColor 顏色將從初級到表面逐漸變化
                        modifier = Modifier
                            .animateContentSize()
                            .padding(2.dp) // animateContentSize 會逐漸改變 Surface 的大小
                    ) {
                        Text(
                            text = name,
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            style = style,
                            modifier = Modifier
                                .clickable { isExpanded = !isExpanded }
                                .padding(horizontal = extraPadding.coerceAtLeast(0.dp))
                        )
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
    fun ParcelizeScreen(style: TextStyle) {
        val selected = rememberSaveable { mutableStateOf(ParcelizeBean("TW", "Parcelize")) }
        RestoreStateText(text = selected.value.value, style = style)
    }

    private val saver = Saver<ParcelizeBean, Any>(
        save = { ParcelizeBean(it.key, it.value) },
        restore = { ParcelizeBean(it.toString(), it.toString())  }
    )

    @Composable
    fun SaverScreen(style: TextStyle) {
        val selected = rememberSaveable(stateSaver = saver) { mutableStateOf(ParcelizeBean("TW", "Saver")) }
        RestoreStateText(text = selected.value.value, style = style)
    }

    private val mapSaver = run {
        val modelKey = "TW"
        val countryKey = "MapSaver"
        mapSaver(
            save = { mapOf(modelKey to it.key, countryKey to it.value) },
            restore = { ParcelizeBean(it[modelKey] as String, it[countryKey] as String) }
        )
    }

    @Composable
    fun MapSaverScreen(style: TextStyle) {
        val selected = rememberSaveable(stateSaver = mapSaver) { mutableStateOf(ParcelizeBean("TW", "MapSaver")) }
        RestoreStateText(text = selected.value.value, style = style)
    }

    private val listSaver = listSaver<ParcelizeBean, Any>(
        save = { listOf(it.key, it.value) },
        restore = { ParcelizeBean(it[0] as String, it[1] as String) }
    )

    @Composable
    fun ListSaverScreen(style: TextStyle) {
        val selected = rememberSaveable(stateSaver = listSaver) { mutableStateOf(ParcelizeBean("TW", "ListSaver")) }
        RestoreStateText(text = selected.value.value, style = style)
    }

    @Composable
    fun RestoreStateText(text: String, style: TextStyle) {
        Text(
            text = text,
            color = Color.Red,
            style = style,
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
    fun SampleStateButton(style: TextStyle) {
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
        var count by remember{ mutableStateOf(1) } // age改变时person会自动刷新，引发Recompose
        val derivedState by remember{ derivedStateOf { "derivedStateOf" } }
        LazyRow(horizontalArrangement = Arrangement.SpaceBetween) {
            val verticalArrangement = Arrangement.Center
            val horizontalAlignment = Alignment.CenterHorizontally
//            val modifier = Modifier.weight(1.0f)
            val white = Color.White
            item {
                Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
                    Text(text = resp, style = style)
                    Button(
                        onClick = { count2++ }) {
                        Text("$count2", color = white, style = style)
                    }
                }
            }
            item {
                Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
                    Text(text = "Normal", style = style)
                    Button(
                        onClick = { count1++ }) {
                        Text("$count1", color = white, style = style)
                    }
                }
            }
            item {
                Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
                    Text(text = "InteractionSource", style = style)
                    Button(
                        onClick = { count2-- },
                        interactionSource = interactionSource) {
                        Text("$count2", color = white, style = style)
                    }
                }
            }
            item {
                Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
                    Text(text = "$derivedState++", style = style)
                    Button(
                        onClick = { count += 1 }
                    ) {
                        Text(text = "$count", color = white, style = style)
                    }
                }
            }
            item {
                Column(verticalArrangement = verticalArrangement, horizontalAlignment = horizontalAlignment) {
                    Text(text = "$derivedState--", style = style)
                    Button(
                        onClick = { count -= 1 }
                    ) {
                        Text(text = "$count", color = white, style = style)
                    }
                }
            }
            item {
                ProduceStateButton(color = white, style = style)
            }
        }
    }

    @Composable
    fun RememberCoroutineScope(rememberCoroutineScope: CoroutineScope, colors: Colors) {
        // 創建綁定到 RememberCoroutineScope 生命週期的 CoroutineScope
        // `LaunchedEffect` 將取消並重新啟動 `scaffoldState.snackBarHostState` 變化
        val scaffoldState = rememberScaffoldState()
        val verticalArrangement = Arrangement.Center
        val horizontalAlignment = Alignment.CenterHorizontally
        val white = Color.White
        val style = TextStyle(fontSize = 11.sp)
        Scaffold(scaffoldState = scaffoldState, modifier = Modifier.height(60.dp)) {
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

//    @Composable
//    fun DerivedStateOf(highPriorityKeywords: List<String> = listOf("Android", "Mobile", "Type")) {
//        val todoTasks = remember { mutableStateListOf("Google", "Samsung", "SONY", "Asus", "HTC", "Nokia", "Sharp", "LG", "VIVO", "OPPO", "XiaoMi", "HuaWei", "OnePlus", "RealMe", "IQOO") }
//        // 只有當todoTasks或highPriorityKeywords時才計算高優先級任務
//        // 改變，而不是在每次重組時
//        val highPriorityTasks by remember(highPriorityKeywords) {
//            derivedStateOf { todoTasks.filter { highPriorityKeywords.contains(it) } }
//        }
//        LazyColumn {
//            item {
//                Text(text = "add-TodoTasks", Modifier.clickable {
//                    todoTasks.add("Review")
//                })
//            }
//            item {
//                Divider(color = Color.Red, modifier = Modifier.height(1.dp).fillMaxWidth())
//            }
//            items(highPriorityTasks) { Text(text = it) }
//            item {
//                Divider(color = Color.Red, modifier = Modifier.height(1.dp).fillMaxWidth())
//            }
//            items(todoTasks) {
//                Text(text = it)
//            }
//        }
//        /* UI 的其餘部分，用戶可以在其中向列表添加元素 */
//    }

    @Composable
    fun SnapShotFlow(colors: Colors, style: TextStyle) {
        var count by remember { mutableStateOf(0) }
        val listState = rememberLazyListState()
        val list = mutableListOf("Google", "Samsung", "SONY", "Asus", "HTC", "Nokia", "Sharp", "LG", "VIVO", "OPPO", "XiaoMi", "HuaWei", "OnePlus", "RealMe", "IQOO")
        LazyRow(state = listState) {
            item {
                Text(
                    text = "count: $count, SnapShotFlow: ",
                    color = colors.onSurface,
                    style = style,
                    textAlign = TextAlign.Center
                )
            }
            items(list) {
                Text(
                    text = "$it ",
                    color = colors.secondaryVariant,
                    style = style,
                    textAlign = TextAlign.Center
                )
            }
        }
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .map { index -> index > 0 }
                .distinctUntilChanged()
                .filter { it == true }
                .collect {
                    count++
                }
        }
    }

    @Composable
    fun loadNetWorkImage(url: String, imageRepository: ImageRepository) : State<Result> {
        // produceState 观察 url 和 imageRepository 两个参数，当它们发生变化时，producer会重新执行
        // produceState的实现是通过 remember { mutableStateOf() } + LaunchedEffect （具有学习意义）
        // produceState 中的任务会随着 LaunchedEffect 的 onDispose 被自动停止。
        return produceState<Result>(initialValue = Result.Loading, url, imageRepository) {
            // 通过挂起函数请求图片
            val image = imageRepository.load(url)
            // 根据请求结果设置 Result
            // 当 Result 变化时，读取此 State 的 Composable 会触发重组
            value = if (image == null) {
                Result.Error
            } else {
                Result.Success(image)
            }
        }
    }

    @Composable
    fun ProduceStateButton(color: Color, style: TextStyle) {
        var loadImage by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display a load image button when image is not loading
            Button(onClick = { loadImage = !loadImage }) {
                Text(
                    text = "ProduceState $loadImage",
                    color = color,
                    style = style
                )
            }
            if (loadImage) {
                ProduceStateExample()
            }
        }
    }

    @Composable
    private fun ProduceStateExample() {
        val context = LocalContext.current
        val url = "www.example.com"
        val imageRepository = remember { ImageRepository() }
        val imageState = loadNetWorkImage(url = url, imageRepository)
        when (imageState.value) {
            is Result.Loading -> {
                println("🔥 ProduceStateExample() Result.Loading")
                with(context) {showToast("🔥 ProduceStateExample() Result.Loading")}
                CircularProgressIndicator()
            }
            is Result.Error -> {
                println("❌ ProduceStateExample() Result.Error")
                with(context) {showToast("❌ ProduceStateExample() Result.Error")}
                Image(imageVector = Icons.Default.Close, contentDescription = getString(R.string.contentDescription))
            }
            is Result.Success -> {
                println("✅ ProduceStateExample() Result.Success")
                with(context) {showToast("✅ ProduceStateExample() Result.Success")}
                val image = (imageState.value as Result.Success).image
                Image(
                    painterResource(id = image.imageIdRes),
                    contentDescription = getString(R.string.contentDescription)
                )
            }
        }
    }

    @Composable
    private fun StatusRead(colors: Colors, style: TextStyle) {
        val mutableState: MutableState<String> = remember { mutableStateOf("MutableState") } // 狀態與屬性委託一起讀取
        val rememberState: String by remember { mutableStateOf("RememberState") } // 使用屬性委託讀取狀態。
        val offsetX by remember { mutableStateOf((-4).dp) }
        val color by remember { mutableStateOf(colors.error) }
        Text(
            text = "${mutableState.value} $rememberState",
            color = color,
            style = style,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset {// 在放置步驟中讀取 `offsetX` 狀態 計算偏移量時的佈局階段。 `offsetX` 的變化重新啟動佈局。
                IntOffset(offsetX.roundToPx(), 0)
            }
        )
    }

    @Composable
    fun DrawLineDemo(colors: Colors) {
        Canvas(modifier = Modifier
            .width(75.dp)
            .height(30.dp), onDraw = {
            drawLine(
                color = colors.error,
                start = Offset(0f, 0f),
                end = Offset(200f, 0f),
                strokeWidth = 30f,
                blendMode = BlendMode.Difference
            )
        })
        Canvas(modifier = Modifier
            .width(75.dp)
            .height(30.dp), onDraw = {
            drawLine(
                color = colors.error,
                start = Offset(0f, 0f),
                end = Offset(200f, 0f),
                strokeWidth = 30f,
                cap = StrokeCap.Round,
                blendMode = BlendMode.Clear
            )
        })
    }

    @Composable
    fun DrawOvalDemo(colors: Colors) {
        Canvas(modifier = Modifier
            .width(75.dp)
            .height(30.dp), onDraw = {
            drawOval(
                color = colors.error,
                size = Size(200.0f, 30.0f),
                alpha = 0.5f,
                blendMode = BlendMode.Color
            )
        })
    }

    @Composable
    fun DrawRectDemo(colors: Colors) {
        val color by remember { mutableStateOf(colors.error) }
        Canvas(modifier = Modifier
            .width(75.dp)
            .height(30.dp)) {
            drawRect( // 在繪圖階段讀取 `color` 狀態 當畫布被渲染時。 `color` 的變化重新開始繪圖。
                color = color,
                size = Size(200.0f, 30.0f), alpha = 0.5f,
                colorFilter = ColorFilter.lighting(Color.White, Color.Red),
                blendMode = BlendMode.ColorDodge
            )
        }
    }

    @Composable
    fun LazyListStateDemo(colors: Colors, style: TextStyle) {
        Surface(color = colors.onSurface) {
            val listState = rememberLazyListState()
            LazyRow(state = listState, modifier = Modifier.height(20.dp)) {
                item{
                    Text(
                        text = "LazyListState: ",
                        color = colors.primaryVariant,
                        style = style,
                        textAlign = TextAlign.Center,
                    )
                }
                items(20) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        contentDescription = getString(R.string.contentDescription),
                        modifier = Modifier.offset {
                            IntOffset(x = listState.firstVisibleItemScrollOffset / 2, y = listState.firstVisibleItemScrollOffset / 2) // Layout 中 firstVisibleItemScrollOffset 的狀態讀取
                        }
//                        modifier = Modifier.offset(
//                            with(LocalDensity.current) {// 組合中 firstVisibleItemScrollOffset 的狀態讀取
//                                (listState.firstVisibleItemScrollOffset / 2).toDp()
//                            }
//                        )
                    )
                }
            }
        }
    }

    @Composable
    fun ReorganizationLoopDemo(colors: Colors, style: TextStyle) {
        Box {
            var imageHeightPx by remember { mutableStateOf(0) }
            LazyRow(modifier = Modifier.height(20.dp)){
                item {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = getString(R.string.contentDescription),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { size ->
                                // Don't do this
                                imageHeightPx = size.height
                            }
                    )
                }
                item {
                    Text(
                        text = "imagePx: $imageHeightPx",
                        color = colors.surface,
                        style = style,
                        modifier = Modifier.padding(
                            horizontal = with(LocalDensity.current) { imageHeightPx.toDp() / 20 }
                        )
                    )
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                item {
                    ControlDemo(string = "Control", style = style)
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                item {
                    CustomDemo(colors = colors, style = style)
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                item {
                    val listColor = listOf(Color.Green, Color.Blue, Color.Red, Color.Yellow, Color.Cyan)
                    GradientButton(background = listColor) {
                        repeat(listColor.size) {
                            Text(
                                text = "Gradient Button",
                                color = colors.onError,
                                style = style,
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.width(4.dp))
                }
                item {
                    BespokeButton(colors = colors) {
                        Text(
                            text = "Bespoke Button",
                            color = colors.onError,
                            style = style,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ControlDemo(string: String, style: TextStyle) {
        val text = "Control"
        val remember = remember { Animatable(Color.Gray) }
        LaunchedEffect(text, Dispatchers.IO) {
            delay(3000)
            remember.animateTo(if (text == string) Color.Green else Color.Red)
        }
        Text(
            text = text,
            color = remember.value,
            style = style,
        )
    }

    @Composable
    fun CustomDemo(colors: Colors, style: TextStyle) {
        Surface(color = colors.surface) {
            CompositionLocalProvider { // 設置 LocalContentAlpha
                ProvideTextStyle(MaterialTheme.typography.subtitle1) {
                    Row {
                        Text(
                            text = "Custom",
                            color = colors.onSurface,
                            style = style
                        )
                }
             }
          }
       }
    }

    @Composable
    fun GradientButton(background: List<Color>, content: @Composable RowScope.() -> Unit) {
        val coroutineScope = rememberCoroutineScope()
        Row(
            modifier = Modifier
                .clickable {
                    coroutineScope.launch(Dispatchers.IO) {

                    }
                }
                .background(
                    Brush.horizontalGradient(background)
                )
        ) {
            CompositionLocalProvider { // set material LocalContentAlpha
                ProvideTextStyle(MaterialTheme.typography.button) {
                    content()
                }
            }
        }
    }

    @Composable
    fun BespokeButton(colors: Colors, content: @Composable RowScope.() -> Unit) {
        Row(
            modifier = Modifier
                .clickable {

                }
                .background(colors.error)
        ) {
            content()
        }
    }

    @Composable
    fun SortList(colors: Colors, style: TextStyle) {
        val list = listOf("Activity Manager", "Window Manager", "Content Providers", "View System", "Notification Manager")
        val listComparator = Comparator<String> { left, right ->
            right.compareTo(left)
        }
        val comparator by remember { mutableStateOf(listComparator) }
        val sortedList = remember(list, comparator) {
            list.sortedWith(comparator)
        }
        LazyRow(modifier = Modifier.height(20.dp)) {
            item {
                Text(
                    text = "Sort List: ",
                    color = colors.onError,
                    style = style,
                )
            }
            items(sortedList) { it ->
                Text(
                    text = "'$it' ",
                    color = colors.onSurface,
                    style = style
                )
            }
        }
    }

    private fun Context.showToast(msg: String) = Toast.makeText(this, msg, LENGTH_SHORT).show()
    sealed class Result {
        object Loading : Result()
        object Error : Result()
        class Success(val image: ImageRes) : Result()
    }
    class ImageRes(val imageIdRes: Int)
    class ImageRepository {
    /**
     * 返回可繪製資源或 null 以模擬具有成功或錯誤狀態的結果
     */
    suspend fun load(url: String): ImageRes? {
        delay(2000)
        // 如果得到一個隨機數為零，則添加 Random 以返回 null。 得到null的可能性是1/4
        return if (kotlin.random.Random.nextInt(until = 4) > 0) {
            val images = listOf(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background,)
            ImageRes(images[kotlin.random.Random.nextInt(images.size)]) // 每次調用 load 函數時加載一個隨機 id
        } else {
            null
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
        Message(
            "Colleague",
            "Hey, take a look at Jetpack Compose, it's great!\n" +
                    "It's the Android's modern toolkit for building native UI." +
                    "It simplifies and accelerates UI development on Android." +
                    "Less code, powerful tools, and intuitive Kotlin APIs :)"
        ),
        Message(
            "Colleague",
            "It's available from API 21+ :)"
        ),
        Message(
            "Colleague",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        Message(
            "Colleague",
            "Android Studio next version's name is Arctic Fox"
        ),
        Message(
            "Colleague",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        Message(
            "Colleague",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        Message(
            "Colleague",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        Message(
            "Colleague",
            "Previews are also interactive after enabling the experimental setting"
        ),
        Message(
            "Colleague",
            "Have you tried writing build.gradle with KTS?"
        ),
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
* #               佛祖保佑         永无BUG              #
* #                                                   #
*/