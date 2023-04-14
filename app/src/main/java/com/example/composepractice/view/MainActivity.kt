package com.example.composepractice.view

//import androidx.window.core.layout.WindowSizeClass
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.BitmapFactory.Options
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.SliderDefaults.DisabledTickAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.composepractice.Constants.Companion.CONTENT_DESCRIPTION
import com.example.composepractice.Constants.Companion.KEY_BEAN_EXAMPLE
import com.example.composepractice.Constants.Companion.KEY_BOOLEAN_EXAMPLE
import com.example.composepractice.Constants.Companion.KEY_STRING_EXAMPLE
import com.example.composepractice.Constants.Companion.KEY_STRING_EXAMPLE_2
import com.example.composepractice.Constants.Companion.ROUTE_FIVE
import com.example.composepractice.Constants.Companion.ROUTE_FOUR
import com.example.composepractice.Constants.Companion.ROUTE_MAIN
import com.example.composepractice.Constants.Companion.ROUTE_ONE
import com.example.composepractice.Constants.Companion.ROUTE_SIX
import com.example.composepractice.Constants.Companion.ROUTE_THREE
import com.example.composepractice.Constants.Companion.ROUTE_TWO
import com.example.composepractice.R
import com.example.composepractice.components.ScrollableAppBar
import com.example.composepractice.data.*
import com.example.composepractice.model.ThemeType
import com.example.composepractice.ui.theme.BloomTheme
import com.example.composepractice.ui.theme.ComposePracticeTheme
import com.example.composepractice.ui.theme.ComposeTutorialTheme
import com.example.composepractice.ui.theme.Shapes
import com.example.composepractice.viewmodel.AnimationViewModel
import com.funny.data_saver.core.*
import com.funny.data_saver.core.DataSaverConverter.registerTypeConverters
import com.funny.data_saver_mmkv.DefaultDataSaverMMKV
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.skydoves.landscapist.coil.CoilImage
import com.tencent.mmkv.MMKV
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.ranges.coerceAtLeast
import kotlin.reflect.KProperty


interface SampleInterface {
    fun log(message: String)
}

@OptIn(ExperimentalSerializationApi::class)
class MainActivity : ComponentActivity(), SampleInterface {

