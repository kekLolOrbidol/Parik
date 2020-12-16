package kristi.heroofmythhaven

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_level.view.*

class GameView: View{
    private lateinit var gameManager: GameManager

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        gameManager.loadGameObject() // A boolean in gamemanager prevents everything from being continuously reloaded,
        // this is something we want to refactor and fix
        gameManager.update()
        for (gameObj in gameManager.getGameObjects()) {
            gameObj.draw(canvas)
        }

        for (monster in gameManager.getMonsterObjects()) {
            monster.draw(canvas)
        }

        invalidate()
    }

    fun setGameManager(gameManager: GameManager) {
        this.gameManager = gameManager
    }

    fun setLeftUserInput(leftInput: MovementUserInput) {
        gameManager.leftUserInput = leftInput
    }
    fun setRightUserInput(rightInput: ActionUserInput) {
        gameManager.rightUserInput = rightInput
    }
}