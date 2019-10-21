package com.honegroupp.familyRegister.utility
import java.security.MessageDigest


/**
 * HashUtil class is for apply hash to the password to improve privacy
 * The algorithm used is SHA-256 and the salt is "BAILLIEU"
 * The salt can prevent dictionary attack
 * */
class HashUtil {
    companion object {

        private const val salt: String = "BAILLIEU"
        private const val algorithm:String = "SHA-256"

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