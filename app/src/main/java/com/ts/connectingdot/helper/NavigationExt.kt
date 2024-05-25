package com.ts.connectingdot.helper

import androidx.navigation.NavController
import com.ts.connectingdot.ui.Screens

fun NavController.navigateTo(
    to: Screens,
    popUpTo: Screens? = null,
    isInclusive: Boolean = true
) {

    if (popUpTo != null){ popBackStack(popUpTo.route, isInclusive) }

    navigate(to.route)

}

fun NavController.navigateTo(
    to: String,
    popUpTo: String? = null,
    isInclusive: Boolean = true
) {

    if (popUpTo != null){ popBackStack(popUpTo, isInclusive) }

    navigate(to)

}