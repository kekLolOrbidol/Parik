package kristi.heroofmythhaven

import android.graphics.*
import kotlin.math.abs

class Background: GameObject {
    // Physics Interface variables
    override var velocityX: Float = VX
    override var velocityY: Float = 0f
    override var gravity: Float = 0f
    override var time: Float = TIME
    override var boundingBox: RectF
    override var mTime = 0f

    override var location = PointF(0f,0f)
    private var background1: Bitmap
    private var bLocation1: PointF
    private var bLocation2: PointF
    private var background2: Bitmap
    private var bList: List<Bitmap>
    var numCompletedBackgrounds = 0
    private val screenSize: PointF

    constructor(mBitmaps: List<Bitmap>, screenSize: PointF) {
        bList = mBitmaps
        background1 = bList[0] // initial background
        background2 = bList[1]
        this.screenSize = screenSize
        bLocation1 = PointF(0f,0f)
        bLocation2 = PointF(screenSize.x, 0f)
        boundingBox = RectF(screenSize.x/2, -500f, screenSize.x, screenSize.y)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(background1, bLocation1.x, bLocation1.y, null)
        canvas.drawBitmap(background2, bLocation2.x, bLocation2.y, null)
        //canvas.drawRect(boundingBox, Paint(Color.RED))
    }

    override fun update(context: Boolean) {
        //changing out backgrounds
        if (bLocation1.x < 0f-screenSize.x) {
            bLocation1.x = screenSize.x
            numCompletedBackgrounds++
        }
        if (bLocation2.x < 0f-screenSize.x) {
            bLocation2.x = screenSize.x
            numCompletedBackgrounds++
        }
        //moving backgrounds
        if (context) {
            move(bLocation1)
            move(bLocation2)
        }

    }

    override fun move(point: PointF) {
        point.x -= velocityX*time
    }

    override fun collision(pObj: Physics): Direction{
        if (RectF.intersects(pObj.boundingBox, boundingBox)){
            val w = 0.5 * (boundingBox.width() + pObj.boundingBox.width()) // Average width
            val h = 0.5 * (boundingBox.height() + pObj.boundingBox.height()) // Average height
            val dx = boundingBox.centerX() - pObj.boundingBox.centerX() // difference of centers
            val dy = boundingBox.centerY() - pObj.boundingBox.centerY()

            if (abs(dx) <= w && abs(dy) <= h) {
                // The diagonals
                val wy = w * dy
                val hx = h * dx

                if (wy > hx) {
                    if (wy > -hx) {
                        pObj.velocityY = 0f// BOTTOM
                        return Direction.BOTTOM
                    }
                    else {
                        pObj.velocityX = 0F// LEFT
                        return Direction.LEFT
                    }
                }
                else {
                    if (wy > -hx) { // RIGHT
                        pObj.location.x -= (w - dx).toFloat()
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
}