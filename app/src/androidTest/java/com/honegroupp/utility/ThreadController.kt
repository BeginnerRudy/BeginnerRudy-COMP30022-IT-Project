package com.honegroupp.utility

class ThreadController {
    companion object{

        const val SHORT_TOAST_WAITING: Long = 1700
        fun stopForNMilliseconds(time: Long){
            // Just for viewing the results. Remove after use.
            try {
                Thread.sleep(time)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}