package kristi.heroofmythhaven

import android.graphics.Canvas

interface GameObject: Physics{
    fun draw(canvas: Canvas)
    fun update(context: Boolean) //This will likely change from boolean to an actual object that implements our own context interface
    // For now the boolean represents if can object can move, so later will want a context for non-character and character objs (their conditions..
    // For moving are slightly different
}