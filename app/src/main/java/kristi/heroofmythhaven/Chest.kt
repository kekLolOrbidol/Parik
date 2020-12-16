package kristi.heroofmythhaven

import android.graphics.*
import kotlin.math.abs

class Chest: GameObject{
    // Physics interface variables
    override var location: PointF
    override var velocityX: Float = VX
    override var velocityY: Float = 0f
    override var gravity: Float = 0f
    override var time: Float = TIME
    override var mTime = 0f
    override var boundingBox: RectF

    private var chest: Bitmap

    constructor(bitMap: Bitmap, startingPoint: PointF) {
        chest = bitMap
        location = startingPoint
        boundingBox = RectF(location.x, location.y, location.x + bitMap.width, location.y + bitMap.height)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(chest, location.x, location.y, null)
    }

    override fun update(context: Boolean) {
        if (context) {
            move(location)
        }

        // Update the bounding box with the new location
        boundingBox.left = location.x
        boundingBox.right = location.x + chest.width
    }

    override fun move(point: PointF) {
        point.x -= velocityX*time
    }

    override fun collision(pObj: Physics): Direction{
        if (RectF.intersects(pObj.boundingBox, boundingBox)){
            val w = 0.5 * (boundingBox.width() + pObj.boundingBox.width())
            val h = 0.5 * (boundingBox.height() + pObj.boundingBox.height())
            val dx = boundingBox.centerX() - pObj.boundingBox.centerX()
            val dy = boundingBox.centerY() - pObj.boundingBox.centerY()
            if (abs(dx) <= w && abs(dy) <= h) {
                val wy = w * dy
                val hx = h * dx

                if (wy > hx) {
                    if (wy > -hx) {
                        pObj.velocityY = 0f // BOTTOM
                        pObj.gravity = 0f
                        pObj.location.y -= (h - dy).toFloat()
                        return Direction.BOTTOM
                    }
                    else { // LEFT
                        pObj.location.x += (w + dx).toFloat()
                        return Direction.LEFT
                    }
                }
                else {
                    if (wy > -hx) { // RIGHT
                        pObj.location.x -= (w - dx).toFloat()
                        return Direction.RIGHT
                    }
                    else {
                        pObj.velocityY = 1f // TOP
                        return Direction.TOP
                    }

                }
            }
        }
        return Direction.NONE
    }
}