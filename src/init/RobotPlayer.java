package init;
import battlecode.common.*;

public strictfp class RobotPlayer {
    static RobotController rc;

    static int turnCount;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;
        Robot me = null;
        switch (rc.getType()) {
            case ENLIGHTENMENT_CENTER: me = new EC(rc); break;
            case POLITICIAN: me = new Politician(rc); break;
            case SLANDERER: me = new Slanderer(rc); break;
            case MUCKRAKER: me = new Muckraker(rc); break;
        }
        turnCount = 0;

        System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                if (rc.getRoundNum() == 300) rc.resign();
                me.takeTurn();
                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }
}
