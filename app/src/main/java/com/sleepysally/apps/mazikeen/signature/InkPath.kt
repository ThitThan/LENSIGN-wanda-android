package com.sleepysally.apps.mazikeen.signature

import android.graphics.Path

class InkPath {
    var color: Int = 0x0
    var strokeWidth: Int = 1

    var emboss: Boolean = false
    var blur: Boolean = false

    var path: Path? = null

    constructor(color: Int, strokeWidth: Int, emboss: Boolean, blur: Boolean, path: Path) {
        this.color = color
        this.strokeWidth = strokeWidth

        this.emboss = emboss
        this.blur = blur

        this.path = path
    }
}