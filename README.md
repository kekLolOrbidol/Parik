# Hero_of_Mythhaven

William Ritchie 815829203
Kristi Werry 823386935

Github: https://github.com/Writchie19/Hero_of_Mythhaven.git

Description:  This is a 2D side scrolling android rpg game. Embark on quests, defeat monsters, acquire gold, and level up.
Use gold to unlock cool new character skins. Can you beat the final level?
Notes:
    - Every level is win-able by touching the treasure chest at the end of the quest.
    - Buttons are: Move Left, Move Right, Attack, and Jump.

There are no special instructions required to run the game.  The game will be locked in landscape orientation. No 3rd party libraries are 
needed. 

List of known issues:
 - We could not finish every thing we wanted:
	- Did not save the character or game state when the app is closed or is onPause state
	- Did not implement Monster AI
	- Did not implement projectiles
	- Did not implement moving obstacles
	- Did not implement an HP bar, instead its a number

 - Known Bugs:
	- Under certain circumstances you instantly die from monsters if you fall on top of them
	- Sometimes when colliding with terrain you "phase" which for a brief time looks like their are multiples of the character, but this usually sorts itself out

 - Final comments:
	- We spent a surprising amount of time handling game bugs, which took away from development time of other features
	- We attempted to write well designed code but we ran out of time and favored functionality over well written code (sorry but you're going to see a lot of booleans)
	- Given a few more days we would have likely finished the remaining features and definitely refactored our code 
