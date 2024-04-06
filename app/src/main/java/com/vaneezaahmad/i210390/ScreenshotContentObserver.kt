package com.vaneezaahmad.i210390

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.util.Locale


class ScreenshotContentObserver(private val mContext: Context) :
    ContentObserver(Handler(Looper.getMainLooper())) {
    private val mContentResolver: ContentResolver

    init {
        mContentResolver = mContext.contentResolver
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (uri == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
            checkRecentImagesForScreenshots()
        }
    }

    @SuppressLint("Range")
    private fun checkRecentImagesForScreenshots() {
        val imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
            cursor = mContentResolver.query(
                imagesUri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
                    val filePath =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))

                    // Check if the file name starts with the screenshot pattern
                    if (filePath != null && filePath.lowercase(Locale.getDefault()).startsWith(
                            SCREENSHOT_PATTERN.lowercase(Locale.getDefault())
                        )
                    ) {
                        // File name matches the screenshot pattern
                        Log.d(
                            TAG,
                            "Screenshot detected: $filePath"
                        )

                        Toast.makeText(
                            mContext,
                            "Screenshot detected: $filePath",
                            Toast.LENGTH_SHORT
                        ).show()
                        // You can do further processing with the screenshot here
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking for screenshots", e)
        } finally {
            cursor?.close()
        }
    }

    companion object {
        private const val TAG = "ScreenshotObserver"
        private const val SCREENSHOT_PATTERN = "Screenshot" // Modify as per naming pattern
    }
}


