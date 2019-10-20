package com.honegroupp.familyRegister.utility

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

/**
 * FilePathUtil is response for find the path of a image on the phone given
 * the uri of that image*/
class FilePathUtil {
    companion object {
        /*Convert the the uri of file to the absolute path*/
        fun getFilePathFromContentUri(uri: Uri, activity: Activity): String? {
            var path: String? = null
            var image_id: String? = null

            var cursor: Cursor? = activity.getContentResolver()
                .query(uri, null, null, null, null)
            if (cursor != null) {
                cursor!!.moveToFirst()
                image_id = cursor!!.getString(0)
                image_id = image_id!!.substring(image_id.lastIndexOf(":") + 1)
                cursor!!.close()
            }

            cursor = activity.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                arrayOf<String>(image_id!!),
                null
            )
            if (cursor != null) {
                cursor!!.moveToFirst()
                path = cursor!!
                    .getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
                cursor!!.close()
            }
            return path
        }

    }
}