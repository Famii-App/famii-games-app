package com.example.cardsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.cardsapp.data.QuestionsRepository
import com.example.cardsapp.ui.theme.CardsAppTheme
import kotlin.math.abs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cardsapp.navigation.BottomNavItem
import com.example.cardsapp.navigation.Screen
import com.example.cardsapp.screens.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CardsAppTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = false
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    var previousSelectedItem by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                BottomNavItem.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (item.icon) {
                                    "home" -> Icons.Default.Home
                                    "cards" -> Icons.Default.CreditCard
                                    "person" -> Icons.Default.Person
                                    "settings" -> Icons.Default.Settings
                                    else -> Icons.Default.Home
                                },
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = {
                            previousSelectedItem = selectedItem
                            selectedItem = index
                            navController.navigate(item.route) {
                                if (item.route == Screen.Home.route) {
                                    popUpTo(0) { inclusive = true }
                                } else {
                                    popUpTo(navController.graph.id) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = {
                    if (selectedItem > previousSelectedItem) {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(500)
                        )
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(500)
                        )
                    }
                },
                exitTransition = {
                    if (selectedItem > previousSelectedItem) {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(500)
                        )
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(500)
                        )
                    }
                },
                popEnterTransition = {
                    if (selectedItem > previousSelectedItem) {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(500)
                        )
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(500)
                        )
                    }
                },
                popExitTransition = {
                    if (selectedItem > previousSelectedItem) {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(500)
                        )
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(500)
                        )
                    }
                }
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(onNavigate = { route -> navController.navigate(route) })
                }
                composable(Screen.Cards.route) {
                    QuestionCards()
                }
                composable(Screen.Profile.route) {
                    ProfileScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun QuestionCards() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }
    var isChangingCard by remember { mutableStateOf(false) }
    
    val alphaAnimatable = remember { Animatable(1f) }
    val scaleAnimatable = remember { Animatable(1f) }
    
    val dragOffsetX = if (isChangingCard) 0f else offsetX
    val offsetXAnimated by animateFloatAsState(
        targetValue = dragOffsetX,
        animationSpec = tween(durationMillis = 150),
        label = "offset"
    )

    val rotation = if (isChangingCard) 0f else (offsetX / 50).coerceIn(-10f, 10f)
    val dragAlpha = (1f - (abs(offsetX) / 500f)).coerceIn(0f, 1f)
    val finalAlpha = if (isChangingCard) alphaAnimatable.value else dragAlpha

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        key(currentQuestionIndex) {
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(400.dp)
                    .graphicsLayer {
                        scaleX = scaleAnimatable.value
                        scaleY = scaleAnimatable.value
                        alpha = finalAlpha
                        rotationZ = rotation
                        translationX = offsetXAnimated
                    }
                    .pointerInput(isChangingCard) {
                        detectDragGestures(
                            onDragEnd = {
                                if (abs(offsetX) > 150 && !isChangingCard) {
                                    isChangingCard = true
                                    
                                    coroutineScope.launch {
                                        alphaAnimatable.snapTo(0f)
                                        scaleAnimatable.snapTo(0.85f)
                                        
                                        offsetX = 0f
                                        
                                        currentQuestionIndex = (currentQuestionIndex + 1) % QuestionsRepository.questions.size
                                        
                                        delay(50)
                                        
                                        launch {
                                            alphaAnimatable.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(700, easing = LinearOutSlowInEasing)
                                            )
                                        }
                                        
                                        launch {
                                            scaleAnimatable.animateTo(
                                                targetValue = 1f,
                                                animationSpec = tween(600, easing = FastOutSlowInEasing)
                                            )
                                        }
                                        
                                        delay(700)
                                        isChangingCard = false
                                    }
                                } else {
                                    offsetX = 0f
                                }
                            },
                            onDrag = { change, dragAmount ->
                                if (!isChangingCard) {
                                    change.consume()
                                    offsetX += dragAmount.x
                                }
                            }
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = QuestionsRepository.questions[currentQuestionIndex].text,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        val isLeftThresholdReached = remember(offsetX) { offsetX < -150 }
        val isRightThresholdReached = remember(offsetX) { offsetX > 150 }
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = if (isLeftThresholdReached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(
                            color = if (isRightThresholdReached) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}