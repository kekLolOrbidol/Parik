package kristi.heroofmythhaven

import android.graphics.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class Player: GameObject, CharacterAttribute{
    // Character Attributes Interface Variables
    override var hp: Int //playerFrames health points
    override var damage: Int
    override var animationFrames: ArrayList<Bitmap> // List of the player's animation frames
    override var currentFrame = 0
    override var clock = 0 // Used as a counter to slow down the animation

    private var rightAttackBoundingBox: RectF
    private var leftAttackBoundingBox: RectF

    // These are used to affect the players animation, movement, and attack
    var facingRight = true
    var isWalking = false
    var isJumping = false
    var isAttacking = false

    // Physics interface
    override var velocityY = 0f
    override var boundingBox: RectF
    override var velocityX = 0f
    override var gravity = GRAVITY
    override var time = TIME
    override var mTime = 0f
    override var location = PointF(0f,0f)

    constructor(mBitmap: ArrayList<Bitmap>, startingPoint: PointF, hp: Int, damage: Int) {
        animationFrames = mBitmap
        this.hp = hp
        this.damage = damage
        this.location.x = startingPoint.x
        this.location.y = startingPoint.y
        boundingBox = RectF(this.location.x, this.location.y, this.location.x + mBitmap[0].width, this.location.y + mBitmap[0].height)
        leftAttackBoundingBox = RectF(this.location.x - 50, this.location.y, this.location.x + mBitmap[0].width, this.location.y + mBitmap[0].height)
        rightAttackBoundingBox = RectF(this.location.x, this.location.y, this.location.x + mBitmap[0].width + 50, this.location.y + mBitmap[0].height)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(animationFrames[currentFrame], this.location.x, this.location.y, null)
    }

    override fun update(context: Boolean) {
        if (context) {
            move(location)
        }

        // Update the players bounding box
        boundingBox.left = this.location.x
        boundingBox.right = this.location.x + animationFrames[0].width
        boundingBox.top = this.location.y
        boundingBox.bottom = this.location.y + animationFrames[0].height

        //update players attack bounding box
        leftAttackBoundingBox.left = this.location.x - 50
        leftAttackBoundingBox.right = this.location.x + animationFrames[0].width
        leftAttackBoundingBox.top = this.location.y
        leftAttackBoundingBox.bottom = this.location.y + animationFrames[0].height

        rightAttackBoundingBox.left = this.location.x
        rightAttackBoundingBox.right = this.location.x + animationFrames[0].width + 50
        rightAttackBoundingBox.top = this.location.y
        rightAttackBoundingBox.bottom = this.location.y + animationFrames[0].height
    }

    fun animate() {
        if(isWalking && facingRight) { //walk right
             if(clock++ == 5){
                 if(currentFrame == 0) {
                     currentFrame = 1
                 }
                 else {
                     currentFrame = 0
                 }
                 clock = 0
             }
        }
        else if(isWalking && !facingRight) { //walk left
            if(clock++ == 5){
                if(currentFrame == 4) {
                    currentFrame = 5
                }
                else {
                    currentFrame = 4
                }
                clock = 0
            }
        }
        else if(isJumping && facingRight) { //jumping right
            if(clock++ == 5){
                if(currentFrame == 0) {
                    currentFrame = 3
                }
                else {
                    currentFrame = 0
                }
                clock = 0
            }
        }
        else if(isJumping && !facingRight) { //jumping left
            if(clock++ == 5){
                if(currentFrame == 4) {
                    currentFrame = 7
                }
                else {
                    currentFrame = 4
                }
                clock = 0
            }
        }
        else if(isAttacking && facingRight) { //attack right
            if(clock++ == 5){
                if(currentFrame == 0) {
                    currentFrame = 2
                }
                else {
                    currentFrame = 0
                }
                clock = 0
            }
        }
        else if(isAttacking && !facingRight) {//attack left
            if(clock++ == 5){
                if(currentFrame == 4) {
                    currentFrame = 6
                }
                else {
                    currentFrame = 4
                }
                clock = 0
            }
        }
        else {
            if(facingRight){
                currentFrame = 0
            }
            else {
                currentFrame = 4
            }
        }


    }

    fun getLocation(point: PointF) {
        point.x = this.location.x
        point.y = this.location.y
    }

    override fun move(point: PointF){
        mTime += time
        point.x += (velocityX * (time))
        point.y -= (velocityY * (mTime) - 0.5f * gravity * mTime * mTime)
    }

    // The collision for the player object is used for attacking purposes
    override fun collision(pObj: Physics): Direction{
        val bBox: RectF

        if (facingRight) {
            bBox = rightAttackBoundingBox
        }
        else {
            bBox = leftAttackBoundingBox
        }

        if (RectF.intersects(pObj.boundingBox, bBox)){
            val w = 0.5 * (bBox.width() + pObj.boundingBox.width())
            val h = 0.5 * (bBox.height() + pObj.boundingBox.height())
            val dx = bBox.centerX() - pObj.boundingBox.centerX()
            val dy = bBox.centerY() - pObj.boundingBox.centerY()

            if (abs(dx) <= w && abs(dy) <= h) {
                val wy = w * dy
                val hx = h * dx

                if (wy > hx) {
                    if (wy > -hx) {
                        return Direction.BOTTOM
                    }
                    else { // LEFT
                        pObj.location.x += (w + dx).toFloat() + 100 // Push the monster back
                        return Direction.LEFT
                    }
                }
                else {
                    if (wy > -hx) { // RIGHT
                        pObj.location.x -= (w - dx).toFloat() // Push the monster back
                        return Direction.RIGHT
                    }
                    else {
                        if ((pObj.location.x - location.x) > 0) { // Push the monster back depending on which direction the player is facing
                            pObj.location.x += (w + dx).toFloat() + 100
                        }
                        else {
                            pObj.location.x -= (w - dx).toFloat() + 100
                        }
                        return Direction.TOP
                    }

                }
            }
        }
        return Direction.NONE
    }

    fun resetTime() {
        mTime = 0f
    }

    override fun dealDamage(character: CharacterAttribute): Int {
        character.hp -= damage
        return character.hp
    }
}