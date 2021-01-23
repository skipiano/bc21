# Battlecode 2021 Scaffold

This is the Battlecode 2021 scaffold, containing an `examplefuncsplayer`. Read https://2021.battlecode.org/getting-started!

### Project Structure

- `README.md`
    This file.
- `build.gradle`
    The Gradle build file used to build and run players.
- `src/`
    Player source code.
- `test/`
    Player test code.
- `client/`
    Contains the client. The proper executable can be found in this folder (don't move this!)
- `build/`
    Contains compiled player code and other artifacts of the build process. Can be safely ignored.
- `matches/`
    The output folder for match files.
- `maps/`
    The default folder for custom maps.
- `gradlew`, `gradlew.bat`
    The Unix (OS X/Linux) and Windows versions, respectively, of the Gradle wrapper. These are nifty scripts that you can execute in a terminal to run the Gradle build tasks of this project. If you aren't planning to do command line development, these can be safely ignored.
- `gradle/`
    Contains files used by the Gradle wrapper scripts. Can be safely ignored.


### Useful Commands

- `./gradlew run`
    Runs a game with the settings in gradle.properties
- `./gradlew update`
    Update to the newest version! Run every so often


### To-do list

All of the bold points should be implemented before sprint 2

- Politician
    - Concentrate patrolling on the side where the attacks come from
    - Need to secure neutrals faster
    - Attack politicians not exploding buffrakers -> don't approach when already one tagging it
    ー　Use small buffs minimally just for a bit
    - Kill useless politicians
    - Just send big politicians to infiltrate and take over spawning spaces
- Slanderer
    - Make closeness to HQ have a lot more weight than closeness to enemy HQ
- Muckraker
    - __Ideas for a 1000 muckraker rush? Surrounded by 1 cost muck/polis__
    - Send buffraker right after taken all the neutral ecs
    - Needs to go towards enemy EC more
    - Cardinal direction muckrakers for defense
    - 1 cost muckrakers also surround buffrakers to help protect it
- EC
    - Analyze top teams and improve build order
    - Reset the count properly, we need more eco even if we have more than 10000+ influence
    - Deal with big polis near our ec
- Improve moveAway functionality
- Analyze top teams:
    - Specifically:
    - baby ducks, PP, Super Cow Powers, Kryptonite, Nikola, Chop Suey, Malott Fat Cats

Order:
15. take neutral ecs first
16. fix attack politicians not exploding buffrakers
18. make politicians surround the hq and take up spaces rather than attacking
19. build more slanderers even when we have more money
20. deal with big polis near our ec


Done:
- ~~Defense politician only needs to spread out from other defense, otherwise muckrakers can slip by~~
- ~~Need to attack EC if there's a ton of attack politicians not doing anything~~
- ~~Communicate EC influence~~
- ~~Change the cost of the search() politician, since devs changed the specs accordingly~~
- ~~Need to spawn more slanderers for more eco: I think pp does 1:1:1 ratio?~~
    - ~~Maybe separate between the initial ECs and the new ECs:~~
    - ~~The initial ECS focus on slanderers and politicians, while the new ones focus on muckrakers~~
    - ~~Still need more eco for the newer ecs~~
- ~~Fix bug where IC.MUCKRAKER is being used for ID and location, and also it sometimes overrides the signal for the muckraker to escape properly~~
- ~~When wandering has a tendency to move in groups, split them up somehow?~~
- ~~Improve muckraker spacing from each other, sometimes they just go wayy backwards~~
- ~~Muckrakers still crowd around a politician, probably bc they come back after getting out of range 10~~
- ~~Not spawning polis to take over neutral ECs fast enough~~
- ~~If muckraker buff is large enough, buff EC it just spawned from~~
- ~~Debug wander and why units get stuck -> java ghosthug last round~~
- ~~Spawn less muckrakers and more politicians in the midgame~~
- ~~Attack politicians check for buffrakers on the way, and kill them if they can~~
- ~~Defense politician more efficient when defending against buffrakers~~
- ~~Remove self buff code~~
- ~~Self destruct useless politicians~~