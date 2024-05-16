package com.ts.connectingdot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.streamliners.base.BaseActivity
import com.streamliners.base.uiEvent.UiEventDialogs
import com.ts.connectingdot.ui.NavHostGraph
import com.ts.connectingdot.ui.theme.ConnectingDotTheme

class MainActivity : BaseActivity() {

    override var buildType: String = BuildConfig.BUILD_TYPE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConnectingDotTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    NavHostGraph(navController)
                    UiEventDialogs()

                }
            }
        }
    }
}

