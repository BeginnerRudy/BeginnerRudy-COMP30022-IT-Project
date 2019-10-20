package com.honegroupp.familyRegister
import com.honegroupp.familyRegister.utility.EmailPathSwitchUtil
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 *Test the email and path can convert to each other
 *
 */
class EmailPathSwitchTest {
    var emails : Array<String> = arrayOf("123@gmail.com",
        "simple@example.com",
        "very.common@example.com",
        "disposable.style.email.with+symbol@example.com",
        "other.email-with-hyphen@example.com",
        "fully-qualified-domain@example.com",
        "user.name+tag+sorting@example.com",
        "x@example.com",
        "example-indeed@strange-example.com",
        "admin@mailserver1",
        "example@s.example",
        " \"@example.org",
        "john..doe\"@example.org",
        "mailhost!username@example.org",
        "user%example.com@example.org")


    var paths : Array<String> = arrayOf("123@gmail=com",
        "simple@example=com",
        "very=common@example=com",
        "disposable=style=email=with+symbol@example=com",
        "other=email-with-hyphen@example=com",
        "fully-qualified-domain@example=com",
        "user=name+tag+sorting@example=com",
        "x@example=com",
        "example-indeed@strange-example=com",
        "admin@mailserver1",
        "example@s=example",
        " \"@example=org",
        "john==doe\"@example=org",
        "mailhost!username@example=org",
        "user%example=com@example=org")

    @Before
    fun setUp(){
        assertEquals(true, emails.size == paths.size)
    }

    /*
    Test whether the emails address convert to the paths without error
    */
    @Test
    fun emailToPath() {
        for (i in 0..emails.size) {
            assertEquals(true, EmailPathSwitchUtil.emailToPath(emails[i]).equals(paths[i]))
        }
    }

    /*
    Test whether the paths convert to original email address without error
    */
    @Test
    fun pathToEmail() {
        for (i in 0..emails.size) {
            assertEquals(true, EmailPathSwitchUtil.pathToEmail(paths[i]).equals(emails[i]))
        }
    }


}
