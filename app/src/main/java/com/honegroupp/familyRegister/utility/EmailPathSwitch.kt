package com.honegroupp.familyRegister.utility

class EmailPathSwitch {
    companion object {

        //Convert email address to path name on firebase
        //Since path name cannot contains ".", convert "." in email address to "=", then can use
        //it as the path. For example: 12345@gmail.com convert to 12345@gmail=com.

        //convert the email address to the relative path
        fun emailToPath(email: String): String {
            return email.replace(".", "=")
        }

        //convert the path to email
        fun pathToEmail(path: String): String {
            return path.replace("=", ".")
        }
    }
}