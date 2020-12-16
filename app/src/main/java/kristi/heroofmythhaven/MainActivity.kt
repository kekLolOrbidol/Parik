package kristi.heroofmythhaven

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var username: String = ""
    private var level: Int = 1 //Player level
    private var gold: Int = 0
    private val QUESTCODE = 123 // Used in on activity result for when the levelactivity finishes and has sent information back
    private var userCharacter: String = "human"
    private var viewingCharacter = 0 //0=human, 1=death knight, 2=dark elf, 3=demon spawn
    private var availableCharacters = mutableListOf(1, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = intent.getStringExtra("Username").toString()
        playNameMain.text = username
        levelMain.text = String.format(getString(R.string.levelTitle) + level.toString())
        goldMain.text = String.format(getString(R.string.goldTitle) + gold.toString())

        quest1.setOnClickListener {
            if(availableCharacters[viewingCharacter] == 1) {
                val startQuest1 = Intent(this, LevelActivity::class.java)
                startQuest1.putExtra("Username", username)
                startQuest1.putExtra("Level", level)
                startQuest1.putExtra("Gold", gold)
                startQuest1.putExtra("Character", userCharacter)
                startQuest1.putExtra("QuestNumber", 1)
                startActivityForResult(startQuest1, QUESTCODE)
            }
            else {
                Toast.makeText(this, "You can't quest with this character yet.", Toast.LENGTH_SHORT).show()
            }
        }
        quest2.setOnClickListener {
            if(availableCharacters[viewingCharacter] == 1) {
                val startQuest2 = Intent(this, LevelActivity::class.java)
                startQuest2.putExtra("Username", username)
                startQuest2.putExtra("Level", level)
                startQuest2.putExtra("Gold", gold)
                startQuest2.putExtra("Character", userCharacter)
                startQuest2.putExtra("QuestNumber", 2)
                startActivityForResult(startQuest2, QUESTCODE)
            }
            else {
                Toast.makeText(this, "You can't quest with this character yet.", Toast.LENGTH_SHORT).show()
            }
        }
        quest3.setOnClickListener {
            if(availableCharacters[viewingCharacter] == 1) {
                val startQuest3 = Intent(this, LevelActivity::class.java)
                startQuest3.putExtra("Username", username)
                startQuest3.putExtra("Level", level)
                startQuest3.putExtra("Gold", gold)
                startQuest3.putExtra("Character", userCharacter)
                startQuest3.putExtra("QuestNumber", 3)
                startActivityForResult(startQuest3, QUESTCODE)
            }
            else {
                Toast.makeText(this, "You can't quest with this character yet.", Toast.LENGTH_SHORT).show()
            }
        }
        changeCharacter.setOnClickListener{
            if(viewingCharacter == 0) { //if current is human
                viewingCharacter = 1
                characterPhoto.setImageResource(R.drawable.death_knight)
                if(availableCharacters[viewingCharacter] == 1) {
                    userCharacter = "deathknight"
                    gold2000Text.text = ""
                }
                else {
                    gold2000Text.text = getString(R.string._2_000_gold)
                }
            }
            else if (viewingCharacter == 1) { //if current is death knight
                viewingCharacter = 2
                characterPhoto.setImageResource(R.drawable.dark_elf)
                if(availableCharacters[viewingCharacter] == 1) {
                    userCharacter = "darkelf"
                    gold2000Text.text = ""
                }
                else {
                    gold2000Text.text = getString(R.string._2_000_gold)
                }
            }
            else if (viewingCharacter == 2) { //if current is dark elf
                viewingCharacter = 3
                characterPhoto.setImageResource(R.drawable.demon_spawn)
                if(availableCharacters[viewingCharacter] == 1) {
                    userCharacter = "demon"
                    gold2000Text.text = ""
                }
                else {
                    gold2000Text.text = getString(R.string._2_000_gold)
                }
            }
            else if(viewingCharacter == 3){ //if current is demon spawn
                viewingCharacter = 0
                characterPhoto.setImageResource(R.drawable.human)
                if(availableCharacters[viewingCharacter] == 1) {
                    userCharacter = "human"
                }
                gold2000Text.text = ""
            }
            else {
                //We should never get here
            }
        }

        buyCharacter.setOnClickListener {
            if(availableCharacters[viewingCharacter] == 1) {
                Toast.makeText(this, "You've already bought this character.", Toast.LENGTH_SHORT).show()
            }
            else if (gold >= 2000) {
                gold -= 2000
                goldMain.text = String.format(getString(R.string.goldTitle) + gold.toString())
                availableCharacters[viewingCharacter] = 1
                Toast.makeText(this, "Purchase Complete.", Toast.LENGTH_SHORT).show()
                gold2000Text.text = ""
                if(viewingCharacter == 0) {
                    userCharacter = "human"
                }
                else if(viewingCharacter == 1) {
                    userCharacter = "deathknight"
                }
                else if(viewingCharacter == 2) {
                    userCharacter = "darkelf"
                }
                else if(viewingCharacter == 3) {
                    userCharacter = "demon"
                }
                else{
                    //should never get here
                }
            }
            else {
                Toast.makeText(this, "You can't buy this character yet.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != QUESTCODE) { //check if the request code is the same
            return
        }
        when (resultCode) {
            Activity.RESULT_OK -> {
                //get the result from level activity
                val finalLevelResult = data!!.getBooleanExtra("DidWin", false)
                Log.i("onActivityResult", "$finalLevelResult")
                if(finalLevelResult){
                    //we won the level so increase gold and level
                    gold += 2000
                    level++
                    levelMain.text = String.format(getString(R.string.levelTitle) + level.toString())
                    goldMain.text = String.format(getString(R.string.goldTitle) + gold.toString())
                }
            }
            Activity.RESULT_CANCELED ->
                Log.i("onActivityResult", "cancel")
        }
    }
}
