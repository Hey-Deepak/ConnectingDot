package com.ts.connectingdot.feature.editProfile

import com.streamliners.pickers.media.PickedMedia
sealed class ImageState {

    data class New(var media: PickedMedia?): ImageState()
    data class Url(val url: String?): ImageState()


}