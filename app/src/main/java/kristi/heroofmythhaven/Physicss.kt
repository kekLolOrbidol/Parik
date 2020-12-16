package kristi.heroofmythhaven

import android.graphics.PointF
import android.graphics.RectF

enum class Direction { //Used for collision directions
    TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    NONE
}

interface Physics {
    var velocityX: Float
    var velocityY: Float
    var gravity: Float
    var time: Float
    var mTime: Float // This is used to represent a change over time, (ie non-constant time)
    var boundingBox: RectF
    var location: PointF

    // Collision detection in most classes is accomplished by calculating the diagonal between the two objects, then based
    // On that information the direction of collision and amount of object overlap is known
    fun collision(pObj: Physics): Direction
    fun move(point: PointF)
}