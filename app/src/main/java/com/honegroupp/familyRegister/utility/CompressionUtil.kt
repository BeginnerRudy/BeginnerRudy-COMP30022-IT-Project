package com.honegroupp.familyRegister.utility

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
/**
 * CompressionUtil contains function to compress the image and change resolution
 * of the image to make uploading more efficient
 * */
class CompressionUtil {

    companion object {


        val IMAGA_QUALITY = 30
        val MAX_RESOLUTION: Float = 2016f

        //compress the image
        fun compressImage(scaledBitmap: Bitmap): ByteArray {
            val bytes = ByteArrayOutputStream()
            scaledBitmap
                .compress(Bitmap.CompressFormat.JPEG, IMAGA_QUALITY, bytes)
            return bytes.toByteArray()
        }

        /*Decrease the resolution of image to reduce the size*/
        fun scaleDown(
            realImage: Bitmap, filter: Boolean
        ): Bitmap {
            val maxImageSize: Float = MAX_RESOLUTION
            val ratio = Math.min(
                maxImageSize / realImage.width,
                maxImageSize / realImage.height
            )
            val width = Math.round(ratio * realImage.width)
            val height = Math.round(ratio * realImage.height)

            return Bitmap.createScaledBitmap(
                realImage, width,
                height, filter
            )
        }

    }
}