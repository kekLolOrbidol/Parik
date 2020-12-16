package kristi.heroofmythhaven

import android.graphics.*
import android.util.Log
import kotlin.math.abs

class FloorI: GameObject {
    override var location: PointF
    override var velocityX: Float = 0f
    override var velocityY: Float = 0f
    override var gravity: Float = 0f
    override var time: Float = 0f
    override var boundingBox: RectF
    override var mTime = 0f

    constructor(mBitmaps: List<Bitmap>, screenSize: PointF) {
        location = PointF(0f, screenSize.y * 0.79f)

        // We have to make the floor huge or else the monsters will fall as soon as they are loaded into the game
        boundingBox = RectF(-300f, screenSize.y * 0.79f, screenSize.x + 10000f, screenSize.y + 300f)
    }

    override fun collision(pObj: Physics): Direction {
        if (RectF.intersects(pObj.boundingBox, boundingBox)){
            val w = 0.5 * (boundingBox.width() + pObj.boundingBox.width()) // Average width
            val h = 0.5 * (boundingBox.height() + pObj.boundingBox.height()) // Average height
            val dx = boundingBox.centerX() - pObj.boundingBox.centerX() // difference of centers
            val dy = boundingBox.centerY() - pObj.boundingBox.centerY()

            if (abs(dx) <= w && abs(dy) <= h) {
                val wy = w * dy
                val hx = h * dx

                if (wy > hx) {
                    if (wy > -hx) {
                        pObj.velocityY = 0f// BOTTOM
                        pObj.gravity = 0f
                        pObj.location.y -= (h - dy).toFloat()
                        return Direction.BOTTOM
                    }
                    else {
                        pObj.velocityX = 0f// LEFT
                        return Direction.LEFT
                    }
                }
                else {
                    if (wy > -hx) {
                        // Since the floor needs to be huge in order to accomodate for loading monsters into the game
                        // The diagonal calculation for player collisions with the floor after jumping from a landing
                        // Actually registers the collision direction as a right collision, hence why these variables are
                        // set here
                        pObj.velocityX = 0f // RIGHT
                        pObj.velocityY = 0f// BOTTOM
                        pObj.gravity = 0f
                        pObj.mTime = 0f
                        pObj.location.y -= (h - dy).toFloat()
                        return Direction.RIGHT
                    }
                    else {
                        pObj.velocityY = 0f // TOP
                        return Direction.TOP
                    }
                }
            }
        }
        return Direction.NONE
    }


    override fun move(point: PointF) {
    }

    // Since it does not move or get draw the floor objects draw and update have no functionality
    // The floor does not actually have a bitmap associated with it, its bitmap is managed as part of the background
    override fun draw(canvas: Canvas) {} // Do nothing

    override fun update(context: Boolean) {} // Do nothing
}