    private val localInterface = staticCompositionLocalOf<SampleInterface> { error("Not provided") }
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("dataStore")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(applicationContext)
        val dataSaverMMKV = DefaultDataSaverMMKV
        registerTypeConverters<DataSaverBean?>(
            save = { bean -> Json.encodeToString(bean) },
            restore = { str ->
                Log.d("ExampleActivity", "restore ExampleBean?: $str")
                Json.decodeFromString(str)
            }
        )
        registerTypeConverters(save = ThemeType.Saver, restore = ThemeType.Restorer)
        setContent {
            ComposableInterface(sampleInterface = this)
            ComposePracticeTheme {
                // 使用主題中“背景”顏色的表面容器
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CompositionLocalProvider(LocalDataSaver provides dataSaverMMKV){
                        PageJumpSamples(Message("Android", "Jetpack Compose"))
                    }
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
    fun Greeting(coroutineScope: CoroutineScope, name: String) {
        Text(
            text = "Hello $name!",
            color = Color.Green,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.clickable {
                coroutineScope.launch(Dispatchers.Main) {
                    TouristGuide.toWelcome()
                }
            }
        )
    }

    @Composable
    fun MessageCard(context: Context, msg: Message, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope, navController: NavController, colors: Colors, shapes: Shapes, typography: Typography, style: TextStyle) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Compose Practice", color = colors.onError) },
                    backgroundColor = Color(0xff0f9d58)
                )
            },
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .padding(it)
                ) { // 作为父级附加到嵌套滚动系统)
                    repeat(1) {
                        Column {
                            ComposePracticeTheme(darkTheme = false) {
                                Greeting(coroutineScope = coroutineScope, name = "Preview Composable")
                            }
                            Row(horizontalArrangement = Arrangement.SpaceAround) {
                                Row(modifier = Modifier
                                    .padding(all = 4.dp)
                                    .clickable {
                                        coroutineScope.launch(Dispatchers.Main) {
                                            navController.navigate(route = ROUTE_FIVE) {
                                                launchSingleTop = true
                                            }
                                        }
                                    }) {
                                    val modifier1 = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .border(1.5.dp, colors.secondary, CircleShape)
                                    val modifier2 = Modifier.size(20.dp)
                                    Image(
                                        painter = painterResource(R.drawable.ic_launcher_foreground),
                                        contentDescription = getString(CONTENT_DESCRIPTION),
                                        modifier = modifier1.then(modifier2)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Column {
                                        Text(
                                            text = msg.author,
                                            color = colors.secondaryVariant,
                                            style = typography.subtitle2
                                        )
                                        Text(
                                            text = msg.body,
                                            color = colors.secondaryVariant,
                                            style = typography.body2
                                        )
                                    }
                                }
                                ComposeTutorialTheme(darkTheme = true) {
                                    // 在我們的消息周圍添加填充
                                    Row(modifier = Modifier
                                        .padding(all = 8.dp)
                                        .clickable {
                                            coroutineScope.launch(Dispatchers.Main) {
                                                navController.navigate(route = ROUTE_SIX) {
                                                    launchSingleTop = true
                                                }
                                            }
                                        }) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_launcher_background),
                                            contentDescription = getString(CONTENT_DESCRIPTION),
                                            modifier = Modifier.size(40.dp) // 將圖像大小設置為 40 dp.clip(CircleShape) // 將圖像裁剪成圓形
                                        )
                                        // 在圖像和列之間添加一個水平空間墊片
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = msg.author,
                                                color = colors.primaryVariant,
                                                style = typography.subtitle2
                                            )
                                            // 在作者和消息文本之間添加一個垂直空間
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = msg.body,
                                                color = colors.primaryVariant,
                                                style = typography.body2
                                            )
                                        }
                                    }
                                }
                            }
                            ComposeTutorialTheme {
                                Conversation(
                                    SampleData.conversationSample,
                                    colors = colors,
                                    shapes = shapes,
                                    typography = typography,
                                    rememberCoroutineScope = coroutineScope
                                )
                            }
                            val listText by remember {
                                mutableStateOf(
                                    listOf(
                                        "Cupcake",
                                        "Donut",
                                        "Eclair",
                                        "Froyo",
                                        "Gingerbread",
                                        "Honeycomb",
                                        "Ice Cream Sandwich",
                                        "Jelly Bean",
                                        "KitKat",
                                        "Lollipop",
                                        "Marshmallow",
                                        "Nougat",
                                        "Oreo",
                                        "Pie"
                                    )
                                )
                            }
                            ListText(list = listText, colors = colors, style = style)
                            val listVersion by remember {
                                mutableStateOf(
                                    listOf(
                                        "Arctic Fox",
                                        "Bumblebee",
                                        "Chipmunk",
                                        "Dolphin",
                                        "Electric",
                                        "Flamingo"
                                    )
                                )
                            }
                            ListName(
                                header = "Android Studio Version:",
                                names = listVersion,
                                colors = colors,
                                style = style,
                                shapes = shapes
                            )
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
                                Box(
                                    modifier = Modifier
                                        .background(Color.Black, Shapes.small)
                                        .padding(8.dp, 0.dp)
                                ) {
                                    Spacer(
                                        Modifier
                                            .matchParentSize()
                                            .background(Color.Gray))
                                    Text(
                                        text = "modifier order",
                                        color = colors.error,
                                        style = style
                                    )
                                }
                                Spacer(modifier = eightDp)
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp, 0.dp)
                                        .background(Color.Black, Shapes.small)
                                ) {
                                    Spacer(
                                        Modifier
                                            .matchParentSize()
                                            .background(Color.Gray))
                                    Text(
                                        text = "modifier order",
                                        color = colors.error,
                                        style = style
                                    )
                                }
                                StatusRead(colors = colors, style = style)
                                DrawLineDemo(colors = colors)
                                DrawOvalDemo(colors = colors)
                                DrawRectDemo(colors = colors)
                            }
                            SampleStateButton(colors = colors, style = style)
                            RememberCoroutineScope(
                                context = context,
                                rememberCoroutineScope = coroutineScope,
                                scaffoldState = scaffoldState,
                                colors = colors,
                                style = style
                            )
                            SnapShotFlow(colors = colors, style = style)
                            LazyListStateDemo(colors = colors, style = style)
                            ReorganizationLoopDemo(coroutineScope = coroutineScope, colors = colors, style = style)
                            val list = listOf(
                                "Activity Manager",
                                "Window Manager",
                                "Content Providers",
                                "View System",
                                "Notification Manager"
                            )
                            val listModifier = Modifier
                                .height(20.dp)
                                .width(165.dp)
                            Row {
                                SortList(
                                    colors = colors,
                                    style = style,
                                    list = list,
                                    modifier = listModifier
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                DerivedStateList(
                                    coroutineScope = coroutineScope,
                                    colors = colors,
                                    style = style,
                                    list = list,
                                    modifier = listModifier
                                )
                            }
                            Row(modifier = Modifier.height(20.dp)) {
                                DelegatedProperties(colors = colors, style = style)
                                Spacer(modifier = Modifier.width(4.dp))
                                DestructuringDataClasses(colors = colors, style = style)
                                Spacer(modifier = Modifier.width(4.dp))
                                ScopesAndReceivers(colors = colors, style = style)
                            }
                            Row(modifier = Modifier.height(20.dp)) {
                                TypeSafeBuildersDSL()
                                Spacer(modifier = Modifier.width(4.dp))
                                MoveBoxWhereTapped()
                                Spacer(modifier = Modifier.width(4.dp))
                                Route(coroutineScope = coroutineScope, colors = colors, style = style, navController = navController, route = 1)
                                Spacer(modifier = Modifier.width(4.dp))
                                Route(coroutineScope = coroutineScope, colors = colors, style = style, navController = navController, route = 2)
                                Spacer(modifier = Modifier.width(4.dp))
                                Route(coroutineScope = coroutineScope, colors = colors, style = style, navController = navController, route = 3)
                                Spacer(modifier = Modifier.width(4.dp))
                                Route(coroutineScope = coroutineScope, colors = colors, style = style, navController = navController, route = 4)
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            LaunchedEffect(null, Dispatchers.IO) {
                                Log.v(
                                    "LaunchedEffect",
                                    "這個block執行在協程${Thread.currentThread().name}中"
                                )
                            }
                            DisposableEffect(
                                onStart = {
                                    Log.v(
                                        "DisposableEffect",
                                        "這個block執行在協程${Thread.currentThread().name}中"
                                    )
                                },
                                onStop = {
                                    Log.v(
                                        "DisposableEffect",
                                        "這個block執行在協程${Thread.currentThread().name}中"
                                    )
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
    fun Conversation(
        messages: List<Message>,
        colors: Colors,
        shapes: Shapes,
        typography: Typography,
        rememberCoroutineScope: CoroutineScope
    ) {
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
    fun ItemView(
        msg: Message,
        colors: Colors,
        shapes: Shapes,
        typography: Typography,
        rememberCoroutineScope: CoroutineScope
    ) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = getString(CONTENT_DESCRIPTION),
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
            Row(modifier = Modifier.weight(8.4f)) {
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
            LazyRow(
                modifier = Modifier
                    .paddingFromBaseline(top = 2.5.dp, bottom = 2.5.dp)
                    .weight(6.8f, true)
            ) {
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
    fun EditTextContent(
        name: String,
        onNameChange: (String) -> Unit,
        shapes: Shapes,
        typography: Typography
    ) {
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
        restore = { ParcelizeBean(it.toString(), it.toString()) }
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
        val selected = rememberSaveable(stateSaver = mapSaver) {
            mutableStateOf(
                ParcelizeBean(
                    "TW",
                    "MapSaver"
                )
            )
        }
        RestoreStateText(text = selected.value.value, style = style)
    }

    private val listSaver = listSaver<ParcelizeBean, Any>(
        save = { listOf(it.key, it.value) },
        restore = { ParcelizeBean(it[0] as String, it[1] as String) }
    )

    @Composable
    fun ListSaverScreen(style: TextStyle) {
        val selected = rememberSaveable(stateSaver = listSaver) {
            mutableStateOf(
                ParcelizeBean(
                    "TW",
                    "ListSaver"
                )
            )
        }
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
    fun SampleStateButton(colors: Colors, style: TextStyle) {
        var count1 = 1
        var count2 by remember { mutableStateOf(1) }
        val interactionSource = remember { MutableInteractionSource() }
//        val isPressed by interactionSource.collectIsPressedAsState()
        var resp by remember {
            mutableStateOf("")
        }
        LaunchedEffect(count2, Dispatchers.IO) {
            delay(400)
            resp = "LaunchedEffect"
//        "Thread = ${Thread.currentThread().name}"
        }
        var count by remember { mutableStateOf(1) } // age改变时person会自动刷新，引发Recompose
        val derivedState by remember { derivedStateOf { "derivedStateOf" } }
        LazyRow(horizontalArrangement = Arrangement.SpaceBetween) {
            val verticalArrangement = Arrangement.Center
            val horizontalAlignment = Alignment.CenterHorizontally
//            val modifier = Modifier.weight(1.0f)
            item {
                Column(
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment
                ) {
                    Text(text = resp, style = style)
                    Button(
                        onClick = { count2++ }) {
                        Text("$count2", color = colors.onError, style = style)
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment
                ) {
                    Text(text = "Normal", style = style)
                    Button(
                        onClick = { count1++ }) {
                        Text("$count1", color = colors.onError, style = style)
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment
                ) {
                    Text(text = "InteractionSource", style = style)
                    Button(
                        onClick = { count2-- },
                        interactionSource = interactionSource
                    ) {
                        Text("$count2", color = colors.onError, style = style)
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment
                ) {
                    Text(text = "$derivedState++", style = style)
                    Button(
                        onClick = { count += 1 }
                    ) {
                        Text(text = "$count", color = colors.onError, style = style)
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = verticalArrangement,
                    horizontalAlignment = horizontalAlignment
                ) {
                    Text(text = "$derivedState--", style = style)
                    Button(
                        onClick = { count -= 1 }
                    ) {
                        Text(text = "$count", color = colors.onError, style = style)
                    }
                }
            }
            item {
                ProduceStateButton(color = colors.onError, style = style)
            }
        }
    }

    @Composable
    fun RememberCoroutineScope(context: Context, rememberCoroutineScope: CoroutineScope, scaffoldState: ScaffoldState, colors: Colors, style: TextStyle) {
        // 創建綁定到 RememberCoroutineScope 生命週期的 CoroutineScope
        // `LaunchedEffect` 將取消並重新啟動 `scaffoldState.snackBarHostState` 變化
        val verticalArrangement = Arrangement.Center
        val horizontalAlignment = Alignment.CenterHorizontally
        Scaffold(scaffoldState = scaffoldState, modifier = Modifier.height(60.dp)) {
            Column(
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment,
                modifier = Modifier.padding(it)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = horizontalAlignment) {
                        Text(text = "rememberCoroutineScope", style = style)
                        Button(
                            onClick = {
                                // 在事件處理程序中創建一個新協程以顯示一個小吃店
                                rememberCoroutineScope.launch(Dispatchers.IO) {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Something happened!",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        ) {
                            Text("SnackBar", color = colors.onError, style = style)
                        }
                    }
                    RememberUpdatedState(colors, style)
                    SideEffect(style, horizontalAlignment)
                    ProduceState(context = context, colors = colors, style = style)
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
                    duration = SnackbarDuration.Short
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
            val names = arrayOf("List", "Size")
            for (name in names) {
                Text(text = name, color = color, style = style, textAlign = TextAlign.Center)
                count++
            }
            Text(
                text = "SideEffect\ncount:$count",
                color = color,
                style = style,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun ProduceState(context: Context, colors: Colors, style: TextStyle) {
        var timerStartStop by remember { mutableStateOf(false) }
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Time $timer", style = style)
                Button(onClick = {
                    timerStartStop = !timerStartStop
                }) {
                    Text(
                        text = if (timerStartStop) "Stop" else "Start",
                        color = colors.background,
                        style = style
                    )
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
        val list = mutableListOf(
            "Google",
            "Samsung",
            "SONY",
            "Asus",
            "HTC",
            "Nokia",
            "Sharp",
            "LG",
            "VIVO",
            "OPPO",
            "XiaoMi",
            "HuaWei",
            "OnePlus",
            "RealMe",
            "IQOO"
        )
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
    fun loadNetWorkImage(url: String, imageRepository: ImageRepository): State<Result> {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 未加載圖像時顯示加載圖像按鈕
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
                with(context) { showToast("🔥 ProduceStateExample() Result.Loading") }
                CircularProgressIndicator()
            }
            is Result.Error -> {
                println("❌ ProduceStateExample() Result.Error")
                with(context) { showToast("❌ ProduceStateExample() Result.Error") }
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = getString(CONTENT_DESCRIPTION)
                )
            }
            is Result.Success -> {
                println("✅ ProduceStateExample() Result.Success")
                with(context) { showToast("✅ ProduceStateExample() Result.Success") }
                val image = (imageState.value as Result.Success).image
                Image(
                    painterResource(id = image.imageIdRes),
                    modifier = Modifier.size(20.dp),
                    contentDescription = getString(CONTENT_DESCRIPTION)
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
        Canvas(
            modifier = Modifier
                .width(75.dp)
                .height(30.dp)
        ) {
            drawRect( // 在繪圖階段讀取 `color` 狀態 當畫布被渲染時。 `color` 的變化重新開始繪圖。
                color = color,
                size = Size(200.0f, 30.0f), alpha = 0.5f,
                colorFilter = ColorFilter.lighting(colors.onSurface, colors.error),
                blendMode = BlendMode.ColorDodge
            )
        }
    }

    @Composable
    fun LazyListStateDemo(colors: Colors, style: TextStyle) {
        Surface(color = colors.onSurface) {
            val listState = rememberLazyListState()
            LazyRow(state = listState, modifier = Modifier.height(20.dp)) {
                item {
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
                        contentDescription = getString(CONTENT_DESCRIPTION),
                        modifier = Modifier.offset {
                            IntOffset(
                                x = listState.firstVisibleItemScrollOffset / 2,
                                y = listState.firstVisibleItemScrollOffset / 2
                            ) // Layout 中 firstVisibleItemScrollOffset 的狀態讀取
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
    fun ReorganizationLoopDemo(coroutineScope: CoroutineScope, colors: Colors, style: TextStyle) {
        Box {
            var imageHeightPx by remember { mutableStateOf(0) }
            LazyRow(modifier = Modifier.height(20.dp)) {
                item {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = getString(CONTENT_DESCRIPTION),
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
                    GradientButton(coroutineScope = coroutineScope, background = listColor) {
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
    fun GradientButton(coroutineScope: CoroutineScope, background: List<Color>, content: @Composable RowScope.() -> Unit) {
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
    fun SortList(colors: Colors, style: TextStyle, list: List<String>, modifier: Modifier) {
        val listComparator = Comparator<String> { left, right -> right.compareTo(left) }
        val comparator by remember { mutableStateOf(listComparator) }
        val sortedList = remember(list, comparator) { list.sortedWith(comparator) }
        LazyRow(modifier = modifier) {
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

    @Composable
    fun DerivedStateList(coroutineScope: CoroutineScope, colors: Colors, style: TextStyle, list: List<String>, modifier: Modifier) {
        val listState = rememberLazyListState()
        LazyRow(state = listState, modifier = modifier) {
            item {
                Text(
                    text = "DerivedState List: ",
                    color = colors.onError,
                    style = style,
                )
            }
            items(list) { it ->
                Text(
                    text = "'$it' ",
                    color = colors.onSurface,
                    style = style
                )
            }
        }
//        val showButton by remember {
//            derivedStateOf {
//                listState.firstVisibleItemIndex > 0
//            }
//        }
        AnimatedVisibility(
            visible = !listState.isScrollingUp(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            GoToFirst {
                coroutineScope.launch(Dispatchers.IO) {
                    listState.scrollToItem(0)
                }
            }
        }
    }

    @Composable
    fun GoToFirst(goToFirst: () -> Unit) {
        Button(
            modifier = Modifier
                .padding(start = 4.dp)
                .height(15.dp)
                .width(15.dp),
            onClick = goToFirst,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "go to First"
            )
        }
    }

    @Composable
    fun BadComposable(colors: Colors, style: TextStyle) {
        var count by remember { mutableStateOf(0) } // 導致點擊重組
        Row(modifier = Modifier.height(20.dp)) {
            Button(
                onClick = { count++ },
                Modifier
                    .wrapContentSize()
                    .height(20.dp)
            ) {
                Text(
                    "Recompose",
                    color = colors.onError,
                    style = style
                )
            }
            Text(
                " $count ",
                color = colors.onError,
                style = style
            )
            if (count == 0) {
                count++
            }
            count++ // 向後寫入，在讀取後寫入狀態</b>
            Button(
                onClick = { count = 0 },
                Modifier
                    .wrapContentSize()
                    .height(20.dp)
            ) {
                Text(
                    "Stop",
                    color = colors.onError,
                    style = style
                )
            }
        }
    }

    @Composable
    fun DelegatedProperties(colors: Colors, style: TextStyle) {
        val exampleDelegate = ExampleDelegate()
        Text(
            "委託屬性: " + exampleDelegate.delegatedProperty,
            color = colors.secondaryVariant,
            style = style
        )
        println("委託屬性: " + exampleDelegate.delegatedProperty)
    }

    @Composable
    fun DestructuringDataClasses(colors: Colors, style: TextStyle) {
        val mary = Person(name = "Mary", age = 35)
        val (_, _) = mary
        Text(
            "${mary.name} ${mary.age}",
            color = colors.secondaryVariant,
            style = style
        )
    }

    @Composable
    fun ScopesAndReceivers(colors: Colors, style: TextStyle) {
        Row {
            Text(
                text = "ScopesAndReceivers",
                // This Text 位於 RowScope 中，因此它可以訪問 Alignment.CenterVertically 但不是 Alignment.CenterHorizontally，可用 在 ColumnScope 中。
                modifier = Modifier.align(Alignment.CenterVertically),
                color = colors.onError,
                style = style
            )
            Box(
                modifier = Modifier
                    .drawBehind {
                        // 此方法接受 DrawScope.() -> Unit 類型的 lambda 因此在這個 lambda 中我們可以訪問屬性和函數 可從 DrawScope 獲得，例如 `drawRectangle` 函數。
                        drawRect(Color.Black)
                    }
                    .size(40.dp, 20.dp)
            )
        }
    }

    @Composable
    fun TypeSafeBuildersDSL() {
        Canvas(Modifier.size(40.dp, 20.dp)) {
            // 繪製灰色背景，drawRect函數由接收方提供
            drawRect(color = Color.Gray)
            // 在左側/右側將內容插入 10 個像素 頂部/底部為 12
            inset(10.0f, 12.0f) {
                val quadrantSize = size / 2.0f
                // 在插入邊界內繪製一個矩形
                drawRect(
                    size = quadrantSize,
                    color = Color.Red
                )
                rotate(45.0f) {
                    drawRect(size = quadrantSize, color = Color.Blue)
                }
            }
        }
    }

    @Composable
    fun Coroutines(coroutineScope: CoroutineScope) {
        // 創建一個遵循此可組合項生命週期的 CoroutineScope
        val scrollState = rememberScrollState()
        Button(
            onClick = {
                // 創建一個滾動到列表頂部的新協程 並調用 ViewModel 加載數據
                coroutineScope.launch {
                    scrollState.animateScrollTo(0) // 這是一個暫停功能
//                    viewModel.loadData()
                }
            }
        ) {

        }
        Button(
            onClick = {
                // 滾動到頂部並通過創建一個新的並行加載數據 協程每個獨立的工作要做
                coroutineScope.launch {
                    scrollState.animateScrollTo(0)
                }
                coroutineScope.launch {
//                    viewModel.loadData()
                }
            }
        ) {

        }
    }

    @Composable
    fun MoveBoxWhereTapped() {
        // 創建一個 `Animatable` 來為 Offset 設置動畫並 `remember` 它。
        val animatedOffset = remember {
            Animatable(Offset(0f, 0f), Offset.VectorConverter)
        }
        Box(
            // pointerInput 修飾符接受一個掛起的代碼塊
            Modifier
                .size(40.dp, 20.dp)
                .pointerInput(Unit) {
                    // 創建一個新的 CoroutineScope 以便能夠創建新的 掛起函數中的協程
                    coroutineScope {
                        while (true) {
                            // 等待用戶點擊屏幕
                            val offset = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            // 啟動一個新的協程以異步動畫 用戶點擊屏幕的地方
                            launch(Dispatchers.IO) {
                                // 動畫到按下的位置
                                animatedOffset.animateTo(offset)
                            }
                        }
                    }
                }
        ) {
            Text("Tap", Modifier.align(Alignment.Center))
            Box(
                Modifier
                    .offset {
                        // 使用動畫偏移量作為這個Box的偏移量
                        IntOffset(
                            animatedOffset.value.x.roundToInt(),
                            animatedOffset.value.y.roundToInt()
                        )
                    }
                    .size(20.dp)
                    .background(Color(0xff3c1361), CircleShape)
            )
        }
    }

    @Composable
    fun Route(coroutineScope: CoroutineScope, colors: Colors, style: TextStyle, navController: NavController, route: Int) {
        val routeName = when(route) {
            1 -> {
                ROUTE_ONE
            }
            2 -> {
                ROUTE_TWO
            }
            3 -> {
                ROUTE_THREE
            }
            4 -> {
                ROUTE_FOUR
            }
            else -> {
                ""
            }
        }
        val text = when(routeName) {
            ROUTE_ONE -> {
                "OneActivity"
            }
            ROUTE_TWO -> {
                "TwoActivity"
            }
            ROUTE_THREE -> {
                "ThreeActivity"
            }
            ROUTE_FOUR -> {
                "FourActivity"
            }
            else -> {
                ""
            }
        }
        Surface(color = colors.secondary) {
            Text(
                text,
                color = colors.primarySurface,
                style = style,
                modifier = Modifier.clickable {
                    coroutineScope.launch(Dispatchers.Main) {
                        navController.navigate(route = routeName) {
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun PageJumpSamples(msg: Message) {
        val context = LocalContext.current
        val navController = rememberNavController()
        val scaffoldState = rememberScaffoldState()
        val coroutineScope = rememberCoroutineScope()
        val theme by remember { mutableStateOf(BloomTheme.DARK) }
        val backstackEntry = navController.currentBackStackEntryAsState() //获取当前的路由状态
        val route = backstackEntry.value?.destination?.route
        val shapes = MaterialTheme.shapes
        val colors = MaterialTheme.colors
        val style = TextStyle(fontSize = 11.sp)
        val typography = MaterialTheme.typography
        // 定義一個具有默認值的 CompositionLocal 全局對象
        // 這個實例可以被應用中的所有可組合項訪問
        val LocalElevations = compositionLocalOf { Elevations() }
        // 根據系統主題計算海拔
        val elevations = if (isSystemInDarkTheme()) {
            Elevations(card = 10.dp, default = 10.dp)
        } else {
            Elevations(card = 5.dp, default = 5.dp)
        }
        // 將高程綁定為 LocalElevations 的值
        TouristGuide.navController = navController
        CompositionLocalProvider(LocalElevations provides elevations) {
            // ... 內容放在這裡 ...
            // Composition 的這一部分將看到 `elevations` 實例
            // 當訪問 LocalElevations.current 時
            NavHost(navController = navController, startDestination = ROUTE_MAIN) {
                composable(
                    route = ROUTE_MAIN,
                ) {
                    ActivityMain(context = context, msg = msg, scaffoldState = scaffoldState, coroutineScope = coroutineScope, navController = navController, colors = colors, shapes = shapes, typography = typography, style = style)
                }
                composable(
                    //方法一
//            route = "OneActivity/{name}/{age}",
                    //方法二
                    route = ROUTE_ONE,
                    arguments = listOf(navArgument("name") {
                        type = NavType.StringType
                        defaultValue = "Tony"
                    }, navArgument("age") {
                        type = NavType.IntType
                        defaultValue = 34
                    })
                ) {
                    ActivityOne(context = context, name = it.arguments?.getString("name") ?: "Tony", it.arguments?.getInt("age") ?: -1, scaffoldState = scaffoldState, coroutineScope = coroutineScope, colors = colors, style = style, shapes = shapes, typography = typography, elevations = elevations, localElevation = LocalElevations) {
                        navController.popBackStack()
                    }
                }
                composable(route = ROUTE_TWO) {
                    ActivityTwo(context = context, coroutineScope = coroutineScope, colors = colors, shapes = shapes, typography = typography, style = style) {
                        navController.popBackStack()
                    }
                }
                composable(route = ROUTE_THREE) {
                    ActivityThree(context = context, coroutineScope = coroutineScope, colors = colors, shapes = shapes, typography = typography, style = style) {
                        navController.popBackStack()
                    }
                }
                composable(route = ROUTE_FOUR) {
                    ActivityFour(context = context, coroutineScope = coroutineScope, colors = colors, shapes = shapes, typography = typography, style = style) {
                        navController.popBackStack()
                    }
                }
                composable(route = ROUTE_FIVE) {
                    ActivityFive(context = context, coroutineScope = coroutineScope, colors = colors, shapes = shapes, typography = typography, style = style) {
                        navController.popBackStack()
                    }
                }
                composable(route = ROUTE_SIX) {
                    ActivitySix(context = context, coroutineScope = coroutineScope, colors = colors, shapes = shapes, typography = typography, style = style) {
                        navController.popBackStack()
                    }
                }
                composable(RouterPath.WELCOME) {
                    window.statusBarColor = MaterialTheme.colors.primary.toArgb()
                    BloomTheme(theme) {
                        WelcomePage {
                            navController.popBackStack()
                        }
                    }
                }
                composable(RouterPath.LOGIN) {
                    window.statusBarColor = MaterialTheme.colors.background.toArgb()
                    BloomTheme(theme) {
                        LoginPage {
                            navController.popBackStack()
                        }
                    }
                }
                composable(RouterPath.HOME) {
                    window.statusBarColor = MaterialTheme.colors.background.toArgb()
                    BloomTheme(theme) {
                        HomePage {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActivityMain(context: Context, msg: Message, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope, navController: NavController, colors: Colors, shapes: Shapes, typography: Typography, style: TextStyle) {
        MessageCard(context = context, msg = msg, scaffoldState = scaffoldState, coroutineScope = coroutineScope, navController = navController, colors = colors, shapes = shapes, typography = typography, style = style)
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ActivityOne(context: Context, name: String, age: Int, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope, colors: Colors, style: TextStyle, shapes: Shapes, typography: Typography, elevations: Elevations, localElevation: ProvidableCompositionLocal<Elevations>, navigation: () -> Unit) {
        val modalDrawer = rememberDrawerState(DrawerValue.Closed)
        val bottomDrawer = rememberBottomDrawerState(BottomDrawerValue.Closed)
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    scaffoldState.drawerState.apply {
                                        navigation()
                                    }
                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    scaffoldState.drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.Home,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                    }, title = {
                        Text(
                            text = "ActivityOne",
                            color = colors.onError,
                        )
                    }, actions = {
                        IconButtonDemo(
                            content = {
                                IconButton(onClick = {
                                }) {
                                    Icon(Icons.Filled.Info, getString(CONTENT_DESCRIPTION), tint = Color.White)
                                }
                            },
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                }
                            })
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(tint = colors.onError, imageVector = Icons.Default.MoreVert, contentDescription = getString(CONTENT_DESCRIPTION))
                        Text(
                            text = "更多",
                            color = colors.onError,
                            modifier = Modifier.clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                }
                            }
                        )
                    })
            },
            bottomBar = {
                BottomAppBar(cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50))) {
                    Text(
                        text = "Bottom AppBar",
                        color = colors.onError,
                    )
                    Spacer(Modifier.weight(1f, true))
                    IconButton(
                        onClick = {
                            coroutineScope.launch(Dispatchers.IO) {
                                // 默認 SnackBarDuration.Short
                                val result = scaffoldState.snackbarHostState.showSnackbar(message = "SnackBar", actionLabel = "Cancel", duration = SnackbarDuration.Indefinite)
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {

                                    }
                                    SnackbarResult.Dismissed -> {

                                    }
                                }
                            }
                        }) {
                        Icon(Icons.Filled.Favorite, getString(CONTENT_DESCRIPTION), tint = colors.onError)
                    }
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show") },
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            drawerContent = {
                Text("Drawer Header", color = colors.onError, modifier = Modifier.padding(16.dp), fontSize = 24.sp)
                Divider()
                Text("Drawer List", color = colors.onError, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
            },
            drawerGesturesEnabled = true,
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
        ) {
            Column(modifier = Modifier.padding(it)) {
                // 定義一個具有默認值的 CompositionLocal 全局對象 這個實例可以被應用中的所有可組合項訪問
                CompositionLocalExample()
                Row(modifier = Modifier.padding(4.dp, 0.dp)) {
                    Surface(elevation = elevations.default, color = Color.Transparent) {
                        Text(text = "我是${name}今年${age}歲", color = colors.secondaryVariant, fontSize = 12.sp, modifier = Modifier.clickable {
                            coroutineScope.launch(Dispatchers.Main) {
                            }
                        })
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(color = colors.onPrimary, modifier = Modifier
                        .height(20.dp)
                        .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                modalDrawer.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                        Text(text = "Modal Drawer", color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(color = colors.onPrimary, modifier = Modifier
                        .height(20.dp)
                        .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                bottomDrawer.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                        Text(text = "Bottom Drawer", color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    // 訪問全局定義的 LocalElevations 變量以獲取 Composition 這部分的當前 Elevations
                    Card(elevation = localElevation.current.card, backgroundColor = Color.Transparent) {
                        Text(text = "CompositionLocalProvider", color = Color.Red, fontSize = 12.sp, modifier = Modifier.clickable {

                        })
                    }
                    Surface(color = colors.onPrimary, modifier = Modifier
                        .height(20.dp)
                        .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                bottomDrawer.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                        Text(text = "Bottom Drawer", color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Row(modifier = Modifier.padding(4.dp, 0.dp)) {
                    Surface(elevation = elevations.default, color = Color.Transparent) {
                        InversionOfControl(coroutineScope = coroutineScope, colors = colors)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    AlertDialogSample(colors = colors)
                }
                Row(modifier = Modifier.padding(4.dp, 0.dp)) {
                    ButtonDemo(colors = colors)
                    Spacer(modifier = Modifier.width(4.dp))
                    ButtonStateDemo(colors = colors)
                    Spacer(modifier = Modifier.width(4.dp))
                    CardDemo()
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        ImageDemo()
                    }
                }
                Row {
                    CoilImageDemo(context = context)
                    SliderDemo(colors = colors)
                }
                TextDemo()
                Row {
                    ClickTextDemo()
                    Spacer(modifier = Modifier.width(4.dp))
                    TextEmphasisEffect()
                }
                Row {
                    BasicTextFieldDemo()
                    ScrollableSample()
                }
                ModalDrawer(
                    modifier = Modifier
                        .width(800.dp)
                        .height(60.dp),
                    drawerState = modalDrawer,
                    drawerContent = {
                        Text("Modal Drawer Header", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                        Divider()
                        Row{
                            repeat(5) {
                                Text("Modal Drawer List ", color = colors.onError, modifier = Modifier
                                    .padding(4.dp)
                                    .clickable { }, fontSize = 10.sp)
                            }
                        }
                    },
                ) {

                }
                BottomDrawer(
                    drawerState = bottomDrawer,
                    drawerContent = {
                        Text("Bottom Drawer Header", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                        Divider()
                        repeat(20) {
                            Text("Bottom Drawer List", color = colors.onError, modifier = Modifier
                                .padding(4.dp)
                                .clickable { }, fontSize = 10.sp)
                        }
                    }
                ) {

                }
            }
        }
    }

    @Composable
    fun LazyListLayout(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        Layout(
            modifier = modifier,
            content = content
        ) { measurAbles, constraints ->
            val placeAbles = measurAbles.map { measurable ->
                measurable.measure(constraints)
            }
            var yPosition = 0
            layout(constraints.maxWidth, constraints.maxHeight) {
                placeAbles.forEach { placeable ->
                    placeable.placeRelative(x = 0, y = yPosition)
                    yPosition += placeable.height
                }
            }
        }
    }

    @Composable
    fun ListColumnLayout() {
        CustomColumnLayout {
            repeat(8) {
                Text(text = "CustomColumnLayout", fontSize = 12.sp)
            }
        }
    }

    @Composable
    fun CustomColumnLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
        Layout(modifier = modifier, content = content) { measureAbles: List<Measurable>,
            constraints: Constraints ->
            // placeAbles 是经过测量的子元素，它拥有自身的尺寸值
            val placeAbles = measureAbles.map { measureAble ->
                // 测量所有子元素，这里不编写任何自定义测量逻辑，只是简单地
                // 调用 Measurable 的 measure 函数并传入 constraints
                measureAble.measure(constraints)
            }
            val width = placeAbles.sumOf { it.height }// 根据 placeAbles 计算得出
            val height = placeAbles.maxOf { it.width }// 根据 placeAbles 计算得出
            // 报告所需的尺寸
            layout(width, height) {
                var y = 0
                placeAbles.forEach { placeable ->
                    //通过遍历将每个项目放置到最终的预期位置
                    placeable.placeRelative(x = 0, y = y)
                    // 按照所放置项目的高度增加 y 坐标值
                    y += placeable.height
                }
            }
        }
    }

//    @Composable
//    fun MyComposable(myViewModel: MyViewModel = viewModel()) {
//        // ...
//        MyDescendant(myViewModel.data)
//    }
//
//    // 不要傳遞整個對象！ 正是後代所需要的。 此外，不要將 ViewModel 作為隱式依賴項使用 一個本地合成。
//    @Composable
//    fun MyDescendant(myViewModel: MyViewModel) { ... }
//
//    // 只傳遞後代需要的
//    @Composable
//    fun MyDescendant(data: DataToDisplay) {
//        // 顯示數據
//    }

//    @Composable
//    fun MyComposable(myViewModel: MyViewModel = viewModel()) {
//        // ...
//        ReusableLoadDataButton(
//            onLoadClick = {
//                myViewModel.loadData()
//            }
//        )
//    }
//
//    @Composable
//    fun ReusableLoadDataButton(onLoadClick: () -> Unit) {
//        Button(onClick = onLoadClick) {
//            Text("Load data")
//        }
//    }

    @Composable
    fun InversionOfControl(
        text: String = "InversionOfControl",
        coroutineScope: CoroutineScope,
        colors: Colors
    ) {
        ReusablePartOfTheScreen(
            colors = colors,
            content = {
                Text(text, color = colors.onError, fontSize = 12.sp, modifier = Modifier
                    .padding(4.dp)
                    .clickable {
                        coroutineScope.launch(Dispatchers.IO) {

                        }
                    })
            }
        )
    }

    @Composable
    fun ReusablePartOfTheScreen(content: @Composable () -> Unit, colors: Colors) {
        Surface(color = colors.onSecondary) {
            content()
        }
    }

    @Composable
    fun AlertDialogSample(colors: Colors) {
        val openDialog1 = remember { mutableStateOf(false) }
        val openDialog2 = remember { mutableStateOf(false) }
        val openDialog3 = remember { mutableStateOf(false) }

        Row {
            Text("AlertDialogOne", color = colors.secondaryVariant, fontSize = 12.sp, modifier = Modifier.clickable {
                openDialog1.value = true
            })
            Spacer(modifier = Modifier.width(4.dp))
            Text("AlertDialogTwo", color = colors.secondaryVariant, fontSize = 12.sp, modifier = Modifier.clickable {
                openDialog2.value = true
            })
            Spacer(modifier = Modifier.width(4.dp))
            Text("LinearProgressDialog", color = colors.secondaryVariant, fontSize = 12.sp, modifier = Modifier.clickable {
                openDialog3.value = true
            })
        }

        if(openDialog1.value) openDialog1.value = alertDialogOne(openDialog = openDialog1, colors = colors)
        if(openDialog2.value) openDialog2.value = alertDialogTwo(openDialog = openDialog2, colors = colors)
        if(openDialog3.value) openDialog3.value = linearProgressDialog(openDialog = openDialog3, colors = colors)
    }

    @Composable
    fun alertDialogOne(openDialog: MutableState<Boolean>, colors: Colors): Boolean {
        AlertDialog(
            backgroundColor = colors.primaryVariant,
//            contentColor = Color.Gray,
            onDismissRequest = { // 當用戶點擊對話框以外的地方或者按下系統返回鍵將會執行的代碼
                openDialog.value = false
            },
            title = {
                Text(
                    text = "開啟位置服務",
                    color = colors.onSecondary,
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = "這將意味著，我們會給您提供精準的位置服務，並且您將接受關於您訂閱的位置信息",
                    color = colors.onError,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    },
                ) {
                    Text(
                        "確認",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.button
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(
                        "取消",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.button
                    )
                }
            }
        )
        return openDialog.value
    }

    @Composable
    fun alertDialogTwo(openDialog: MutableState<Boolean>, colors: Colors): Boolean {
        AlertDialog(
            backgroundColor = colors.primaryVariant,
//            contentColor = Color.Gray,
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "開啟位置服務",
                    color = colors.surface,
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = "這將意味著，我們會給您提供精準的位置服務，並且您將接受關於您訂閱的位置信息",
                    color = colors.onError,
                    fontSize = 16.sp
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("必須接受！")
                    }
                }
            }
        )
        return openDialog.value
    }

    @Composable
    fun linearProgressDialog(openDialog: MutableState<Boolean>, colors: Colors): Boolean {
        Dialog(
            onDismissRequest = { openDialog.value = false }
        ) {
            Box(
                modifier = Modifier
                    .size(300.dp, 60.dp)
                    .background(colors.onError),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    LinearProgressIndicator()
                    Text("加載中ing...")
                }
            }
        }
        return openDialog.value
    }

    @Composable
    fun ButtonDemo(colors: Colors) {
        Button(
            modifier = Modifier.size(100.dp, 40.dp),
            onClick = {

            }) {
            Icon(
                // Material 庫中的圖標，有 Filled, Outlined, Rounded, Sharp, Two Tone 等
                Icons.Filled.Favorite,
                tint = colors.onError,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            // 添加間隔
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("喜歡", color = colors.onError, fontSize = 12.sp)
        }
    }

    @Composable
    fun ButtonStateDemo(colors: Colors) {
        // 獲取按鈕的狀態
        val interactionState = remember { MutableInteractionSource() }
        // 使用 Kotlin 的解構方法
        val (text, textColor, buttonColor) = when {
            interactionState.collectIsPressedAsState().value  -> ButtonState("Just Pressed", Color.Red, colors.onSecondary)
            else -> ButtonState( "Just Button", colors.onError, Color.Red)
        }
        Button(
            elevation = null,
            interactionSource = interactionState,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            onClick = {},
        ) {
            Text(
                text = text, color = textColor, fontSize = 12.sp
            )
        }
    }

    @Composable
    fun CardDemo() {
        Card(
            modifier = Modifier
                .size(100.dp)
                .padding(5.dp)
                .clickable {},
            // 設置點擊波紋效果，注意如果 CardDemo() 函數不在 MaterialTheme 下調用 將無法顯示波紋效果
            elevation = 10.dp // 設置陰影
        ) {
            Column(
                modifier = Modifier.padding(15.dp) // 內邊距
            ) {
                Text(
                    buildAnnotatedString {
                        append("歡迎來到 ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                        ) {
                            append("Jetpack Compose 博物館")
                        }
                    },
                    fontSize = 12.sp
                )
                Text(
                    buildAnnotatedString {
                        append("你現在觀看的章節是 ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                            append("Card")
                        }
                    },
                    fontSize = 12.sp
                )
            }
        }
    }

    @Composable
    fun IconButtonDemo(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        content: @Composable () -> Unit
    ) {
        Box(
            modifier = modifier
                .clickable(
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Button,
                    interactionSource = interactionSource,
                    indication = rememberRipple(bounded = false, radius = 10.dp)
                )
                .then(Modifier.size(24.dp)),
            contentAlignment = Alignment.Center
        ) { content() }
    }

    @Composable
    fun ImageDemo() {
        Surface(
            shape = CircleShape,
            border = BorderStroke(5.dp, Color.Gray)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
        }
    }

    @Composable
    fun CoilImageDemo(context: Context) {
        val imageLoader = ImageLoader.Builder(context)
            .componentRegistry {
                add(SvgDecoder(context))
            }
            .build()
        var flag by remember { mutableStateOf(false) }
        val size by animateDpAsState(targetValue = if(flag) 450.dp else 50.dp)
        CoilImage(
            imageModel = "https://coil-kt.github.io/coil/images/coil_logo_black.svg",
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clickable(
                    onClick = {
                        flag = !flag
                    },
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ),
            imageLoader = imageLoader
        )
    }

    @Composable
    fun SliderDemo(colors: Colors) { // 圆圈的颜色
        var progress by remember{ mutableStateOf(0f)}
        // 滑條未經過部分的默認 alpha 值
        val inactiveTrackAlpha = 0.24f
        // 當滑條被禁用的狀態下已經過部分的默認 alpha 值
        val disabledInactiveTrackAlpha = 0.12f
        // 當滑條被禁用的狀態下未經過部分的默認 alpha 值
        val disabledActiveTrackAlpha = 0.32f
        // 在滑條上方顯示的刻度的默認的 alpha 值
        val tickAlpha = 0.54f
        // 當刻度線被禁用時，默認的 alpha 值
        val activeTickColor = colors.onSurface
        val activeTrackColor = colors.primaryVariant
        val disabledInactiveTrackColor = colors.secondaryVariant
        val disabledActiveTrackColor = colors.onSecondary
        Slider(
            value = progress,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.primary,
                disabledThumbColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled).compositeOver(MaterialTheme.colors.surface),
                activeTrackColor = MaterialTheme.colors.primary,
                inactiveTrackColor = activeTrackColor.copy(alpha = inactiveTrackAlpha),
                disabledActiveTrackColor = MaterialTheme.colors.onSurface.copy(alpha = disabledActiveTrackAlpha),
                disabledInactiveTrackColor = disabledActiveTrackColor.copy(alpha = disabledInactiveTrackAlpha),
                activeTickColor = contentColorFor(activeTrackColor).copy(alpha = tickAlpha),
                inactiveTickColor = activeTrackColor.copy(alpha = tickAlpha),
                disabledActiveTickColor = activeTickColor.copy(alpha = DisabledTickAlpha),
                disabledInactiveTickColor = disabledInactiveTrackColor.copy(alpha = DisabledTickAlpha)
            ),
            onValueChange = {
                progress = it
            },
        )
    }

    @Composable
    fun TextDemo() { // 圆圈的颜色
        Text(
            text = "你好呀陌生人，這是一個標題，不是很長，因為我想不出其他什麼比較好的標題了".repeat(2),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Left,
            fontFamily = FontFamily.Serif,
            style = TextStyle(
                fontWeight = FontWeight.W900, //设置字体粗细
                fontSize = 20.sp,
                letterSpacing = 7.sp
            ),
            modifier = Modifier.clickable(
                onClick = { // 通知事件
                    Toast.makeText(applicationContext, "你點擊了此文本", Toast.LENGTH_LONG).show()
                },
                indication = null,
                interactionSource = MutableInteractionSource()
            )
        )
    }

    @Composable
    fun ClickTextDemo() { // 圆圈的颜色
        val annotatedText = buildAnnotatedString {
            append("勾選即代表同意")
            pushStringAnnotation(
                tag = "tag",
                annotation = "一個用戶協議BLABLABLA"
            )
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF0E9FF2),
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("用戶協議")
            }
            pop()
        }
        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                annotatedText.getStringAnnotations(
                    tag = "tag", start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    showToast("你已經點到 ${annotation.item} 啦")
                }
            },
            style = TextStyle(fontSize = 11.sp)
        )
    }

    @Composable
    fun TextEmphasisEffect() {
        Row{
            // 将内部 Text 组件的 alpha 强调程度设置为高
            // 注意: MaterialTheme 已经默认将强调程度设置为 high
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text("high Alpha", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(4.dp))
            // 將內部 Text 組件的 alpha 強調程度設置為中
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("medium Alpha", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(4.dp))
            // 將內部 Text 組件的 alpha 強調程度設置為禁用
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                Text("disabled Alpha", fontSize = 12.sp)
            }
        }
    }

    @Composable
    fun BasicTextFieldDemo() {
        var text by remember{mutableStateOf("")}
        var passwordHidden by remember{ mutableStateOf(false)}
        Box(modifier = Modifier
            .size(300.dp, 120.dp)) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                leadingIcon = {
                    Icon(Icons.Filled.Search, null)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color(0xFF0079D3),
                    backgroundColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordHidden = !passwordHidden
                        }
                    ){
                        Icon(painterResource(id = R.drawable.ic_launcher_foreground), null)
                    }
                },
                label = {
                    Text("密碼")
                },
                singleLine = true,
                visualTransformation = if(passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
            )
        }
//        var text by remember { mutableStateOf("") }
//        Box(
//            modifier = Modifier.fillMaxSize().background(Color(0xFFD3D3D3)),
//            contentAlignment = Alignment.Center
//        ) {
//            BasicTextField(
//                value = text,
//                onValueChange = {
//                    text = it
//                },
//                modifier = Modifier
//                    .background(Color.White, CircleShape)
//                    .height(35.dp)
//                    .fillMaxWidth(),
//                decorationBox = { innerTextField ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(horizontal = 10.dp)
//                    ) {
//                        IconButton(
//                            onClick = { }
//                        ) {
//                            Icon(painterResource(id = R.drawable.ic_launcher_foreground), getString(CONTENT_DESCRIPTION))
//                        }
//                        Box(
//                            modifier = Modifier.weight(1f),
//                            contentAlignment = Alignment.CenterStart
//                        ) {
//                            innerTextField()
//                        }
//                        IconButton(
//                            onClick = { },
//                        ) {
//                            Icon(Icons.Filled.Send, null)
//                        }
//                    }
//                }
//            )
//        }
//        var text by remember { mutableStateOf("") }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color(0xFFD3D3D3)),
//            contentAlignment = Alignment.Center
//        ) {
//            BasicTextField(
//                value = text,
//                onValueChange = {
//                    text = it
//                },
//                modifier = Modifier
//                    .background(Color.White)
//                    .fillMaxWidth(),
//                decorationBox = { innerTextField ->
//                    Column(
//                        modifier = Modifier.padding(vertical = 10.dp)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                        ) {
//                            IconButton(onClick = {}) {
//                                Icon(
//                                    painterResource(id = R.drawable.ic_launcher_background),
//                                    contentDescription = null
//                                )
//                            }
//                            IconButton(onClick = {}) {
//                                Icon(
//                                    painterResource(id = R.drawable.ic_launcher_background),
//                                    contentDescription = null
//                                )
//                            }
//                            IconButton(onClick = {}) {
//                                Icon(
//                                    painterResource(id = R.drawable.ic_launcher_background),
//                                    contentDescription = null
//                                )
//                            }
//                            IconButton(onClick = {}) {
//                                Icon(
//                                    painterResource(id = R.drawable.ic_launcher_background),
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                        Box(
//                            modifier = Modifier.padding(horizontal = 10.dp)
//                        ) {
//                            innerTextField()
//                        }
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.End
//                        ) {
//                            TextButton(onClick = { /*TODO*/ }) {
//                                Text("Send")
//                            }
//                            Spacer(Modifier.padding(horizontal = 10.dp))
//                            TextButton(onClick = { /*TODO*/ }) {
//                                Text("Close")
//                            }
//                        }
//                    }
//                }
//            )
//        }
    }

    @Composable
    fun IntrinsicPropertyMeasurementDemo1(modifier: Modifier = Modifier, text1: String, text2: String, style: TextStyle) {
        Row(modifier = modifier
            .height(IntrinsicSize.Min)
            .width(180.dp)) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start),
                text = text1,
                style = style
            )
            Divider(color = Color.Black, modifier = Modifier
                .fillMaxHeight()
                .width(1.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End),
                text = text2,
                style = style
            )
        }
    }

    @Composable
    fun IntrinsicPropertyMeasurementDemo2(colors: Colors, style: TextStyle) {
        Row {
            Box(
                Modifier
                    .size(20.dp)
                    .background(colors.primaryVariant)) {
                Text(text = "Jetpack Compose is an", color = colors.onError, style = style)
            }
            Box(
                Modifier
                    .size(20.dp)
                    .background(colors.secondaryVariant)) {
                Text(text = "excellent development tool", color = colors.onError, style = style)
            }
        }
    }

    @Composable
    fun IntrinsicRow(modifier: Modifier, content: @Composable () -> Unit) {
        Layout(
            content = content,
            modifier = modifier,
            measurePolicy = object: MeasurePolicy {
                override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
                    val divideConstraints = constraints.copy(minWidth = 0)
                    val mainPlaceAbles = measurables.filter {
                        it.layoutId == "main"
                    }.map {
                        it.measure(constraints)
                    }
                    val dividePlaceable = measurables.first { it.layoutId == "divider"}.measure(divideConstraints)
                    val midPos = constraints.maxWidth / 2
                    return layout(constraints.maxWidth, constraints.maxHeight) {
                        mainPlaceAbles.forEach {
                            it.placeRelative(0, 0)
                        }
                        dividePlaceable.placeRelative(midPos, 0)
                    }
                }

                override fun IntrinsicMeasureScope.minIntrinsicHeight(measurables: List<IntrinsicMeasurable>, width: Int): Int {
                    var maxHeight = 0
                    measurables.forEach {
                        maxHeight = it.maxIntrinsicHeight(width).coerceAtLeast(maxHeight)
                    }
                    return maxHeight
                }
            }
        )
    }

    @Composable
    fun SubcomposeRow(modifier: Modifier, text: @Composable () -> Unit, divider: @Composable (Int) -> Unit) { // 傳入高度
        SubcomposeLayout(modifier = modifier) { constraints->
            var maxHeight = 0
            val placeAbles = subcompose("text", text).map {
                val placeable = it.measure(constraints)
                maxHeight = placeable.height.coerceAtLeast(maxHeight)
                placeable
            }
            val dividerPlaceable = subcompose("divider") {
                divider(maxHeight)
            }.map {
                it.measure(constraints.copy(minWidth = 0))
            }
            assert(dividerPlaceable.size == 1) { "DividerScope Error!" }
            layout(constraints.maxWidth, constraints.maxHeight){
                placeAbles.forEach {
                    it.placeRelative(0, 0)
                }
                dividerPlaceable.forEach {
                    it.placeRelative(0, 0)
                }
            }
        }
    }

    @Composable
    fun ParentDataBox() { // 父元素
        Box(modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .background(Color.Yellow)){ // 子元素
            Box(modifier = Modifier
                .align(Alignment.Center)
                .size(25.dp)
                .background(Color.Blue))
        }
    }

    @Composable
    fun CountBox(context: Context) {
        Row {
            repeat(5) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(randomColor(count = it))
                        .count(context = context, num = Random.nextInt(30, 100))
                )
            }
        }
    }

    @Composable
    private fun randomColor(count: Int): Color {
        return when(count) {
            1 -> Color.Gray
            2 -> Color.LightGray
            3 -> Color.DarkGray
            4 -> Color.Black
            else -> Color.Cyan
        }
    }

    interface VerticalScope {
        @Stable
        fun Modifier.weight(weight: Float) : Modifier
    }

    class WeightParentData(val weight: Float=0f) : ParentDataModifier {
        override fun Density.modifyParentData(parentData: Any?) = this@WeightParentData
    }

    object VerticalScopeInstance : VerticalScope {
        @Stable
        override fun Modifier.weight(weight: Float): Modifier = this.then(
            WeightParentData(weight)
        )
    }

    @Composable
    fun WeightedVerticalLayout(
        modifier: Modifier = Modifier,
        content: @Composable VerticalScope.() -> Unit
    ) {
        val measurePolicy = MeasurePolicy { measurAbles, constraints ->
            val placeAbles = measurAbles.map {
                it.measure(constraints)
            }
            // 获取各weight值
            val weights = measurAbles.map {
                (it.parentData as WeightParentData).weight
            }
            val totalHeight = constraints.maxHeight
            val totalWeight = weights.sum()
            // 宽度：最宽的一项
            val width = placeAbles.maxOf { it.width }

            layout(width, totalHeight) {
                var y = 0
                placeAbles.forEachIndexed() { i, placeable ->
                    placeable.placeRelative(0, y)
                    // 按比例设置大小
                    y += (totalHeight * weights[i] / totalWeight).toInt()
                }
            }
        }
        Layout(modifier = modifier, content = { VerticalScopeInstance.content() }, measurePolicy = measurePolicy)
    }

    @Composable
    fun ScrollableSample() {
        // 實際可組合狀態
        var offset by remember { mutableStateOf(0f) }
        Box(
            Modifier
                .size(50.dp, 110.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    // Scrollable state：描述瞭如何消費scrolling delta和更新offset
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    }
                )
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(offset.toString())
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ActivityTwo(
        context: Context,
        coroutineScope: CoroutineScope,
        colors: Colors,
        shapes: Shapes,
        typography: Typography,
        style: TextStyle,
        navigation: () -> Unit
    ) {
        val scaffoldState = rememberBottomSheetScaffoldState()
        val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, animationSpec = tween(1000))
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                Text("Modal Drawer Header", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                Divider()
                repeat(20) {
                    Text("Modal Drawer List ", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 10.sp)
                }
            },
            sheetPeekHeight = 128.dp,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    navigation()
                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                    }, title = {
                        Text(
                            text = "ActivityTwo",
                            color = colors.onError,
                        )
                    }, actions = {
                        Icon(tint = colors.onError, imageVector = Icons.Default.Menu, contentDescription = getString(CONTENT_DESCRIPTION))
                        Text(
                            text = "菜單",
                            color = colors.onError,
                            modifier = Modifier.clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                    scaffoldState.drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        )
                    })
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show") },
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.End,
        ) {
            Column(modifier = Modifier.padding(it)) {
                Row {
                    Surface(color = colors.onPrimary, modifier = Modifier
                        .height(20.dp)
                        .clickable {
                            coroutineScope.launch(Dispatchers.IO) {
                                sheetState.apply {
                                    if (isVisible) sheetState.hide() else sheetState.show()
                                }
                            }
                        }) {
                        Text(text = "Bottom Sheet Scaffold", color = Color.Red, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IntrinsicPropertyMeasurementDemo2(colors = colors, style = style)
                    Spacer(modifier = Modifier.width(4.dp))
                    IntrinsicRow(modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)) {
                        Text(text = "Intrinsic", modifier = Modifier
                            .wrapContentWidth(Alignment.Start)
                            .layoutId("main"), style = style)
                        Divider(color = Color.Black, modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .layoutId("divider"))
                        Text(text = "Row", modifier = Modifier
                            .wrapContentWidth(Alignment.End)
                            .layoutId("main"), style = style)
                    }
                }
                Row {
                    IntrinsicPropertyMeasurementDemo1(text1 = "Intrinsic", text2 = "Size", style = style)
                    SubcomposeRow(
                        modifier = Modifier
                            .width(180.dp)
                            .height(20.dp),
                        text = {
                            Text(text = "Subcompose", Modifier.wrapContentWidth(Alignment.Start), style = style)
                            Text(text = "Row", Modifier.wrapContentWidth(Alignment.End), style = style)
                        }
                    ) {
                        val heightPx = with(LocalDensity.current) { it.toDp() }
                        Divider(
                            color = Color.Black,
                            modifier = Modifier
                                .width(1.dp)
                                .height(heightPx)
                        )
                    }
                }
                Row(modifier = Modifier.padding(4.dp, 0.dp)) {
                    Box(modifier = Modifier
                        .size(50.dp)
                        .background(Color.Red)
                        .wrapContentSize(align = Alignment.Center)
                        .size(25.dp)
                        .background(Color.Green))
                    ParentDataBox()
                    CountBox(context = context)
                }
                Row {
                    ListColumnLayout()
                    Spacer(modifier = Modifier.width(4.dp))
                    WeightedVerticalLayout(
                        Modifier
                            .height(100.dp)
                            .width(30.dp)) {
                        Box(modifier = Modifier
                            .width(30.dp)
                            .weight(1f)
                            .background(randomColor(1)))
                        Box(modifier = Modifier
                            .width(30.dp)
                            .weight(2f)
                            .background(randomColor(2)))
                        Box(modifier = Modifier
                            .width(30.dp)
                            .weight(7f)
                            .background(randomColor(3)))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationBtn(colors, style)
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationContentSize(colors, style)
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationCrossFade(colors, style)
                }
                Row {
                    AnimationAsState()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationSnap()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationAnimatable()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationBox()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationRememberInfiniteTransition()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationTargetBased()
                    Spacer(modifier = Modifier.width(4.dp))
                    AnimationVector()
                }
                Row {
                    AnimationSpring()
                    Spacer(modifier = Modifier.width(10.dp))
                    AnimationTween()
                }
                Row {
                    AnimationKeyframes()
                    Spacer(modifier = Modifier.width(10.dp))
                    AnimationRepeatable()
                }
                Row {
                    AnimationInfiniteRepeatable()
                    Spacer(modifier = Modifier.width(10.dp))
                    Gesture()
                }
                ModalBottomSheetLayout(
                    sheetState = sheetState,
                    sheetContent = { //这里显示底部弹窗内容
                        Text("Bottom Sheet Header", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                        Divider()
                        repeat(20) {
                            Text("Bottom Sheet List", color = colors.onError, modifier = Modifier
                                .padding(4.dp)
                                .clickable { }, style = style)
                        }
                    }, content = { //处理后退事件，显示和隐藏必须用协程执行
                        BackHandler(sheetState.isVisible) {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        } //显示页面内容
                    },
                )
            }
        }
    }

    @Composable
    fun AnimationBtn(colors: Colors, style: TextStyle) {
        // 如果你在这里有 getValue 的报错，或者无法自动导入，这是一些旧版 Android Studio 还没有完全适配 Compose 的 bug。
        // 你需要手动导入，或者更新到最新的 AS
        // import androidx.compose.runtime.getValue
        var state by remember{ mutableStateOf(true) }
        Surface(color = colors.surface) {
            Column(
                modifier = Modifier.height(130.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AnimatedVisibility(visible = state) {
                    Text(
                        text = "垂直消失",
                        fontWeight = FontWeight.W900,
                        style = style
                    )
                }
                AnimatedVisibility(
                    visible = state,
                    enter = slideInVertically(
                        initialOffsetY = { -40 }
                    ) + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(initialAlpha = 0.3f),
                    exit = shrinkHorizontally() + fadeOut()
                ) {
                    Text(
                        text = "水平消失",
                        fontWeight = FontWeight.W900,
                        style = style
                    )
                }
                Spacer(Modifier.size(40.dp, 78.dp))
                Surface(
                    modifier = Modifier
                        .height(20.dp)
                        .width(40.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    color = colors.primary
                ) {
                    Text(if(state) "隱藏" else "顯示", color = Color.White, style = style, textAlign = TextAlign.Center, modifier = Modifier.clickable {
                        state = !state
                    })
                }
            }
        }
    }

    @Composable
    fun AnimationContentSize(colors: Colors, style: TextStyle) {
        var text by remember{ mutableStateOf("contentSize 動畫") }
        Surface(color = colors.onPrimary) {
            Box(
                modifier = Modifier
                    .height(130.dp)
                    .width(85.dp),
                contentAlignment = Alignment.Center
            ){
                Text(text,
                    color = colors.onError,
                    modifier = Modifier
                        .clickable {
                            text += text
                        }
                        .animateContentSize(),
                    style = style
                )
            }
        }
    }

    @Composable
    fun AnimationCrossFade(colors: Colors, style: TextStyle) {
        var flag by remember{ mutableStateOf(false) }
        Column{
            Crossfade(
                targetState = flag, animationSpec = tween(1000)
            ) {
                when(it){
                    false -> Screen1()
                    true -> Screen2()
                }
            }
            Surface(color = colors.onSecondary, modifier = Modifier
                .size(50.dp, 20.dp)
                .align(alignment = Alignment.CenterHorizontally)) {
                Text("切換", color = colors.onError, style = style, textAlign = TextAlign.Center, modifier = Modifier.clickable {
                    flag = !flag
                })
            }
        }
    }

    @Composable
    fun Screen1() {
        Box(
            modifier = Modifier
                .background(Color.Red)
                .size(50.dp, 110.dp),
            contentAlignment = Alignment.Center
        ) {

        }
    }

    @Composable
    fun Screen2() {
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .size(50.dp, 110.dp),
            contentAlignment = Alignment.Center
        ) {

        }
    }

    @Composable
    fun AnimationAsState() {
        val enable = remember { mutableStateOf(false) }
        val alpha: Float by animateFloatAsState(
            targetValue = if (enable.value) 1f else 0.5f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
        val colors = animateColorAsState(targetValue = if (enable.value) Color.Green else Color.Red)
        val size = animateIntSizeAsState(targetValue = if (enable.value) IntSize(25, 25) else IntSize(50, 50))
        Box(
            Modifier
                .size(size.value.width.dp, size.value.height.dp)
                .graphicsLayer(alpha = alpha)
                .background(colors.value)
                .clickable {
                    enable.value = !enable.value
                }
        )
    }

    @Composable
    fun AnimationAnimatable() {
        var flag by remember{ mutableStateOf(false) }
        val color = remember{ Animatable(Color.Gray) }
        Box(
            modifier = Modifier
                .size(50.dp, 50.dp)
                .background(color.value)
                .clickable {
                    flag = !flag
                },
            contentAlignment = Alignment.Center
        ) {

        }
        LaunchedEffect(flag) {
            color.animateTo(
                targetValue = if (flag){ Color.Green } else { Color.Red },
                animationSpec = tween(1000)
            )
        }
    }

    @Composable
    fun AnimationSnap() { // 立即将值切换到结束值
        val enable = remember { mutableStateOf(false) }
        val alpha: Float by animateFloatAsState(targetValue = if (enable.value) 1f else 0.5f, animationSpec = snap(delayMillis = 1000))
        val colors = animateColorAsState(targetValue = if (enable.value) Color.Cyan else Color.Magenta)
        val size = animateIntSizeAsState(targetValue = if (enable.value) IntSize(25, 25) else IntSize(50, 50))
        Box(
            Modifier
                .size(size.value.width.dp, size.value.height.dp)
                .graphicsLayer(alpha = alpha)
                .background(colors.value)
                .clickable {
                    enable.value = !enable.value
                }
        )
    }

    enum class BoxState { Collapsed, Expanded }

    @Composable
    fun AnimationBox() {
        var boxState by remember { mutableStateOf(BoxState.Collapsed) }
        val transitionData = updateTransitionData(boxState)
        // UI 树
        Box(
            modifier = Modifier
                .background(transitionData.color)
                .size(transitionData.size)
                .clickable {
                    boxState = when (boxState) {
                        BoxState.Collapsed -> BoxState.Expanded
                        else -> BoxState.Collapsed
                    }
                }
        )
    }

    // 保存动画数值
    private class TransitionData(color: State<Color>, size: State<Dp>) {
        val color by color
        val size by size
    }

    // 创建一个 Transition 并返回其动画值。
    @Composable
    private fun updateTransitionData(boxState: BoxState): TransitionData {
        val transition = updateTransition(boxState, label = "")
        val color = transition.animateColor(label = "") { state ->
            when (state) {
                BoxState.Collapsed -> Color.Blue
                BoxState.Expanded -> Color.Red
            }
        }
        val size = transition.animateDp(label = "") { state ->
            when (state) {
                BoxState.Collapsed -> 25.dp
                BoxState.Expanded -> 50.dp
            }
        }
        return remember(transition) { TransitionData(color, size) }
    }

    @Composable
    fun AnimationRememberInfiniteTransition() {
        val infiniteTransition = rememberInfiniteTransition()
        val color by infiniteTransition.animateColor(
            initialValue = Color.Red, // 初始值
            targetValue = Color.Green, // 最终值
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing), // 一个动画值的转换持续 1 秒，缓和方式为 LinearEasing
                repeatMode = RepeatMode.Reverse
                // 指定动画重复运行的方式，
                // Reverse 为 init -> target, target -> init, init -> target
                // Repeat 为 init -> target, init -> target, init -> target
            )
        )
        Box(
            Modifier
                .size(50.dp)
                .background(color))
    }

    @Composable
    fun AnimationTargetBased() {
        var state by remember {
            mutableStateOf(0)
        }
        val anim = remember {
            TargetBasedAnimation(
                animationSpec = tween(2000),
                typeConverter = Float.VectorConverter,
                initialValue = 100f,
                targetValue = 300f
            )
        }
        var playTime by remember { mutableStateOf(0L) }
        var animationValue by remember {
            mutableStateOf(0)
        }
        LaunchedEffect(state) {
            val startTime = withFrameNanos { it }
            println("进入协程：")
            do {
                playTime = withFrameNanos { it } - startTime
                animationValue = anim.getValueFromNanos(playTime).toInt()
            } while (!anim.isFinishedFromNanos(playTime))

        }
        Box(modifier = Modifier.size(50.dp),contentAlignment = Alignment.Center) {
            Box(modifier = Modifier
                .size(animationValue.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(animationValue / 9))
                .clickable {
                    state++
                }, contentAlignment = Alignment.Center) {
                Text(text = animationValue.toString(), style = TextStyle(color = Color.White, fontSize = (animationValue/9).sp))
            }
        }
    }

    @Composable
    fun AnimationSpring() { // 彈簧
        val enable = remember { mutableStateOf(true) }
        val value = animateIntAsState(
            targetValue = if (enable.value) 300 else 100,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        Box(
            Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .width(value.value.dp)
                    .height(50.dp)
                    .background(Color.Blue, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                    .clickable {
                        enable.value = !enable.value
                    }, contentAlignment = Alignment.CenterStart
            ) {

            }
        }
    }

    @Composable
    fun AnimationTween() { // 延伸填滿
        val state = remember { mutableStateOf(true) }
        val value = animateIntAsState(
            targetValue = if (state.value) 300 else 100,
            animationSpec = tween(
                durationMillis = 1500,
                delayMillis = 200,
                easing = LinearEasing
            )
        )
        Box(Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .width(value.value.dp)
                    .height(50.dp)
                    .background(Color.Green, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                    .clickable {
                        state.value = !state.value
                    }
            ) {

            }
        }
    }

    @Composable
    fun AnimationKeyframes() { // 階段性彈跳
        var state by remember { mutableStateOf(true) }
        val value by animateIntAsState(
            targetValue = if (state) 300 else 100,
            animationSpec = keyframes {
                durationMillis = 1500
                0 at 700 with LinearOutSlowInEasing
                700 at 1400 with FastOutLinearInEasing
                1400 at 2000
            })
        Box(Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .width(value.dp)
                    .height(50.dp)
                    .background(Color.Yellow, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                    .clickable {
                        state = !state
                    }
            ) {

            }
        }
    }

    @Composable
    fun AnimationRepeatable() { // 执行有限次数动画后自动停止
        var state by remember { mutableStateOf(true) }
        val value by animateIntAsState(
            targetValue = if (state) 300 else 100,
            animationSpec = repeatable(
                iterations = 5,//动画重复执行的次数，设置多少就执行多少次
                animation = tween(durationMillis = 1500),
                repeatMode = RepeatMode.Reverse
            )
        )
        Box(
            Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .width(value.dp)
                    .height(50.dp)
                    .background(Color.Red, RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                    .clickable {
                        state = !state
                    }
            ) {

            }
        }
    }

    @Composable
    fun AnimationInfiniteRepeatable() {
        var state by remember { mutableStateOf(true) }
        val value by animateIntAsState(
            targetValue = if (state) 300 else 100,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )
        Box(
            Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .width(value.dp)
                    .height(50.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
                    )
                    .clickable {
                        state = !state
                    }
            ) {

            }
        }
    }

    @Composable
    fun AnimationVector() {
        var state by remember { mutableStateOf(true) }
        val value by animateValueAsState(
            targetValue = if (state) AnimationSize(0xffff5500, 100f) else AnimationSize(0xff00ff00, 300f),
            typeConverter = TwoWayConverter(
                convertToVector = {
//                AnimationVector2D(target.color.toFloat(), target.size)
                    AnimationVector2D(it.color.toFloat(), it.size)
                },
                convertFromVector = {
                    AnimationSize(it.v1.toLong(), it.v2)
                }
            )
        )
        println("颜色:${value.color}")
        Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(value.size.dp)
//                .size(300.dp)
                    .background(Color(value.color), RoundedCornerShape(60.dp))
                    .clickable {
                        state = !state
                    }
            ) {

            }
        }
    }

    @Composable
    fun Gesture() {
        val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }
        Box(
            Modifier.size(170.dp, 50.dp), contentAlignment = Alignment.CenterStart) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Color.DarkGray,
                        RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp)
                    )
                    .clickable {

                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            coroutineScope {
                                while (true) { // 检测一个点击头事件并获得其位置。
                                    val position = awaitPointerEventScope {
                                        awaitFirstDown().position
                                    }
                                    launch { // 应用到点击的位置。
                                        offset.animateTo(position)
                                    }
                                }
                            }
                        }
                ) {
                    Box(modifier = Modifier
                        .size(20.dp)
                        .offset { offset.value.toIntOffset() }
                        .background(Color.Cyan, RoundedCornerShape(60.dp))) {
                    }
                }
            }
        }
    }

    private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    @Composable
    fun ActivityThree(context: Context, coroutineScope: CoroutineScope, colors: Colors, shapes: Shapes, typography: Typography, style: TextStyle, navigation: () -> Unit) {
        val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
        BackdropScaffold(
            scaffoldState = scaffoldState,
            appBar = {
                TopAppBar(navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = getString(CONTENT_DESCRIPTION), Modifier.clickable {
                            coroutineScope.launch {
                                navigation()
                            }
                        },
                        tint = colors.onError
                    )
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = getString(CONTENT_DESCRIPTION), Modifier.clickable {
                            coroutineScope.launch {
                                if (scaffoldState.isConcealed) {
                                    scaffoldState.reveal()
                                } else {
                                    scaffoldState.conceal()
                                }
                            }
                        },
                        tint = colors.onError
                    )
                },
                    title = { Text(text = "ThreeActivity", color = colors.onError) }
                )
            },
            backLayerContent = {
                val list = listOf(
                    MemberItemData(R.drawable.ic_dark_logo, "dark logo"),
                    MemberItemData(R.drawable.ic_light_logo, "light logo"),
                    MemberItemData(R.drawable.ic_dark_welcome_bg, "dark bg"),
                    MemberItemData(R.drawable.ic_light_welcome_bg, "light bg"),
                    MemberItemData(R.drawable.ic_dark_welcome_illos, "dark illos"),
                    MemberItemData(R.drawable.ic_light_welcome_illos, "light illos"),
                    MemberItemData(R.drawable.ic_account_circle, "account circle"),
                    MemberItemData(R.drawable.ic_done, "done"),
                    MemberItemData(R.drawable.ic_favorite_border, "favorite border"),
                    MemberItemData(R.drawable.ic_filter_list, "filter list"),
                    MemberItemData(R.drawable.ic_shopping_cart, "shopping cart"),
                    MemberItemData(R.drawable.ic_search, "search"),
                    MemberItemData(R.drawable.ic_home, "home"),
                    MemberItemData(R.drawable.ic_launcher_foreground, "launcher foreground"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                    MemberItemData(R.drawable.ic_launcher_background, "launcher background"),
                )
                Column {
                    HorizontalPager(list = list, colors = colors, style = style)
                    Row {
                        Column(modifier = Modifier.width(125.dp)) {
                            Text("BackLayer Header", color = colors.onPrimary, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                            Divider()
                            ListWithHeader(colors = colors, style = style)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        LazyVerticalGridSample(list = list, colors = colors, style = style)
                        Spacer(modifier = Modifier.width(4.dp))
                        ListItemKey(colors = colors, style = style)
                    }
                }
            },
            frontLayerContent = {
                Column {
                    Text("FrontLayer Header", color = colors.onError, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                    Divider(color = colors.onSecondary, modifier = Modifier.height(1.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        AnimationDefault(colors = colors, style = style)
                        Spacer(modifier = Modifier.width(4.dp))
                        AnimationApproach(colors = colors, style = style)
                        Spacer(modifier = Modifier.width(4.dp))
                        AnimationEnterTransition(colors = colors, style = style)
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(color = colors.onError) {
                            Column {
                                AnimationIcon1()
                                Spacer(modifier = Modifier.width(4.dp))
                                AnimationSearchBar1()
                            }
                        }
                    }
                    Row {
                        Surface(color = colors.onError) {
                            Column {
                                AnimationIcon2()
                                Spacer(modifier = Modifier.width(4.dp))
                                AnimationSearchBar2()
                            }
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(color = colors.onSurface) {
                            SwitchBlock(style = style)
                        }
                        LocalComposition(style = style)
                    }
                    Row {
                        Column {
                            CompositionLocalDemo(colors = colors, style = style)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Row(modifier = Modifier.height(40.dp)) {
                                ScrollBoxesSmooth(colors = colors, style = style)
                                Spacer(modifier = Modifier.width(4.dp))
                                ScrollableExample(colors = colors, style = style)
                            }
                            Spacer(modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth())
                            Row {
                                ClickableDemo(style = style)
                                Spacer(modifier = Modifier.width(4.dp))
                                CombinedClickDemo(style = style)
                            }
                            Spacer(modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth())
                            DraggableDemo(colors = colors, style = style)
                            Spacer(modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth())
                            SwipeableDemo(colors = colors, style = style)
                            Spacer(modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth())
                            Row {
                                HorizontalListDemo(colors = colors, style = style)
                                Spacer(modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxWidth())
                                HorizontalListTheme(colors = colors, style = style)
                            }
                            Spacer(modifier = Modifier
                                .height(4.dp)
                                .fillMaxWidth())
                            Row {
                                TransformerDemo(colors = colors, style = style)
                                Spacer(modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxWidth())
                                DragGestureDemo(colors = colors, style = style)
                            }
                        }
                    }
                    Spacer(modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth())
                    Row {
                        NestedBoxDemo()
                        Spacer(modifier = Modifier
                            .width(4.dp)
                            .fillMaxWidth())
                        ConsumeDemo()
                        Spacer(modifier = Modifier
                            .width(4.dp)
                            .fillMaxWidth())
                        BaseDragGestureDemo(colors = colors, style = style)
                        Spacer(modifier = Modifier
                            .width(4.dp)
                            .fillMaxWidth())
                        AwaitDragOrCancellation(colors = colors, style = style)
                    }
                }
            },
            peekHeight = 120.dp,
            headerHeight = 60.dp,
            gesturesEnabled = true
        ) {

        }
    }

    @Composable
    fun AnimationDefault(colors: Colors, style: TextStyle) {
        var state by remember{ mutableStateOf(true)}
        Surface(color = colors.onError) {
            Column(
                modifier = Modifier.size(75.dp, 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AnimatedVisibility(visible = state) {
                    Text(
                        text = "Animation\nDefault",
                        fontWeight = FontWeight.W900,
                        style = style
                    )
                }
                Spacer(Modifier.padding(vertical = 16.dp))
                Button(onClick = { state = !state }) {
                    Text(if(state) "隱藏" else "顯示", color = colors.onSurface)
                }
            }
        }
    }

    @Composable
    fun AnimationApproach(colors: Colors, style: TextStyle) {
        var state by remember{ mutableStateOf(true)}
        Surface(color = colors.onError) {
            Column(
                modifier = Modifier.size(75.dp, 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AnimatedVisibility(
                    visible = state,
                    enter = slideInVertically(
                        initialOffsetY = { - 1000 },
                        animationSpec = tween(durationMillis = 1200)
                    )
                ) {
                    Text(
                        text = "Animation\nApproach",
                        fontWeight = FontWeight.W900,
                        style = style
                    )
                }
                Spacer(Modifier.padding(vertical = 16.dp))
                Button(onClick = { state = !state }) {
                    Text(if(state) "隱藏" else "顯示", color = colors.onSurface)
                }
            }
        }
    }

    @Composable
    fun AnimationEnterTransition(colors: Colors, style: TextStyle) {
        var state by remember{ mutableStateOf(true)}
        Surface(color = colors.onError) {
            Column(
                modifier = Modifier.size(75.dp, 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AnimatedVisibility(
                    visible = state,
                    enter = slideInVertically(
                        initialOffsetY = { - 1000 },
                        animationSpec = tween(durationMillis = 1200)
                    ) + fadeIn(
                        animationSpec = tween(durationMillis = 1200)
                    )
                ) {
                    Text(
                        text = "Animation\nEnter\nTransition",
                        fontWeight = FontWeight.W900,
                        style = style
                    )
                }
                Spacer(Modifier.padding(vertical = 10.dp))
                Button(onClick = { state = !state }) {
                    Text(if(state) "隱藏" else "顯示", color = colors.onSurface)
                }
            }
        }
    }

    @Composable
    fun AnimationIcon1() {
        var change by remember{ mutableStateOf(false) }
        var flag by remember{ mutableStateOf(false) }
        val buttonSize by animateDpAsState(
            targetValue = if(change) 32.dp else 24.dp
        )
        if(buttonSize == 32.dp) {
            change = false
        }
        IconButton(
            onClick = {
                change = true
                flag = !flag
            }
        ) {
            Icon(Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(buttonSize),
                tint = if(flag) Color.Red else Color.Gray
            )
        }
    }

    @Composable
    fun AnimationSearchBar1() {
        var text by remember{ mutableStateOf("") }
        var focusState by remember { mutableStateOf(false) }
        val size by animateFloatAsState(targetValue = if(focusState) 1f else 0.5f)
        Column(
            modifier = Modifier.size(120.dp, 52.dp)
        ) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .onFocusChanged {
                        focusState = it.isFocused
                    }
                    .fillMaxWidth(size)
            )
        }
    }

    @Composable
    fun AnimationIcon2() {
        var change by remember{ mutableStateOf(false) }
        var flag by remember{ mutableStateOf(false) }
        val buttonSizeVariable = remember { Animatable(24.dp, Dp.Companion.VectorConverter) }

        LaunchedEffect(change) {
            buttonSizeVariable.animateTo(if(change) 32.dp else 24.dp)
        }
        if(buttonSizeVariable.value == 32.dp) {
            change = false
        }
        IconButton(
            onClick = {
                change = true
                flag = !flag
            }
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                modifier = Modifier.size(buttonSizeVariable.value),
                tint = if(flag) Color.Red else Color.Gray
            )
        }
    }

    @Composable
    fun AnimationSearchBar2() {
        var text by remember{ mutableStateOf("") }
        var focusState by remember { mutableStateOf(false)}
        val sizeVariable = remember { Animatable(0f) }

        LaunchedEffect(focusState) {
            sizeVariable.animateTo(if(focusState) 1f else 0.5f)
        }
        Column(
            modifier = Modifier.size(154.dp, 52.dp)
        ) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .onFocusChanged {
                        focusState = it.isFocused
                    }
                    .fillMaxWidth(sizeVariable.value)
            )
        }
    }

    sealed class SwitchState {
        object OPEN: SwitchState()
        object CLOSE: SwitchState()
    }

    @Composable
    fun SwitchBlock(style: TextStyle){
        var selectedState: SwitchState by remember { mutableStateOf(SwitchState.CLOSE) }
        val transition = updateTransition(selectedState, label = "switch_transition")
        val selectBarPadding by transition.animateDp(transitionSpec = { tween(1000) }, label = "") {
            when (it) {
                SwitchState.CLOSE -> 40.dp
                SwitchState.OPEN -> 0.dp
            }
        }
        val textAlpha by transition.animateFloat(transitionSpec = { tween(1000) }, label = "") {
            when (it) {
                SwitchState.CLOSE -> 1f
                SwitchState.OPEN -> 0f
            }
        }
        val imageAlpha by transition.animateFloat(transitionSpec = { tween(1000) }, label = "") {
            when (it) {
                SwitchState.CLOSE -> 0f
                SwitchState.OPEN -> 1f
            }
        }
        Box(
            modifier = Modifier
                .size(130.dp, 100.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    selectedState =
                        if (selectedState == SwitchState.OPEN) SwitchState.CLOSE else SwitchState.OPEN
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "node_background",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(imageAlpha)
            )
            Text(
                text = "點我",
                style = style,
                fontSize = 30.sp,
                color = Color.White,
                fontWeight = FontWeight.W900,
                modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(textAlpha)
            )
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(30.dp)
                .padding(top = selectBarPadding)
                .background(Color(0xFF5FB878))
            ) {
                Row(modifier = Modifier
                    .align(Alignment.Center)
                    .alpha(1 - textAlpha)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "star", tint = Color.White)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "已選擇",
                        style = style,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.W900,
                    )
                }
            }
        }
    }

    @Composable
    fun LocalComposition(style: TextStyle) {
        val localString = compositionLocalOf { "Composition Local Of" }
        ComposeTutorialTheme(true) {
            Column {
                CompositionLocalProvider(
                    localString provides "Composition Local Provider"
                ) {
                    Text(
                        text = localString.current,
                        color = Color.Green,
                        style = style
                    )
                    CompositionLocalProvider(
                        localString provides "Composition Local String"
                    ) {
                        Text(
                            text = localString.current,
                            color = Color.Yellow,
                            style = style
                        )
                    }
                }
                Text(
                    text = localString.current,
                    color = Color.Red,
                    style = style
                )
                Text(
                    text = "Slash Tang",
                    color = Color.Cyan,
                    style = style
                )
            }
        }
    }

    var isStatic = false
    var compositionLocalName = ""
    val currentLocalColor = if (isStatic) {
        compositionLocalName = "StaticCompositionLocal 場景"
        staticCompositionLocalOf { Color.Black }
    } else {
        compositionLocalName = "DynamicCompositionLocal 場景"
        compositionLocalOf { Color.Black }
    }
    var recomposeFlag = "Init"
    @Composable
    fun CompositionLocalDemo(isStatic: Boolean = false, colors: Colors, style: TextStyle) {
        var color by remember{ mutableStateOf(Color.Green) }
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = compositionLocalName, color = colors.onError, modifier = Modifier.clickable {
                    color = if (color == Color.Green) {
                        colors.onError
                    } else {
                        Color.Green
                    }
                })
                Spacer (Modifier.height(10.dp))
                CompositionLocalProvider(
                    currentLocalColor provides color
                ) {
                    TaggedBox("Wrapper: $recomposeFlag", 240.dp, Color.Red, style = style) {
                        TaggedBox("Middle: $recomposeFlag", 180.dp, currentLocalColor.current, style = style) {
                            TaggedBox("Inner: $recomposeFlag", 120.dp, Color.Yellow, style = style)
                        }
                    }
                }
            }
        }
        recomposeFlag = "Recompose"
    }

    @Composable
    fun TaggedBox(tag:String, size: Dp, background: Color, style: TextStyle, content: @Composable () -> Unit = {}) {
        Column(
            modifier = Modifier
                .size(size)
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = tag, style = style)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }

    @Composable
    private fun ScrollBoxesSmooth(colors: Colors, style: TextStyle) {
        // 在第一個構圖上平滑滾動 100px
        val state = rememberScrollState()
        LaunchedEffect(Unit) { state.animateScrollTo(110) }
        Column(
            modifier = Modifier
                .background(Color.DarkGray)
                .width(55.dp)
                .height(40.dp)
                .verticalScroll(state)
        ) {
            repeat(10) {
                Text("Item $it", modifier = Modifier.padding(2.dp), color = colors.onError, style = style)
            }
        }
    }

    @Composable
    fun ScrollableExample(colors: Colors, style: TextStyle) {
        // 實際可組合狀態
        var offset by remember { mutableStateOf(0f) }
        Box(
            Modifier
                .width(55.dp)
                .height(40.dp)
                .scrollable(
                    orientation = Orientation.Vertical,
                    // Scrollable state：描述瞭如何消費scrolling delta和更新offset
                    state = rememberScrollableState { delta ->
                        offset += delta
                        delta
                    }
                )
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(offset.toString(), color = colors.onError, style = style)
        }
    }

    @Composable
    fun ClickableDemo(style: TextStyle) {
        val enableState by remember { mutableStateOf(true) }
        Box(modifier = Modifier
            .width(55.dp)
            .height(40.dp)
            .background(Color.White)
            .clickable(enabled = enableState) {
                showToast("Clickable 點擊")
            }
        ) {
            Text("Clickable", color = Color.Black, style = style)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CombinedClickDemo(style: TextStyle) {
        val enableState by remember { mutableStateOf(true) }
        Box(modifier = Modifier
            .width(55.dp)
            .height(40.dp)
            .background(Color.Green)
            .combinedClickable(
                enabled = enableState,
                onLongClick = {
                    showToast("LongClick 點擊")
                },
                onDoubleClick = {
                    showToast("DoubleClick 點擊")
                },
                onClick = {
                    showToast("Click 點擊")
                }
            )
        ) {
            Text("CombinedClick", color = Color.Black, style = style)
        }
    }

    @Composable
    fun DraggableDemo(colors: Colors, style: TextStyle) {
        var offsetX by remember { mutableStateOf(0f) }
        val boxSideLengthDp = 50.dp
        val boxSildeLengthPx = with(LocalDensity.current) { boxSideLengthDp.toPx() }
        val draggableState = rememberDraggableState { offsetX = (offsetX + it).coerceIn(0f, 3 * boxSildeLengthPx) }
        Box(
            Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(Color.Black)) {
            Box(
                Modifier
                    .size(boxSideLengthDp)
                    .offset {
                        IntOffset(offsetX.roundToInt(), 0)
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = draggableState
                    )
                    .background(colors.onSurface)
            ) {
                Text("Draggable", color = colors.onError, style = style)
            }
        }
    }

    enum class Status{ CLOSE, OPEN }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SwipeableDemo(colors: Colors, style: TextStyle) {
        val blockSize = 48.dp
        val blockSizePx = with(LocalDensity.current) { blockSize.toPx() }
        val swipeableState = rememberSwipeableState(initialValue = Status.CLOSE)
        var anchors = mapOf(0f to Status.CLOSE, blockSizePx to Status.OPEN)
        Box(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
//                .size(height = blockSize, width = blockSize * 2)
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(swipeableState.offset.value.toInt(), 0)
                    }
                    .swipeable(
                        state = swipeableState,
                        anchors = mapOf(
                            0f to Status.CLOSE,
                            blockSizePx to Status.OPEN
                        ),
                        thresholds = { from, to ->
                            if (from == Status.CLOSE) {
                                FractionalThreshold(0.3f)
                            } else {
                                FractionalThreshold(0.5f)
                            }
                        },
                        orientation = Orientation.Horizontal
                    )
                    .size(blockSize)
                    .background(colors.onBackground)
            ) {
                Text("Swipeable", color = colors.onError, style = style)
            }
        }
    }

    @Composable
    fun TransformerDemo(colors: Colors, style: TextStyle) {
        var offset by remember { mutableStateOf(Offset.Zero) }
        var ratationAngle by remember { mutableStateOf(0f) }
        var scale by remember { mutableStateOf(1f) }
        val transformableState = rememberTransformableState { zoomChange: Float, panChange: Offset, rotationChange: Float ->
            scale *= zoomChange
            offset += panChange
            ratationAngle += rotationChange
        }
        Box(
            Modifier
                .width(55.dp)
                .height(54.dp), contentAlignment = Alignment.Center) {
            Box(Modifier
                .width(55.dp)
                .height(54.dp)
                .rotate(ratationAngle) // 需要注意 offset 与 rotate 的调用先后顺序
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .scale(scale)
                .background(Color.Cyan)
                .transformable(
                    state = transformableState,
                    lockRotationOnZoomPan = false
                ),
                contentAlignment = Alignment.Center
            ) {
                Text("Transformer", color = colors.onError, style = style)
            }
        }
    }

    @Composable
    fun HorizontalListDemo(colors: Colors, style: TextStyle) {
        val scrollState = rememberScrollState()
        Surface(color = colors.error) {
            Row(modifier = Modifier
                .height(40.dp)
                .width(55.dp)
                .horizontalScroll(scrollState)) {
                repeat(10) {
                    Text("Horizontal $it ", color = colors.onError, style = style)
                }
            }
        }
    }

    @Composable
    fun HorizontalListTheme(colors: Colors, style: TextStyle) {
        BloomTheme{
            Surface(color = colors.secondaryVariant) {
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .width(55.dp)
                        .offset(x = with(LocalDensity.current) {
                            -scrollState.value.toDp()
                        })
                        .scrollable(scrollState, Orientation.Horizontal, reverseDirection = true)
                ) {
                    repeat(10) {
                        Text("Horizontal $it ", color = colors.onError, style = style)
                    }
                }
            }
        }
    }

    @Composable
    fun DragGestureDemo(colors: Colors, style: TextStyle) {
        var offset by remember { mutableStateOf(Offset.Zero) }
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(55.dp)
                .height(54.dp)
        ) {
            Box(Modifier
                .width(55.dp)
                .height(54.dp)
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .background(colors.error)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            println("onDragStart 開始")
                        },
                        onDragEnd = {
                            println("onDragEnd 結束")
                        },
                        onDragCancel = {
                            println("onDragCancel 取消")
                        },
                        onDrag = { change: PointerInputChange, dragAmount: Offset ->
                            println("onDrag 拖動中")
                            offset += dragAmount
                        }
                    )
                },
                contentAlignment = Alignment.Center
            ) {
                Text("DragGesture", color = colors.onError, style = style, textAlign = TextAlign.Center)
            }
        }
    }

//    @Composable
//    fun ForEachGestureDemo(colors: Colors, style: TextStyle) {
//        forEachGesture {
//            awaitPointerEventScope {
//                val event = awaitPointerEvent()
//                Text("x: ${event.changes[0].position.x}, y: ${event.changes[0].position.y}", color = colors.onError, style = style, textAlign = TextAlign.Center)
//            }
//        }
//    }

    @Composable
    fun NestedBoxDemo() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(85.dp)
                .background(Color.Red)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        awaitPointerEvent(PointerEventPass.Initial)
                    }
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(65.dp)
                    .background(Color.Blue)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            awaitPointerEvent(PointerEventPass.Final)
                        }
                    }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color.Green)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                awaitPointerEvent(PointerEventPass.Initial)
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(Color.Yellow)
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    awaitPointerEvent(PointerEventPass.Main)
                                }
                            }
                    )
                }
            }
        }
    }

    @Composable
    fun ConsumeDemo() {
        var event : PointerEvent
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(85.dp)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        event = awaitPointerEvent(PointerEventPass.Initial)
                        println("first layer, downChange: ${event.changes[0].consumed.downChange}")
                    }
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(65.dp)
                    .background(Color.Blue)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            event = awaitPointerEvent(PointerEventPass.Final)
                            println("second layer, downChange: ${event.changes[0].consumed.downChange}")
                        }
                    }
            ) {
                Box(
                    Modifier
                        .size(45.dp)
                        .background(Color.Green)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                event = awaitPointerEvent()
                                event.changes[0].consumeDownChange() // 消费手势事件
                                println("third layer, downChange: ${event.changes[0].consumed.downChange}")
                            }
                        }
                )
            }
        }
    }

    private suspend fun PointerInputScope.forEachGesture(block: suspend PointerInputScope.() -> Unit) {
        val currentContext = currentCoroutineContext()
        while (currentContext.isActive) {
            try {
                block()
                // 挂起等待所有手指抬起
                // awaitAllPointersUp()
            } catch (e: CancellationException) {
                if (currentContext.isActive) {
                    // 手势事件取消时，如果协程还存活则等待手指抬起再进行下一轮监听
                    // awaitAllPointersUp()
                    throw e
                }
            }
        }
    }

    @Composable
    fun BaseDragGestureDemo(colors: Colors, style: TextStyle) {
        val boxSize = 85.dp
        var offset by remember { mutableStateOf(Offset.Zero) }
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.size(boxSize)
        ) {
            Box(Modifier
                .size(boxSize)
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .background(colors.onError)
                .pointerInput(Unit) {
                    forEachGesture {
                        awaitPointerEventScope {
                            // 获取第一根手指的DOWN事件
                            val downEvent = awaitFirstDown()
                            // 根据手指标识符跟踪多痛手势
                            drag(downEvent.id) {
                                // 根据手势位置改变量更新偏移量状态
                                offset += it.positionChange()
                            }
                        }
                    }
                }
            ) {
                Text("BaseDragGesture", color = colors.onBackground, style = style, textAlign = TextAlign.Center)
            }
        }
    }

    @Composable
    fun AwaitDragOrCancellation(colors: Colors, style: TextStyle) {
        val boxSize = 85.dp
        var offset by remember { mutableStateOf(Offset.Zero) }
        Box(Modifier
            .size(boxSize)
            .offset {
                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
            }
            .background(colors.primarySurface)
            .pointerInput(Unit) {
                forEachGesture {
                    awaitPointerEventScope {
                        // 获取第一根手指的DOWN事件
                        val downPointer = awaitFirstDown()
                        while (true) {
                            // 根据手指标识符跟踪拖动手势，手指抬起货拖动事件被消费时返回null
                            val event = awaitDragOrCancellation(downPointer.id)
                                ?: // 拖动事件被取消
                                break
                            if (event.changedToUp()) {
                                // 所有手指均已抬起
                                break
                            }
                            // 根据手势位置改变量更新偏移量状态
                            offset += event.positionChange()
                        }
                    }
                }
            }
        ) {
            Text("AwaitDragOrCancellation", color = colors.onBackground, style = style, textAlign = TextAlign.Center)
        }
    }

    @Composable
    fun Contributors(colors: Colors, style: TextStyle) {
        Column{
            MemberItem(colors = colors, style = style, R.drawable.ic_dark_logo, text = "dark logo")
            MemberItem(colors = colors, style = style, R.drawable.ic_dark_welcome_bg, "dark bg")
            MemberItem(colors = colors, style = style, R.drawable.ic_dark_welcome_illos, text = "dark illos")
            MemberItem(colors = colors, style = style, R.drawable.ic_light_logo, "light logo")
            MemberItem(colors = colors, style = style, R.drawable.ic_light_welcome_bg, "light bg")
            MemberItem(colors = colors, style = style, R.drawable.ic_light_welcome_illos, "light illos")
        }
    }

    @Composable
    fun TouchFish(colors: Colors, style: TextStyle) {
        for(index in 0..10) MemberItem(colors = colors, style = style, androidx.benchmark.R.drawable.logo, text = "logo")
    }

    @Composable
    fun MemberItem(colors: Colors, style: TextStyle, imageID: Int, text:String){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Surface(
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Image(painter = painterResource(imageID), contentDescription = null)
            }
            Spacer(Modifier.padding(horizontal = 8.dp))
            Text(text = text, style = style, fontWeight = FontWeight.W500, color = colors.onError)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {  }
                ) {
                    Icon(Icons.Filled.Email, null, tint = colors.primarySurface)
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun ListWithHeader(colors: Colors, style: TextStyle) {
        val sections = listOf("item 1", "item 2", "item 3", "item 4", "item 5", "item 6", "item 7", "item 8", "item 9", "item 10")
        Column(modifier = Modifier.width(125.dp)) {
            TopAppBar(
                title = {
                    Text(
                        text = "List Header",
                        style = style
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                actions = {
                    IconButton(
                        onClick = {  }
                    ) {
                        Icon(Icons.Filled.Search, null)
                    }
                },
                backgroundColor = colors.onError
            )
            LazyColumn {
                sections.forEachIndexed{ index, section ->
                    stickyHeader {
                        Text(
                            text = section,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF2F4FB))
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            fontWeight = FontWeight.W700,
                            color = Color(0xFF00AAFF),
                            style = style
                        )
                    }
                    when(index){
                        0 -> item{Contributors(colors, style)}
                        1 -> item{TouchFish(colors, style)}
                    }
                }
            }
        }
    }

    @Composable
    fun LazyVerticalGridSample(list: List<MemberItemData>, colors: Colors, style: TextStyle) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.width(125.dp)
        ) {
            item(span = {
                // this上下文为LazyGridItemSpanScope
                GridItemSpan(this.maxLineSpan)
            }) {
                Text(text = "LazyVerticalGrid", textAlign = TextAlign.Center, fontSize = 14.sp)
            }
            list.forEach {
                item {
                    GridItem(colors = colors, imageID = it.imageID, text = it.text, style = style)
                }   
            }
        }
    }

    @Composable
    fun GridItem(colors: Colors, imageID: Int, text:String, style: TextStyle) {
        Surface(contentColor = colors.primaryVariant, modifier = Modifier
            .fillMaxSize()
            .clickable {

            }) {
            Box(contentAlignment = Alignment.Center) {
                Image(painter = painterResource(imageID), contentDescription = null)
                Text(text = text, style = style, fontWeight = FontWeight.W500, color = colors.onError)
            }
        }
    }

