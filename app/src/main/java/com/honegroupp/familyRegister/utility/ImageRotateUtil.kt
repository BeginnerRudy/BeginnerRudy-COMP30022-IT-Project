package com.honegroupp.familyRegister.utility

import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import android.provider.MediaStore





class ImageRotateUtil{
    companion object{

        /*this function retutnt the rotation of image
        modified from code written by Sumit Chakraborty on stackflow
        https://stackoverflow.com/questions/42411409/why-image-auto-rotate-when-set-to-imageview-with-picasso
        */

        fun getCameraPhotoOrientation(imageFilePath: String): Int {
            var rotate = 0

            Log.d("Orientationx","XXXX".toString())
            try {

//                val exif: ExifInterface
//
//                exif = ExifInterface(imageFilePath)
//                val exifOrientation = exif
//                    .getAttribute(ExifInterface.TAG_ORIENTATION)
//                Log.d("exifOrientation", exifOrientation)
//                val orientation = exif.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL)

                val exif:ExifInterface = ExifInterface(imageFilePath);
                val orientation:Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

                Log.d("Orientationx",orientation.toString())
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                    ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return rotate

        }


    }
}