package tk.tarajki.meme.util

import java.util.*

class Duration(hour: Double) {
    private var durationInMilliSecond: Double = hour * 3600000


    var secound: Double
        set(value) {
            durationInMilliSecond = value * 1000
        }
        get() {
            return durationInMilliSecond / 1000
        }

    var minute: Double
        set(value) {
            secound = 60 * value
        }
        get() {
            return secound / 60
        }

    var hour: Double
        set(value) {
            minute = 60 * value
        }
        get() {
            return minute / 60
        }

    var day: Double
        set(value) {
            hour = 24 * value
        }
        get() {
            return hour / 24
        }

    operator fun plus(date: Date): Date {

        return Date(date.time + durationInMilliSecond.toLong())
    }
}