//    @Composable
//    fun PagingDemo(pager: Pager<Int, MemberItemData>, colors: Colors, style: TextStyle) {
//        val lazyPagingItems = pager.flow.collectAsLazyPagingItems()
//        LazyColumn(modifier = Modifier.width(125.dp)) {
//            items(lazyPagingItems.itemCount) {
//                Surface(contentColor = colors.secondaryVariant, modifier = Modifier
//                    .fillMaxSize()
//                    .clickable {
//
//                    }) {
//                    Box(contentAlignment = Alignment.Center) {
//                        Image(painter = painterResource(lazyPagingItems[it]!!.imageID), contentDescription = null)
//                        Text(text = lazyPagingItems[it]!!.text, style = style, fontWeight = FontWeight.W500, color = colors.onError)
//                    }
//                }
//            }
//        }
//    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun HorizontalPager(list: List<MemberItemData>, colors: Colors, style: TextStyle) {
        val state = rememberPagerState(initialPage = 0)
        Column {
            HorizontalPager(count = list.size, state = state, modifier = Modifier.height(56.dp)) { pager ->
                val item = list[pager]
                GridItem(colors, item.imageID, item.text, style)
            }
            Indicator(size = list.size, index = state.currentPage)
        }
    }

    @Composable
    private fun IndicatorHorizontal(isSelected: Boolean) {
        val width = animateDpAsState(
            targetValue = if (isSelected) 30.dp else 10.dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
        )
        Box(
            modifier = Modifier
                .height(10.dp)
                .width(width.value)
                .clip(CircleShape)
                .background(if (isSelected) Color.Black else Color.LightGray)
        )
    }

    @Composable
    private fun Indicator(size: Int, index: Int) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(size) {
                IndicatorHorizontal(it == index)
            }
        }
    }

    @Composable
    fun ListItemKey(colors: Colors, style: TextStyle) {
        val list = ArrayList<MemberItemIdData>()
        for(index in 0..20) {
            list.add(MemberItemIdData(index, androidx.benchmark.R.drawable.logo, text = "logo"))
        }
        LazyColumn(modifier = Modifier.width(125.dp)) {
            item {
                Text("List ItemKey", color = colors.onPrimary, modifier = Modifier.padding(4.dp), fontSize = 14.sp)
            }
            items(
                items = list,
                key = { memberItem -> // 返回 item 的一个稳定的且唯一的键
                    memberItem.id
                }
            ) { item ->
                MemberItem(colors = colors, style = style, imageID = item.imageID, text = item.text)
            }
        }
    }

    @Composable
    fun ActivityFour(context: Context, coroutineScope: CoroutineScope, colors: Colors, shapes: Shapes, typography: Typography, style: TextStyle, navigation: () -> Unit) {
        var selectedItem by remember{ mutableStateOf(0) }
        val toolbarHeight = 200.dp // 定义 ToolBar 的高度
        val maxUpPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() - 56.dp.roundToPx().toFloat() } // ToolBar 最大向上位移量 56.dp 参考自 androidx.compose.material AppBar.kt 里面定义的 private val AppBarHeight = 56.dp
        val minUpPx = 0f // ToolBar 最小向上位移量
        var toolbarOffsetHeightPx = remember { mutableStateOf(0f) } // 偏移折叠工具栏上移高度
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    val newOffset = toolbarOffsetHeightPx.value + delta
                    // 设置 ToolBar 的位移范围
                    toolbarOffsetHeightPx.value = newOffset.coerceIn(-maxUpPx, -minUpPx)
                    return Offset.Zero
                }
            }
        }
        val listState = rememberLazyListState()
        // 添加一个用于是否显示按钮的代码 如果第一个可见的项目已经被移动过去，就显示这个按钮。
        val showButton by remember {
            derivedStateOf { // 尽量减少不必要的合成
                listState.firstVisibleItemIndex > 0
            }
        }
        val listTitle = listOf("地方","郵件","電話", "人")
        val listIcon = listOf(Icons.Filled.Place, Icons.Filled.Email, Icons.Filled.Phone, Icons.Filled.Person)
        AnimatedVisibility(visible = showButton) {
            toolbarOffsetHeightPx = InitFab(coroutineScope = coroutineScope, listState = listState, toolbarOffsetHeightPx = toolbarOffsetHeightPx)
        }
        Scaffold(
            topBar = {
                ScrollableAppBar(
                    navigation = navigation,
                    title = "FourActivity offset is ${toolbarOffsetHeightPx.value}",
                    backgroundImageId = R.drawable.image_even_holiday,
                    scrollableAppBarHeight = toolbarHeight,
                    toolbarOffsetHeightPx = toolbarOffsetHeightPx,
                    coroutineScope = coroutineScope
                )
            },
            content = {
                // 添加一个用于是否显示按钮的代码 如果第一个可见的项目已经被移动过去，就显示这个按钮。
                Surface(color = colors.primarySurface) {
                    Box {
                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(it)
                                .nestedScroll(nestedScrollConnection) // 作为父级附加到嵌套滚动系统
                        ) { // 列表带有内置的嵌套滚动支持，它将通知我们它的滚动
                            LazyColumn(state = listState) {
                                items(100) { index ->
                                    Text("I'm item $index", modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp, 12.dp)
                                        .clickable {

                                        },
                                        color = colors.onError
                                    )
                                    Divider(color = colors.onBackground)
                                }
                            }
                            LaunchedEffect(listState) {
                                snapshotFlow { listState.firstVisibleItemIndex }
                                    .map { index -> index > 0 }
                                    .distinctUntilChanged()
                                    .filter { it == true }
                                    .collect {
                                        Toast.makeText(context, "SnapShotFlow", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                }
            },
            floatingActionButton = { toolbarOffsetHeightPx = InitFab(coroutineScope = coroutineScope, listState = listState, toolbarOffsetHeightPx = toolbarOffsetHeightPx) },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                BottomNavigation {
                    listTitle.forEachIndexed { index, item ->
                        val setColor = if(selectedItem == index) {
                            colors.onError
                        } else {
                            colors.error
                        }
                        BottomNavigationItem(
                            icon = { Icon(listIcon[index], contentDescription = getString(CONTENT_DESCRIPTION), tint = setColor ) },
                            label = { Text(item, color = setColor) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            },
        )
    }

    @Composable
    fun InitFab(coroutineScope: CoroutineScope, listState: LazyListState, toolbarOffsetHeightPx: MutableState<Float>): MutableState<Float> {
        ExtendedFloatingActionButton(
//            modifier = Modifier.offset(280.dp, 405.dp),
            text = { Text("FAB") },
            onClick = {
                coroutineScope.launch(Dispatchers.Main) {
                    listState.animateScrollToItem(0)
                    toolbarOffsetHeightPx.value = 0.0f
                }
            }
        )
        return toolbarOffsetHeightPx
    }

    @Composable
    fun CompositionLocalExample() {
        MaterialTheme { // MaterialTheme 將 ContentAlpha.high 設置為默認值
            Column {
                Text("使用 MaterialTheme 提供的 alpha", fontSize = 16.sp)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row {
                        Text("為LocalContentAlpha 提供的中值", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("This Text也使用中值", fontSize = 14.sp)
                    }
                    Row {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                            DescendantExample()
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        QuantityString(1)
                    }
                }
            }
        }
    }

    @Composable
    fun DescendantExample() {
        // CompositionLocalProviders 也適用於可組合函數
        Text("此文本現在使用禁用的 alpha", fontSize = 13.sp)
    }

    @Composable
    fun QuantityString(count: Int) {
        // 從 LocalContext 的當前值獲取 `resources`
        val text = remember(resources, count) {
            resources.getQuantityString(R.plurals.numberOfSongsAvailable, count, count)
        }
        Text(text = text)
    }

    @Composable
    fun ActivityFive(context: Context, coroutineScope: CoroutineScope, colors: Colors, style: TextStyle, shapes: Shapes, typography: Typography, navigation: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    navigation()
                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.IO) {

                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.Home,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                    }, title = {
                        Text(
                            text = "ActivityFive",
                            color = colors.onError,
                        )
                    }, actions = {
                        IconButtonDemo(
                            content = {
                                IconButton(onClick = {
                                }) {
                                    Icon(Icons.Filled.Info, getString(CONTENT_DESCRIPTION), tint = Color.White)
                                }
                            },
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                }
                            })
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(tint = colors.onError, imageVector = Icons.Default.MoreVert, contentDescription = getString(CONTENT_DESCRIPTION))
                        Text(
                            text = "更多",
                            color = colors.onError,
                            modifier = Modifier.clickable {
                                coroutineScope.launch(Dispatchers.IO) {
                                }
                            }
                        )
                    })
            },
            bottomBar = {
                BottomNavigationCustom(colors = colors)
            },
        ) {
            val option = Options()
            Row {
                Box(modifier = Modifier
                    .width(180.dp)
                    .height(350.dp)) {
                    InkColorCanvasRule(option)
                }
                Box(modifier = Modifier
                    .width(180.dp)
                    .height(350.dp)) {
                    InkColorCanvasIrregular(option)
                }
            }
            Column(modifier = Modifier
                .padding(it)
                .padding(8.dp, 12.dp)) {
                Row {
                    val size = 50.dp
                    DrawColorRing(size)
                    Spacer(modifier = Modifier.width(8.dp))
                    DrawContent(size)
                    Spacer(modifier = Modifier.width(8.dp))
                    DrawBehind(size)
                    Spacer(modifier = Modifier.width(8.dp))
                    DrawBorder(size)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }

    @Composable
    fun DrawColorRing(size: Dp) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            val radius = 50.dp
            val ringWidth = 5.dp
            Canvas(modifier = Modifier.size(radius)) {
                this.drawCircle( // 画圆
                    brush = Brush.sweepGradient(listOf(Color.Red, Color.Green, Color.Red), Offset(radius.toPx() / 2f, radius.toPx() / 2f)),
                    radius = radius.toPx() / 2f,
                    style = Stroke(
                        width = ringWidth.toPx()
                    )
                )
            }
        }
    }

    @Composable
    fun DrawContent(size: Dp) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp)
                ,modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawCircle(
                            Color(0xffe7614e),
                            18.dp.toPx() / 2,
                            center = Offset(drawContext.size.width, 0f)
                        )
                    }
            ) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Diana")
            }
        }
    }

    @Composable
    fun DrawBehind(size: Dp) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp)
                ,modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            Color(0xffe7614e),
                            18.dp.toPx() / 2,
                            center = Offset(drawContext.size.width, 0f)
                        )
                    }
            ) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Diana")
            }
        }
    }

    @Composable
    fun DrawBorder(size: Dp) {
        Box(
            modifier = Modifier.size(size),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var borderColor by remember { mutableStateOf(Color.Red) }
                Card(
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .size(size = size)
                        .drawWithCache {
                            Log.d("compose_study", "此处不会发生 Recompose")
                            val path = Path().apply {
                                moveTo(0f, 0f)
                                relativeLineTo(50.dp.toPx(), 0f)
                                relativeLineTo(0f, 50.dp.toPx())
                                relativeLineTo(-50.dp.toPx(), 0f)
                                relativeLineTo(0f, -50.dp.toPx())
                            }
                            onDrawWithContent {
                                Log.d("compose_study", "此处会发生 Recompose")
                                drawContent()
                                drawPath(
                                    path = path,
                                    color = borderColor,
                                    style = Stroke(
                                        width = 10f,
                                    )
                                )
                            }
                        }
                ) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Diana", modifier = Modifier.clickable {
                        borderColor = when(borderColor) {
                            Color.Red -> {
                                Color.Yellow
                            }
                            else -> {
                                Color.Red
                            }
                        }
                    })
                }
            }
        }
    }

    @Composable
    fun InkColorCanvas(option: Options) {
        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical, option)
        val imageBitmapDefault = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical_finish, option)
        val animal = remember { Animatable(0.0f) }
        var xbLength = 0.0f
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    coroutineScope {
                        while (true) {
                            val offset = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            launch {
                                animal.animateTo(
                                    xbLength,
                                    animationSpec = spring(stiffness = Spring.DampingRatioLowBouncy)
                                )
                            }

                        }
                    }
                }
        ) {
            drawIntoCanvas { canva ->
                val multiColorBitmpa = Bitmap.createScaledBitmap(
                    imageBitmap,
                    size.width.toInt(),
                    size.height.toInt(), false
                )
                val blackColorBitmpa = Bitmap.createScaledBitmap(
                    imageBitmapDefault,
                    size.width.toInt(),
                    size.height.toInt(),
                    false
                )
                val paint = Paint().asFrameworkPaint()
                canva.nativeCanvas.drawBitmap(multiColorBitmpa, 0f, 0f, paint) //绘制图片
                //保存图层
                val layerId: Int = canva.nativeCanvas.saveLayer(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    paint,
                )
                canva.nativeCanvas.drawBitmap(blackColorBitmpa, 0f, 0f, paint)
                //PorterDuffXfermode 设置画笔的图形混合模式
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                //画圆
                canva.nativeCanvas.drawCircle(
                    size.width / 2,
                    size.height / 2,
                    animal.value,
                    paint
                )
                //画布斜边
                xbLength = kotlin.math.sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2)).toFloat()
                paint.xfermode = null
                canva.nativeCanvas.restoreToCount(layerId)
            }
        }
    }

    @Composable
    fun InkColorCanvasRule(option: Options) {
        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical, option)
        val imageBitmapDefault = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical_finish, option)
        val screenOffset = remember { mutableStateOf(Offset(0f, 0f)) }
        val animalState = remember { mutableStateOf(false) }
        val animal: Float by animateFloatAsState(
            if (animalState.value) {
                1f
            } else {
                0f
            }, animationSpec = TweenSpec(durationMillis = 4000)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    coroutineScope {
                        while (true) {
                            val position = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            launch {
                                screenOffset.value = Offset(position.x, position.y)
                                animalState.value = !animalState.value
                            }

                        }
                    }
                }
        ) {
            drawIntoCanvas { canvas ->
                val multiColorBitmap = Bitmap.createScaledBitmap(
                    imageBitmap,
                    size.width.toInt(),
                    size.height.toInt(),
                    false
                )
                val blackColorBitmap = Bitmap.createScaledBitmap(
                    imageBitmapDefault,
                    size.width.toInt(),
                    size.height.toInt(),
                    false
                )
                val paint = Paint().asFrameworkPaint()
                canvas.nativeCanvas.drawBitmap(multiColorBitmap, 0f, 0f, paint) //绘制图片
                //保存图层
                val layerId: Int = canvas.nativeCanvas.saveLayer(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    paint,
                )
                //当前图层也是顶层图层绘制黑白Btmap
                canvas.nativeCanvas.drawBitmap(blackColorBitmap, 0f, 0f, paint)
                //PorterDuffXfermode 设置画笔的图形混合模式
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                val xbLength = kotlin.math.sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2)).toFloat()*animal
                //画圆
                canvas.nativeCanvas.drawCircle(
                    screenOffset.value.x,
                    screenOffset.value.y,
                    xbLength,
                    paint
                )
                //画布斜边
                paint.xfermode = null
                canvas.nativeCanvas.restoreToCount(layerId)
            }
        }
    }

    @Composable
    fun InkColorCanvasIrregular(option: Options) {
        val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical, option)
        val imageBitmapDefault = BitmapFactory.decodeResource(resources, R.drawable.giant_bg_vertical_finish, option)
        val scrrenOffset = remember { mutableStateOf(Offset(0f, 0f)) }
        val animalState = remember { mutableStateOf(false) }
        val animal: Float by animateFloatAsState(
            if (animalState.value) {
                1f
            } else {
                0f
            }, animationSpec = TweenSpec(durationMillis = 6000)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    coroutineScope {
                        while (true) {
                            val position = awaitPointerEventScope {
                                awaitFirstDown().position
                            }
                            launch {
                                scrrenOffset.value = Offset(position.x, position.y)
                                animalState.value = !animalState.value
                            }

                        }
                    }
                }
        ) {
            drawIntoCanvas { canva ->
                val multiColorBitmpa = Bitmap.createScaledBitmap(
                    imageBitmap,
                    size.width.toInt(),
                    size.height.toInt(), false
                )
                val blackColorBitmpa = Bitmap.createScaledBitmap(
                    imageBitmapDefault,
                    size.width.toInt(),
                    size.height.toInt(),
                    false
                )
                val paint = Paint().asFrameworkPaint()
                canva.nativeCanvas.drawBitmap(multiColorBitmpa, 0f, 0f, paint) //绘制图片
                //保存图层
                val layerId: Int = canva.nativeCanvas.saveLayer(
                    0f,
                    0f,
                    size.width,
                    size.height,
                    paint,
                )
                canva.nativeCanvas.drawBitmap(blackColorBitmpa, 0f, 0f, paint)
                //PorterDuffXfermode 设置画笔的图形混合模式
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                val xbLength = kotlin.math.sqrt(size.width.toDouble().pow(2.0) + size.height.toDouble().pow(2)).toFloat() * animal
                //画圆
//            canva.nativeCanvas.drawCircle(
//                scrrenOffset.value.x,
//                scrrenOffset.value.y,
//                xbLength,
//                paint
//            )
                val path = Path().asAndroidPath()
                path.moveTo(scrrenOffset.value.x, scrrenOffset.value.y)
                //随便绘制了哥区域。当然了为了好看曲线可以更美。
                if (xbLength>0) {
                    path.addOval(
                        RectF(
                            scrrenOffset.value.x - xbLength,
                            scrrenOffset.value.y - xbLength,
                            scrrenOffset.value.x + 100f + xbLength,
                            scrrenOffset.value.y + 130f + xbLength
                        ), android.graphics.Path.Direction.CCW
                    )
                    path.addCircle(
                        scrrenOffset.value.x, scrrenOffset.value.y, 100f + xbLength,
                        android.graphics.Path.Direction.CCW
                    )
                    path.addCircle(
                        scrrenOffset.value.x-100, scrrenOffset.value.y-100, 50f + xbLength,
                        android.graphics.Path.Direction.CCW
                    )
                }
                path.close()
                canva.nativeCanvas.drawPath(path, paint)
                //画布斜边
                paint.xfermode = null
                canva.nativeCanvas.restoreToCount(layerId)
            }
        }
    }

    @OptIn(InternalComposeApi::class)
    @Composable
    fun BottomNavigationCustom(colors: Colors) {
        val viewModel = AnimationViewModel()
        val listStateColor by remember { mutableStateOf(arrayListOf(colors.error, colors.onError, colors.onError, colors.onError)) }
        val listTitle = listOf("主頁","資訊","喜歡", "設置")
        val listIcon = listOf(Icons.Filled.Home, Icons.Filled.Info, Icons.Filled.Favorite, Icons.Filled.Settings)
        val applyContext = currentComposer.applyCoroutineContext
        val clickTrue = remember { mutableStateOf(false) }
        val mCurAnimValueColor = remember { Animatable(1f) }
        val animalBooleanState: Float by animateFloatAsState(
            if (viewModel.animalBoolean.value) {
                0f
            } else {
                1f
            }, animationSpec = TweenSpec(durationMillis = 600),
            finishedListener = {
                if (it >= 0.9f && clickTrue.value){
                    viewModel.animalBoolean.value = !viewModel.animalBoolean.value
                }
            }
        )
        val stiffness = 100f
        val animalScaleCanvasWidthValue: Float by animateFloatAsState(
            if (!clickTrue.value) {
                0f
            } else {
                30f
            },
            animationSpec = SpringSpec(stiffness = stiffness),
        )
        val animalScaleCanvasHeightValue: Float by animateFloatAsState(
            if (!clickTrue.value) {
                0f
            } else {
                30f
            },
            animationSpec = SpringSpec(stiffness = stiffness),
        )
        val mCurAnimalHeight: Float by animateFloatAsState(
            if (!clickTrue.value) {
                -30f
            } else {
                30f
            },
            animationSpec = SpringSpec(stiffness = stiffness),
        )
        val mCurAnimValueY: Float by animateFloatAsState(
            if (!clickTrue.value) {
                0f
            } else {
                1f
            }, animationSpec = SpringSpec(stiffness = stiffness),
            finishedListener = {
                if (it >= 0.9f && clickTrue.value) {
                    CoroutineScope(applyContext).launch {
                        mCurAnimValueColor.animateTo(
                            0f,
                            animationSpec = SpringSpec(stiffness = stiffness)
                        )
                    }
                }
                if (it <= 0.01f && !clickTrue.value) {
                    CoroutineScope(applyContext).launch {
                        mCurAnimValueColor.animateTo(
                            1f,
                            animationSpec = SpringSpec(stiffness = stiffness)
                        )
                    }
                }
                //动画结束->回归原来位置
                if (it > 0.9f && clickTrue.value) {
                    clickTrue.value = !clickTrue.value
                }
            }
            //TweenSpec(durationMillis = 1600)
            // DurationBasedAnimationSpec, FloatSpringSpec, FloatTweenSpec, KeyframesSpec, RepeatableSpec, SnapSpec, SpringSpec, TweenSpec
        )
        //半径的决定动画
        val mCurAnimValue: Float by animateFloatAsState(
            if (clickTrue.value) {
                0f
            } else {
                1f
            }, animationSpec = SpringSpec(dampingRatio = 1f, stiffness = 30f)
        )
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)) {
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    drawIntoCanvas { canvas ->
                        //绘制底部曲线到底部
                        canvas.translate(0f, size.height)
                        canvas.scale(1f, -1f)
                        val paint = Paint()
                        paint.strokeWidth = 2f
                        paint.style = PaintingStyle.Fill
                        paint.color = Color(245, 215, 254, 255)

                        val height = 276f
                        val cicleHeight = height / 3
                        val ScaleHeight = animalScaleCanvasHeightValue
                        val ScaleWidth = animalScaleCanvasWidthValue
                        //控制脖子左边,一直在变化
                        val path = Path()
                        path.moveTo(0f + ScaleWidth, 0f)
                        path.lineTo(0f + ScaleWidth, height - cicleHeight + ScaleHeight)
                        path.quadraticBezierTo(
                            0f + ScaleWidth,
                            height + ScaleHeight,
                            cicleHeight,
                            height + ScaleHeight
                        )

                        //第一个左弧度
                        path.lineTo(size.width - cicleHeight - ScaleWidth, height + ScaleHeight)
                        path.quadraticBezierTo(
                            size.width - ScaleWidth,
                            height + ScaleHeight,
                            size.width - ScaleWidth,
                            height - cicleHeight + ScaleHeight
                        )
                        path.lineTo(size.width - ScaleWidth, 0f)
                        path.close()
                        canvas.drawPath(path, paint)
//--------------------------------------------------------------------------
                        canvas.save()
                        //中间凸起部分
                        val centerHdX = size.width / 3 / 2 + size.width / 3 * viewModel.position.value!!
                        //这里坐标系位置圆点就为上边线中点
                        canvas.translate(centerHdX, height)
                        val R = 30f
                        //0-50是变大部分
                        val RH = mCurAnimalHeight
                        //50到-50是变为平
                        val p0 = Offset(-R, 0f + RH + animalScaleCanvasHeightValue)
                        val p1 = Offset(-R, R + RH + animalScaleCanvasHeightValue)
                        val p3 = Offset(0f, 2 * R - 30f + RH + animalScaleCanvasHeightValue)
                        val p5 = Offset(R, R + RH + animalScaleCanvasHeightValue)
                        val p6 = Offset(R, 0f + RH + animalScaleCanvasHeightValue)
                        val p7 = Offset(100f, -10f + animalScaleCanvasHeightValue)

                        val pathCub = Path()
                        pathCub.moveTo(-100f, 0f + animalScaleCanvasHeightValue)
                        pathCub.cubicTo(p0.x, p0.y, p1.x, p1.y, p3.x, p3.y)
                        pathCub.cubicTo(p5.x, p5.y, p6.x, p6.y, p7.x, p7.y)

                        canvas.drawPath(pathCub, paint)

                        //中间凸起部分落下
                        canvas.restore()

//--------------------------------------------------------------------------

                        //绘制弹性圆球
                        //假设点击的是index=0一共三个底部按钮
                        canvas.save()
                        //1,2,3
                        //将坐标系移动到点击部位()这样写起来比较爽好理解。将点击部位作为我们的坐标系园点
                        val centerX = size.width / 3 / 2 + size.width / 3 * viewModel.position.value!!
                        Log.e("圆点", "LoginPage: $centerX")
                        canvas.translate(centerX, height * 2 / 3.2f)
                        //canvas.drawCircle(Offset(0f, 0f), 100f, paint)
                        //这里我们清楚坐标圆点之后我们进行绘制我们的圆
                        val r = 100f - 50 * (1 - mCurAnimValue)
                        //圆的坐标和中心点的坐标计算
                        //1.首先 原点为(0f,0f)且半径r=100f--->那么p6(0f,r),p5=(r/2,r),p4(r,r/2),p3(r,0f)
                        //2.第二象限里面 p2(r,-r/2),p1(r/2,-r),p0(0f,r)
                        //3.第三象限里面 p11(-r/2,-r),p10(-r,-r/2),p9(-r,0f)
                        //4.第四象限里面 p8(-r,r/2),p7(r/2,r),p6(0f,r)
                        Log.e("mCurAnimValueY", "LoginPage=: $mCurAnimValueY")
                        val moveTopHeight = mCurAnimValueY * 250f
                        val P0 = Offset(0f, -r + moveTopHeight + animalScaleCanvasHeightValue)
                        val P1 = Offset(r / 2, -r + moveTopHeight + animalScaleCanvasHeightValue)
                        val P2 = Offset(r, -r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                        val P3 = Offset(r, 0f + moveTopHeight + animalScaleCanvasHeightValue)
                        val P4 = Offset(r, r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                        val P5 = Offset(r / 2, r + moveTopHeight + animalScaleCanvasHeightValue)
                        val P6 = Offset(0f, r + moveTopHeight + animalScaleCanvasHeightValue)
                        val P7 = Offset(-r / 2, r + moveTopHeight + animalScaleCanvasHeightValue)
                        val P8 = Offset(-r, r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                        val P9 = Offset(-r, 0f + moveTopHeight + animalScaleCanvasHeightValue)
                        val P10 = Offset(-r, -r / 2 + moveTopHeight + animalScaleCanvasHeightValue)
                        val P11 = Offset(-r / 2, -r + moveTopHeight + animalScaleCanvasHeightValue)

                        val heightController = 180f
                        val pathReult = Path()
                        pathReult.moveTo(P0.x, P0.y - heightController * mCurAnimValue)
                        //p1->p2->p3
                        pathReult.cubicTo(
                            P1.x,
                            P1.y - 30 * mCurAnimValue,
                            P2.x,
                            P2.y - 30 * mCurAnimValue,
                            P3.x,
                            P3.y
                        )
                        //p4->p5->p6
                        pathReult.cubicTo(P4.x, P4.y, P5.x, P5.y, P6.x, P6.y)
                        //p7->p8->p9
                        pathReult.cubicTo(P7.x, P7.y, P8.x, P8.y, P9.x, P9.y)
                        //p10->p11->p0
                        pathReult.cubicTo(
                            P10.x,
                            P10.y - 30 * mCurAnimValue,
                            P11.x,
                            P11.y - 30 * mCurAnimValue,
                            P0.x,
                            P0.y - heightController * mCurAnimValue
                        )
                        pathReult.close()
                        paint.color = Color(245, 215, 254, mCurAnimValueColor.value.toInt() * 255)
                        //canvas.drawPath(pathReult, paint)
                }
            }
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom) {
                items(listTitle.size) {index ->
                    Column( // Material 庫中的圖標，有 Filled, Outlined, Rounded, Sharp, Two Tone 等
//                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .setModifiers(viewModel.position.value, index, animalBooleanState)
                            .clickable {
                                viewModel.positionChanged(index)
                                clickTrue.value = !clickTrue.value
                                viewModel.animalBoolean.value = !viewModel.animalBoolean.value
                                for (i in 0 until listStateColor.size) {
                                    if (i == viewModel.position.value) {
                                        listStateColor[i] = colors.error
                                    } else {
                                        listStateColor[i] = colors.onError
                                    }
                                }
                            }) {
                        Icon(
                            listIcon[index],
                            tint = listStateColor[index],
                            contentDescription = getString(CONTENT_DESCRIPTION),
                            modifier = Modifier
                                .size(36.dp)
                                .setModifiers(viewModel.position.value, index, animalBooleanState)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(listTitle[index], color = listStateColor[index])
                    }
                }
            }
        }
    }

//    private lateinit var getCanvas: Canvas
//    @Composable
//    fun CanvasCircle() {
//        //距离左边屏幕距离
//        val marginToLeft = 1000f
//        //距离屏幕下边距离
//        val marginToBottom = 2000f
//        Canvas(modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()) {
//            drawIntoCanvas { canvas ->
//                val paint = Paint()
//                paint.style = PaintingStyle.Fill
//                paint.color = Color.Green
//                canvas.translate(0f, size.height)
//                canvas.scale(0.5f, 0.5f)
//                canvas.translate(marginToLeft, marginToBottom)
//                canvas.drawCircle(Offset(100f, 100f), 100f, paint)
//                getCanvas = canvas
//            }
//            this.drawXLine(2000.0f, 0.0f, 2000.0f, getCanvas)
//        }
//    }
//
//    private fun DrawScope.drawXLine(
//        x_scaleWidth: Float,
//        marginToLeft: Float,
//        grid_width: Float,
//        canvas: Canvas
//    ) {
//        var xScaleWidth1 = x_scaleWidth
//        var gridWidth1 = grid_width
//        val linePaint = Paint()
//        linePaint.strokeWidth = 2f
//        linePaint.style = PaintingStyle.Stroke
//        linePaint.color = Color(188, 188, 188, 100)
//        //x轴距离右边也留80距离
//        xScaleWidth1 = (size.width - marginToLeft - 80f)
//        gridWidth1 = xScaleWidth1 / 6
//        val onePath = Path()
//        onePath.lineTo(xScaleWidth1, 0f)
//        canvas.drawPath(onePath, linePaint)
//        canvas.save()
//        //通过平移画布绘制剩余的平行x轴线
//        (0 until 3).forEach { index ->
//            canvas.translate(0f, gridWidth1 - 40f)
//            canvas.drawPath(onePath, linePaint)
//        }
//        canvas.restore()
//    }


    @Composable
    fun ActivitySix(context: Context, coroutineScope: CoroutineScope, colors: Colors, style: TextStyle, shapes: Shapes, typography: Typography, navigation: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    navigation()
                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch(Dispatchers.IO) {

                                }
                            }) {
                            Icon(tint = colors.onError,
                                imageVector = Icons.Default.Home,
                                contentDescription = getString(CONTENT_DESCRIPTION)
                            )
                        }
                    }, title = {
                        Text(
                            text = "ActivitySix",
                            color = colors.onError,
                        )
                    }, actions = {
                        IconButtonDemo(
                            content = {
                                IconButton(onClick = {

                                }) {
                                    Icon(Icons.Filled.Info, getString(CONTENT_DESCRIPTION), tint = Color.White)
                                }
                            },
                            onClick = {
                                coroutineScope.launch(Dispatchers.Main) {

                                }
                            })
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(tint = colors.onError, imageVector = Icons.Default.MoreVert, contentDescription = getString(CONTENT_DESCRIPTION))
                        Text(
                            text = "更多",
                            color = colors.onError,
                            modifier = Modifier.clickable {
                                coroutineScope.launch(Dispatchers.IO) {

                                }
                            }
                        )
                    })
            },
            bottomBar = {

            },
        ) {
            Surface(modifier = Modifier.padding(it)) {
                DataSaverDemo()
            }
        }
    }

    @ExperimentalSerializationApi
    @Composable
    fun DataSaverDemo() { // or LocalDataSaver provides dataSaverMMKV LocalDataSaver provides dataSaverDataStorePreferences your Class instance
        val emptyBean = DataSaverBean(233, "FunnySaltyFish")
        // 获取 DataSaverInterface | 您可以使用此变量做手动保存
        val dataSaverInterface = LocalDataSaver.current
        // 你可以设置 [savePolicy]为其他类型(参见 [SavePolicy] )，以防止某些情况下过于频繁地保存
        // 如果你设置为 SavePolicy.NEVER，则写入本地的操作需要自己做
        // 例如: onClick = { dataSaverState.save() }
        var stringExample by rememberDataSaverState(KEY_STRING_EXAMPLE, "FunnySaltyFish, tap to input", savePolicy = SavePolicy.IMMEDIATELY, async = true)
        var booleanExample by rememberDataSaverState(KEY_BOOLEAN_EXAMPLE, false)
        var beanExample by rememberDataSaverState(KEY_BEAN_EXAMPLE, default = emptyBean)
        var themeType: ThemeType by rememberDataSaverState(key = "key_theme_type", default = ThemeType.DynamicNative)
        var listExample by rememberDataSaverListState(key = "key_list_example", default = listOf(emptyBean.copy(label = "Name 1"), emptyBean.copy(label = "Name 2"), emptyBean.copy(label = "Name 3")))
        // Among our basic implementations, only MMKV supports `Parcelable` by default
        var parcelableExample by rememberDataSaverState(key = "parcelable_example", default = DataSaverParcelable("FunnySaltyFish", 20))
        Column(Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            Heading(text = "Auto Save Examples:")
            Text(text = "This is an example of saving String") // 保存字符串的示例
            OutlinedTextField(value = stringExample, onValueChange = {
                stringExample = it
            })

            Text(text = "This is an example of saving Boolean") // 保存布尔值的示例
            Switch(checked = booleanExample, onCheckedChange = {
                booleanExample = it
            })

            Text(text = "This is an example of saving Parcelable") // 保存布尔值的示例
            Text(parcelableExample.toString())
            Button(onClick = {
                parcelableExample = parcelableExample.copy(age = parcelableExample.age + 1)
            }) {
                Text(text = "Add age by 1")
            }

            Text(text = "This is an example of saving custom Data Bean") // 保存自定义类型的示例
            Text(text = beanExample.toString())
            Button(onClick = {
                beanExample = beanExample.copy(id = beanExample.id + 1)
            }) {
                Text(text = "Add bean's id") // id自加
            }

            Text(text = "This is an example of saving custom Sealed Class") // 保存自定义类型的示例
            Column(
                Modifier
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                    .padding(8.dp)) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "主題/Theme",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(12.dp))
                RadioTile(text = "默認", selected = themeType == ThemeType.StaticDefault) {
                    themeType = ThemeType.StaticDefault
                }
                RadioTile(text = "動態取色", selected = themeType == ThemeType.DynamicNative) {
                    themeType = ThemeType.DynamicNative
                }
            }
            val nullableCustomBeanState: DataSaverMutableState<DataSaverBean?> = rememberDataSaverState(key = "nullable_bean", initialValue = null)
            Text(text = "This is an example of saving custom Data Bean(nullable)") // 保存自定义类型的示例
            Text(text = nullableCustomBeanState.value.toString())
            Row(Modifier.fillMaxWidth()) {
                Button(onClick = {
                    nullableCustomBeanState.value = DataSaverBean(id = 100, label = "I'm not null")
                }) {
                    Text(text = "Set As Not Null")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = {
                    nullableCustomBeanState.value = null
                    // nullableCustomBeanState.remove(replacement = EmptyBean)
                }) {
                    Text(text = "Set As Null")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Heading(text = "Save-When-Disposed Examples:")
            SaveWhenDisposedExample()
            Spacer(modifier = Modifier.height(16.dp))
            Heading(text = "List Example")
            LazyColumn(Modifier.heightIn(0.dp, 400.dp)) {
                items(listExample) { item ->
                    Text(modifier = Modifier.padding(8.dp), text = item.toString(), fontSize = 16.sp)
                }
                item {
                    Row {
                        Button(onClick = {
                            listExample =
                                listExample + emptyBean.copy(label = "Name ${listExample.size + 1}")
                        }) {
                            Text(text = "Add To List")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(onClick = {
                            if (listExample.isNotEmpty()) listExample = listExample.dropLast(1)
                        }) {
                            Text(text = "Remove From List")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SaveWhenDisposedExample() {
        var showDialog by remember {
            mutableStateOf(false)
        }
        if (showDialog) AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Sample") },
            text = {
                var stringExample2 by rememberDataSaverState(
                    key = KEY_STRING_EXAMPLE_2,
                    initialValue = "this one will be saved only when disposed",
                    savePolicy = SavePolicy.DISPOSED,
                    async = false
                )
                OutlinedTextField(value = stringExample2, onValueChange = {
                    stringExample2 = it
                })
            },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text(text = "Close") } },
        )
        Button(onClick = { showDialog = true }) {
            Text(text = "Click Me To Open Dialog")
        }
    }

    @Composable
    fun Heading(text: String) {
        Text(text, fontWeight = FontWeight.W600, fontSize = 18.sp)
    }

    @Composable
    fun RadioTile(
        text: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.W700)
            RadioButton(selected = selected, onClick = onClick)
        }
    }

    @Composable
    fun LazyListState.isScrollingUp(): Boolean {
        var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
        var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
        return remember(this) {
            derivedStateOf {
                if (previousIndex != firstVisibleItemIndex) {
                    previousIndex > firstVisibleItemIndex
                } else {
                    previousScrollOffset >= firstVisibleItemScrollOffset
                }.also {
                    previousIndex = firstVisibleItemIndex
                    previousScrollOffset = firstVisibleItemScrollOffset
                }
            }
        }.value
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
            delay(2000) // 如果得到一個隨機數為零，則添加 Random 以返回 null。 得到null的可能性是1/4
            return if (Random.nextInt(until = 4) > 0) {
                val images = listOf(
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_launcher_background,
                    R.drawable.ic_launcher_background,
                )
                ImageRes(images[Random.nextInt(images.size)]) // 每次調用 load 函數時加載一個隨機 id
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
        PageJumpSamples(msg = Message("Colleague", "Hey, take a look at Jetpack Compose, it's great!"))
    }

}

class ExampleDelegate {
    var delegatedProperty: String by Delegate()
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "Property: ${property.name}"
//        return "Ref: $thisRef Property: ${property.name}"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("Value: $value Property: ${property.name} Ref: $thisRef.")
    }
}

// 作者 FunnySaltyFish (http://funnysaltyfish.fun)
class CountNumParentData(var countNum: Int) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@CountNumParentData
}

fun Modifier.count(context: Context, num: Int) = this
    .drawWithContent {
        drawIntoCanvas { canvas ->
            val paint = android.graphics
                .Paint()
                .apply {
                    color = context.resources.getColor(R.color.white)
                    textSize = 40F
                }
            canvas.nativeCanvas.drawText(num.toString(), 0F, 40F, paint)
        } // 绘制 Box 自身内容
        drawContent()
    }
    .then( // 这部分是 父级数据修饰符
        CountNumParentData(num)
    )

fun Modifier.setModifiers(
    animalCenterIndex: Int?,
    i: Int,
    animalBooleanState: Float
): Modifier {
    Log.e("currentValue=", "modifiers: "+animalCenterIndex.toString()+"=="+i )
    return if (animalCenterIndex == i) {
        this
            .padding(bottom = 15.dp + (animalBooleanState * 100).dp)
//          .width(25.dp)
//          .height(25.dp)
    } else {
        this
            .padding(bottom = 15.dp - (animalBooleanState * 10).dp)
//          .width(25.dp)
//          .height(25.dp)
    }
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