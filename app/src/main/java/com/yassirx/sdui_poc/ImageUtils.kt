package com.yassirx.sdui_poc

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.DecimalFormat
import kotlin.math.pow


fun createImageUri(context: Context): Uri {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()
    val file = File.createTempFile(
        "arbin_",
        ".jpg",
        directory,
    )
    val authority = context.packageName + ".fileprovider"
    return FileProvider.getUriForFile(
        context,
        authority,
        file,
    )
}

suspend fun getImageRequestBody(uri: Uri, context: Context): RequestBody {
    val sourceFile = getImageBody(uri, context, imageSize = 1000000)
    return MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart(
            "file", sourceFile.name,
            sourceFile.asRequestBody("application/octet-stream".toMediaType())
        )
        .build()
}

private suspend fun getImageBody(
    imageUri: Uri,
    context: Context,
    imageSize: Long
): File {
    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
        imageUri,
        "r",
        null
    )
    val file = File(context.cacheDir, File(imageUri.path!!).name)

    return file
}

private fun getReadableFileSize(size: Long): String {
    if (size <= 0) {
        return "0"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}