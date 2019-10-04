package com.honegroupp.familyRegister.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class CompressionUtil{

    companion object{


        val IMAGA_QUALITY = 30

        //compress the image th PNG
        fun compressImage(uri: Uri, context:Context):Uri{
            val bytes = ByteArrayOutputStream()

//            require minimum API level 28
//            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))

//            convert the uri to bitmap
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

//            compres the image
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGA_QUALITY, bytes)

//            convert bitmap to bytes
            val bytesData = bytes.toByteArray()

//            convert the bytes to newBitmap
            val newBitmap  = BitmapFactory.decodeByteArray(bytesData, 0, bytesData.size);


//            convert the bitmap to path
            val path =
                MediaStore.Images.Media.insertImage(context.contentResolver,
                    newBitmap, System.currentTimeMillis().toString(), null)

//            convert the path to uri
            val compressedUri = Uri.parse(path)

            //return the uri of compressed image
            return compressedUri
        }

    }
}