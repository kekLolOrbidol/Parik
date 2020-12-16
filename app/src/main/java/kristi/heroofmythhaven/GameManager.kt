package kristi.heroofmythhaven

import android.graphics.PointF

// These constants are used as a standard by all game objects for physics operations
const val TIME = 0.25f
const val VX = 18f
const val VY = 18f
const val GRAVITY = 7f

class GameManager{
    private lateinit var gameObjects: MutableList<GameObject>
    private lateinit var monsterObjects: MutableList<Monster>
    private val context: LevelActivity
    private var loaded = false // Used to prevent the game information from being loaded morethan once, we do this because
    // in gameView it needed to be in the draw funtion or else the game would crash

    private lateinit var player: Player
    private lateinit var background: Background
    private lateinit var chest: Chest
    private var ground: Float = 0f // will be set to the location of the ground in the y direction
    private var isJumping = true // Boolean representing if the player is jumping
    var rightUserInput: ActionUserInput = ActionUserInput.NOTHING
    var leftUserInput: MovementUserInput = MovementUserInput.NOINPUT
    private var hasWon = false
    private var isPaused = false
    private val level: Int // Game level, this corresponds to which dungeon will get loaded
    private val playerLevel: Int
    private val playerType: String

    constructor(level: Int, context: LevelActivity, playerLevel: Int, playerType: String) {
        this.context = context
        this.level = level
        this.playerLevel = playerLevel
        this.playerType = playerType
    }

    // Based on what level is inputted, go to a JSON file and grab the necessary information
    fun loadGameObject(){
            if (!loaded) {
                val gameView = context.findViewById<GameView>(R.id.gameView)
                val floor = FloorI(listOf(), PointF(gameView.width.toFloat(), gameView.height.toFloat()))
                val loadedLevel = LevelLoader(context, level, playerType, playerLevel)
                ground = gameView.height.toFloat() * 0.79f

                gameObjects = loadedLevel.getGameObjs()
                monsterObjects = loadedLevel.getMonsterObjs()
                gameObjects.add(floor)
                player = loadedLevel.getPlayer()
                background = loadedLevel.getBackground()
                chest = loadedLevel.getChest()
                loaded = true
            }
    }

