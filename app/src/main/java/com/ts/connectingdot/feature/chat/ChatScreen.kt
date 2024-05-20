package com.ts.connectingdot.feature.chat

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.streamliners.compose.android.comp.appBar.TitleBarScaffold
import com.streamliners.compose.comp.CenterText

@Composable
fun ChatScreen(
    channelId: String,
    navController: NavController
) {


    TitleBarScaffold(
        title = "Chat",
        navigateUp = { navController.navigateUp() }) {
        
        CenterText(text = channelId)
        
    }

}