package kristi.heroofmythhaven

import android.graphics.Bitmap

interface CharacterAttribute {
    var hp: Int // Character health points
    var damage: Int // Damage dealt by the character
    var animationFrames: ArrayList<Bitmap> //bitmap list for animation
    var currentFrame: Int //keeps track of the current frame of the animation
    var clock : Int // Used as a counter to slow down the animation
    // Should return the input characters remaining health after damage has been dealt
    fun dealDamage(character: CharacterAttribute): Int
}