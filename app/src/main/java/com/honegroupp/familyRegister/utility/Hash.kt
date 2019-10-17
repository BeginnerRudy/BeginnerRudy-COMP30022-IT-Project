package com.honegroupp.familyRegister.utility


import java.security.MessageDigest


class Hash {
    companion object {

        val salt: String = "BAILLIEU"
        val algorithm = "SHA-256"

        fun applyHash(text: String): String {

            //            add salt to string
            val newText = text + salt
            val bytes = text.toByteArray()

            //get the hashing API
            val md = MessageDigest.getInstance(algorithm)
            val digest = md.digest(bytes)
            return digest.fold("", { str, it -> str + "%02x".format(it) })
        }
    }
}