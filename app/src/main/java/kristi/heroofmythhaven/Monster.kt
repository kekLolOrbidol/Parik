package kristi.heroofmythhaven

import android.content.Context
import android.graphics.*
import kotlin.math.abs

class Monster: GameObject, CharacterAttribute {
    // Physics Interface Variables
    override var velocityY = 0f
    override var boundingBox: RectF
    override var velocityX = -VX
    override var gravity = GRAVITY
    override var time = TIME
    override var location: PointF
    override var mTime = 0f

    // CharacterAttribute interface variables
    override var hp: Int
    override var damage: Int
    override var animationFrames: ArrayList<Bitmap> // List of the playerFrames frames
    override var currentFrame = 0
    override var clock = 0 // Used as a counter to slow down the animation

    var goingLeft = true


    constructor(bitMap: ArrayList<Bitmap>, hp: Int, damage: Int, location: PointF) {
        this.hp = hp
        this.damage = damage
        animationFrames = bitMap
        this.location = location
        boundingBox = RectF(location.x, location.y, location.x + animationFrames[0].width, location.y + animationFrames[0].height)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(animationFrames[currentFrame], this.location.x, this.location.y, null)
    }

    override fun update(context: Boolean) {
        if (context) {
            if (goingLeft) {
                velocityX = - 2 * VX
            }
            else {
                velocityX = 0f
            }
        }
        else {
            if (goingLeft) {
                velocityX = -VX
            }
            else {
                velocityX = VX
            }
        }
        if(clock++ == 5){
            if(currentFrame == 0) {
                currentFrame = 1
            }
            else {
                currentFrame = 0
            }
            clock = 0
        }

        move(location)


        boundingBox.left = this.location.x
        boundingBox.right = this.location.x + animationFrames[0].width
        boundingBox.top = this.location.y
        boundingBox.bottom = this.location.y + animationFrames[0].height
    }

    override fun collision(pObj: Physics): Direction {
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
                        return Direction.BOTTOM
                    }
                    else { // LEFT
                        pObj.location.x += (w + dx).toFloat() + 100 //Push the object back
                        goingLeft = true
                        velocityX = -VX
                        return Direction.LEFT
                    }
                }
                else {
                    if (wy > -hx) { // RIGHT
                        pObj.location.x -= (w - dx).toFloat() + 100 //Push the object back
                        goingLeft = false
                        velocityX = VX
                        return Direction.RIGHT
                    }
                    else {
                        if ((pObj.location.x - location.x) > 0) { //Push the object back depending on the direction the monster is currently traveling
                            pObj.location.x += (w + dx).toFloat() + 100
                            goingLeft = true
                        }
                        else {
                            pObj.location.x -= (w - dx).toFloat() + 100
                            pObj.location.y -= (h - dy).toFloat() // Make sure the input object's location is outside of the bounds of this landing
                            goingLeft = false
                        }
                        return Direction.TOP
                    }

                }
            }
        }
        return Direction.NONE
    }

    override fun move(point: PointF) {
        point.x += (velocityX * (time))
    }

    override fun dealDamage(character: CharacterAttribute): Int {
        character.hp -= damage
        return character.hp
    }

    fun delete(context: Context) {
        boundingBox = RectF(0f,0f,0f,0f)
        location.x = 0f
        location.y = 0f
        animationFrames[0] = Bitmap.createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.empty))
        animationFrames[1] = Bitmap.createBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.empty))
    }
}