package com.honegroupp.familyRegister.model


import java.security.MessageDigest


class Hash{
    companion object {

        val salt: String = "BAILLIEU"
        val algorithm = "SHA-256"

        fun applyHash(text: String):String {

//            add salt to string
            val newText = text + salt
            val bytes = text.toByteArray()

            //get the hashing API
            val md = MessageDigest.getInstance(algorithm)
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }


        //compare whether two string has same hash are equal
        fun equalHash(text1: String, text2: String): Boolean{
            return applyHash(text1+ salt) == (applyHash(text2+ salt))
        }

        //check whether a text has the hash
        fun checkHash(text: String, hash: String):Boolean{
            return applyHash(text+ salt) == hash
        }


    }
}