    fun update() {
        if (!isPaused) { // Should not do anything if the game is paused
            context.updateHP(player.hp)
            var movementContext =
                false // Determines if the background and game objects can move or if the player can move
            // Their relation ship is inversely related

            var bottomCollision =
                false // Used to fix a bug where the player could walk off a landing and be floating in the air
            var isAttacking = false

            // Checks for if the player is attacking, will define the isAttacking variable, which affects whether the next
            // block of if statements will run or not, in other words if the player is attacking, then they cannot do anything else
            if (leftUserInput == MovementUserInput.LEFT && rightUserInput == ActionUserInput.ATTACK) {
                player.facingRight = false // This will effect animation and direction in which the attack occurs
                if (!isJumping) { // Cannot attack if already jumping
                    player.velocityX = 0f

                    // These booleans were a quick fix to meet desired functionality, will want to refactor to avoid these
                    isAttacking = true
                    player.isAttacking = true
                    player.isWalking = false
                    player.isJumping = false
                }
            } else if (leftUserInput == MovementUserInput.RIGHT && rightUserInput == ActionUserInput.ATTACK) {
                player.facingRight = true
                if (!isJumping) {
                    player.velocityX = 0f
                    isAttacking = true
                    player.isAttacking = true
                    player.isWalking = false
                    player.isJumping = false
                }
            } else if (leftUserInput == MovementUserInput.NOINPUT && rightUserInput == ActionUserInput.ATTACK) {
                if (!isJumping) {
                    player.velocityX = 0f
                    isAttacking = true
                    player.isAttacking = true
                    player.isWalking = false
                    player.isJumping = false
                }
            } else {
                isAttacking = false
                player.isAttacking = false
            }

            // If the player is not attacking, then they can move and/or jump
            if (!isAttacking) {
                if (leftUserInput == MovementUserInput.RIGHT && rightUserInput == ActionUserInput.JUMP) {
                    if (!isJumping) {
                        player.location.y -= 1
                        player.gravity = GRAVITY
                        player.velocityY = VY
                        player.velocityX = VX
                        player.time = TIME
                        player.resetTime()
                        isJumping = true
                    }
                    player.facingRight = true
                    player.isJumping = true
                    player.isWalking = false
                } else if (leftUserInput == MovementUserInput.LEFT && rightUserInput == ActionUserInput.JUMP) {
                    if (!isJumping) {
                        player.location.y -= 1
                        player.gravity = GRAVITY
                        player.velocityY = VY
                        player.velocityX = -VX
                        player.time = TIME
                        player.resetTime()
                        isJumping = true
                    }

                    // Prevent the player from jumping beyond the end of the leftside of the screen
                    if (player.location.x < 0) {
                        player.velocityX = 0f
                        player.location.x = 0f
                    }
                    player.facingRight = false
                    player.isJumping = true
                    player.isWalking = false
                } else if (leftUserInput == MovementUserInput.NOINPUT && rightUserInput == ActionUserInput.NOTHING) {
                    player.velocityX = 0f
                    if (player.location.x < 0) {
                        player.location.x = 0f
                    }

                    if (!isJumping) {
                        player.velocityY = 0f
                        player.gravity = 0f
                    }
                    player.isJumping = false
                    player.isWalking = false
                } else if (leftUserInput == MovementUserInput.NOINPUT && rightUserInput == ActionUserInput.JUMP) {
                    if (!isJumping) {
                        player.location.y -= 1
                        player.gravity = GRAVITY
                        player.velocityY = VY
                        player.time = TIME
                        player.resetTime()
                        isJumping = true
                    }
                    player.isJumping = true
                    player.isWalking = false
                } else if (leftUserInput == MovementUserInput.LEFT && rightUserInput == ActionUserInput.NOTHING) {
                    player.velocityX = -VX
                    if (player.location.x < 0) {
                        player.velocityX = 0f
                        player.location.x = 0f
                    }
                    if (!isJumping) {
                        player.velocityY = 0f
                        player.gravity = 0f
                    }
                    player.facingRight = false
                    player.isJumping = false
                    player.isWalking = true
                } else if (leftUserInput == MovementUserInput.RIGHT && rightUserInput == ActionUserInput.NOTHING) {
                    player.velocityX = VX
                    if (!isJumping) {
                        player.velocityY = 0f
                        player.gravity = 0f
                    }
                    player.facingRight = true
                    player.isJumping = false
                    player.isWalking = true
                }
            }

            // Collision has the side effect of possibly altering the input physics object's internal physics parameters if a collision occurs
            // The outcome of this loop will effect the players movement regardless of user input, and also sets booleans
            // For the next update call
            for (gameObj in gameObjects) { //check the objects colliding with the player
                if (gameObj != player) {
                    when (gameObj.collision(player)) {
                        Direction.NONE -> {
                        } // Do nothing
                        Direction.TOP -> {
                        }
                        Direction.BOTTOM -> {
                            isJumping = false
                            player.resetTime()
                            bottomCollision = true
                            if (gameObj == chest && !hasWon) {
                                win()
                                hasWon = true
                            }
                        }
                        Direction.LEFT -> {
                            if (gameObj == chest && !hasWon) {
                                win()
                                hasWon = true
                            }
                        }
                        Direction.RIGHT -> {
                            if (gameObj == background) {
                                movementContext = true
                            }
                            if (gameObj == chest && !hasWon) {
                                win()
                                hasWon = true
                            }
                        }
                    }
                }
            }

            // This loop checks monster collisions with the environment/terrain and the player
            for (monster in monsterObjects) {
                // Collisions with the environment/terrain
                for (gameObj in gameObjects) {
                    if (gameObj != background && gameObj != player) {
                        when (gameObj.collision(monster)) {
                            Direction.NONE -> {
                            } // Do nothing
                            Direction.TOP -> {
                            }
                            Direction.BOTTOM -> {

                            }
                            Direction.LEFT -> {
                                monster.goingLeft = false // Dictates the direction the monster moves
                                monster.velocityX = VX
                            }
                            Direction.RIGHT -> {
                                monster.goingLeft = true
                                monster.velocityX = -VX
                            }
                        }
                    }
                }

                // The monster cannot deal damage to the player if they're currently attacking
                if (!isAttacking) {
                    when (monster.collision(player)) { //check if the monster is hitting the player
                        Direction.NONE -> {
                        } // Do nothing
                        Direction.TOP -> {
                            if (monster.dealDamage(player) <= 0) { // dealDamage should return the input objs health after damage has been dealt
                                lose()
                            }
                        }
                        Direction.BOTTOM -> {
                            if (monster.dealDamage(player) <= 0) {
                                lose()
                            }
                        }
                        Direction.LEFT -> {
                            if (monster.dealDamage(player) <= 0) {
                                lose()
                            }
                        }
                        Direction.RIGHT -> {
                            if (monster.dealDamage(player) <= 0) {
                                lose()
                            }
                        }
                    }
                }
            }

            if (isAttacking) {
                for (monster in monsterObjects) {
                    when (player.collision(monster)) { //check if the player is hitting a monster
                        Direction.NONE -> {
                        } // Do nothing
                        Direction.TOP -> {
                            if (player.dealDamage(monster) <= 0) {
                                monster.delete(context) // Delete the reference to the monster object if it defeated
                            }
                        }
                        Direction.BOTTOM -> {
                            if (player.dealDamage(monster) <= 0) {
                                monster.delete(context)
                            }
                        }
                        Direction.LEFT -> {
                            if (player.dealDamage(monster) <= 0) {
                                monster.delete(context)
                            }

                        }
                        Direction.RIGHT -> {
                            if (player.dealDamage(monster) <= 0) {
                                monster.delete(context)
                            }
                        }
                    }
                }
            }

            // Addresses a bug where the player walks off an obstacle but does not fall
            if (!bottomCollision) {
                player.gravity = GRAVITY
            }

            for (gameObj in gameObjects) { //update all the game objects, background
                gameObj.update(movementContext) //we also update player here but it doesn't matter because of movement context
            }

            for (monster in monsterObjects) { //separate update for all the monsters
                monster.update(movementContext)
            }

            player.animate()
            // The movement of the player and the other objects of the game are inversely related
            player.update(!movementContext) // This should eventually become the specific context for characters
        }
    }

    private fun win() {
        context.showEndPopup(true)
    }

    private fun lose() {
        context.updateHP(player.hp)
        context.showEndPopup(false)
    }

    fun getGameObjects(): List<GameObject> {
        return gameObjects
    }

    fun getMonsterObjects(): List<Monster> {
        return monsterObjects
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }
}