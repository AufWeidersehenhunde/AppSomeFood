package com.example.somefood.Utils

import android.content.Intent.ACTION_PICK
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts

class PhotoProfile(
    activityResultRegistry: ActivityResultRegistry,
    private val callback: (image: Uri?) -> Unit
) {
    private val getContentLauncher = activityResultRegistry.register(
        ACTION_PICK,
        ActivityResultContracts.GetContent()
    ) { uri -> callback.invoke(uri) }

    fun pickPhoto() {
        getContentLauncher.launch("image/*")
    }
}