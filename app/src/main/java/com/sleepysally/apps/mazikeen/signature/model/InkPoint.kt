package com.sleepysally.apps.mazikeen.signature.model

class InkPoint {
    var x: Number = -1f
    var y: Number  = -1f
    var pressure: Number = 0f
    var timeMs: Long = 0

    constructor(x: Number, y: Number, pressure: Number, timeMs: Long) {
        this.x = x
        this.y = y
        this.pressure = pressure

        this.timeMs = timeMs
    }

    override fun toString(): String {
        return "{ x: ${this.x}, y: ${this.y}, pressure: ${this.pressure}, timeMs: ${this.timeMs} },\n"
    }
}
