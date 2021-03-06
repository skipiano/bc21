package coms;

import battlecode.common.*;
import static coms.Robot.*;

public class Nav {
    private int patience;
    private MapLocation currentDest;
    private int closestDist;

    public static int[][] cardinalNewSense;
    public static int[][] interCardinalNewSense;

    private static Direction[] directions = Direction.cardinalDirections();

    // constants
    private static double THRESHOLD = 0.45;

    public Nav() {
        patience = 0;
        currentDest = null;
        closestDist = 1000000;


//        try {
//            initializingSurroundingMap();
//        } catch (GameActionException e) {
//            System.out.println("oops, intialization failed");
//        }

        //the following is a look up table that makes updating map from 4*x^2 bytecode to 4*x bytecode
        switch(rc.getType()) {
            case ENLIGHTENMENT_CENTER:
                cardinalNewSense = new int[][]{{-6, 2}, {-5, 3}, {-4, 4}, {-3, 5}, {-2, 6}, {-1, 6}, {0, 6}, {1, 6}, {2, 6}, {3, 5}, {4, 4}, {5, 3}, {6, 2}};
                interCardinalNewSense = new int[][]{{-2, 6}, {-1, 6}, {0, 6}, {1, 6}, {2, 6}, {2, 5}, {3, 5}, {3, 4}, {4, 4}, {4, 3}, {5, 3}, {5, 2}, {6, 2}, {6, 1}, {6, 0}, {6, -1}, {6, -2}};
                break;
            case MUCKRAKER:
                cardinalNewSense = new int[][]{{-5, 2}, {-4, 3}, {-3, 4}, {-2, 5}, {-1, 5}, {-0, 5}, {1, 5}, {2, 5}, {3, 4}, {4, 3}, {5, 2}};
                interCardinalNewSense = new int[][]{{-2, 5}, {-1, 5}, {0, 5}, {1, 5}, {2, 5}, {2, 4}, {3, 4}, {3, 3}, {4, 3}, {4, 2}, {5, 2}, {5, 1}, {5, 0}, {5, -1}, {5, -2}};
                break;
            case POLITICIAN:
                cardinalNewSense = new int[][]{{-4, 3}, {-3, 4}, {-2, 4}, {-1, 4}, {-0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 3}};
                interCardinalNewSense = new int[][]{{-3, 4}, {-2, 4}, {-1, 4}, {-0, 4}, {1, 4}, {2, 4}, {3, 4}, {3, 3}, {4, 3}, {4, 2}, {4, 1}, {4, 0}, {4, -1}, {4, -2}, {4, -3}};
                break;
            case SLANDERER:
                cardinalNewSense = new int[][]{{-4, 2}, {-3, 3}, {-2, 4}, {-1, 4}, {-0, 4}, {1, 4}, {2, 4}, {3, 3}, {4, 2}};
                interCardinalNewSense = new int[][]{{-2, 4}, {-1, 4}, {-0, 4}, {1, 4}, {2, 4}, {2, 3}, {3, 3}, {3, 2}, {4, 2}, {4, 1}, {4, 0}, {4, -1}, {4, -2}};
                break;
        }
        System.out.println("made nav");
    }

//    public static void move(Direction dir) throws GameActionException {
//        rc.move(dir);
//
//        MapLocation loc = rc.getLocation();
//        MapLocation nloc;
//        int[][]template;
//        int dirID = directionToInt(dir);
//        if (directionToInt(dir)%2==0){template = cardinalNewSense;}
//        else {template = interCardinalNewSense;};
//        int rotation=dirID>>1;
//        int nx;
//        int ny;
//        for(int[] xypair : template){
//            switch ( rotation%4){
//                case 0: nloc = loc.translate( xypair[0], xypair[1]); break;
//                case 1: nloc = loc.translate( xypair[1],-xypair[0]); break;
//                case 2: nloc = loc.translate(-xypair[0],-xypair[1]); break;
//                case 3: nloc = loc.translate(-xypair[1], xypair[0]); break;
//                default: nloc = loc.translate( xypair[0], xypair[1]); System.out.println("nav move wrong");
//            }
//            nx=nloc.x%128;
//            ny=nloc.y%128;
//            if (mapPassibility[nx][ny]==0 && rc.canSenseLocation(nloc)){
//                if(!rc.onTheMap(nloc)){mapPassibility[nx][ny]=-1;};
//                mapPassibility[nx][ny]=rc.sensePassability(nloc);
//            }
//        }
//    }

//    public void initializingSurroundingMap() throws GameActionException {
//        MapLocation loc = rc.getLocation();
//        MapLocation nloc;
//        int nx;
//        int ny;
//        for(int x = -sqrtSensorRadius; x<= sqrtSensorRadius; x++){
//            for(int y = -sqrtSensorRadius; y<= sqrtSensorRadius; y++){
//                nloc = loc.translate(x,y);
//                nx=nloc.x%128;
//                ny=nloc.y%128;
//                if (mapPassibility[nx][ny]==0 && rc.canSenseLocation(nloc)){
//                    if(!rc.onTheMap(nloc)){mapPassibility[nx][ny]=-1;};
//                    mapPassibility[nx][ny]=rc.sensePassability(nloc);
//                }
//            }
//        }
//    }

    // chase a unit based on their ID
    public void chase(RobotInfo ri) throws GameActionException {

    }

    public int[][] getEnds() {
        int midX, midY;
        if (minX == 9999 && maxX == 30065) midX = 20000;
        else if (minX == 9999) midX = maxX-32;
        else if (maxX == 30065) midX = minX+32;
        else midX = (minX+maxX)/2;

        if (minY == 9999 && maxY == 30065) midY = 20000;
        else if (minY == 9999) midY = maxY-32;
        else if (maxY == 30065) midY = minY+32;
        else midY = (minY+maxY)/2;

        ends = new int[][]{{minX, minY}, {minX, midY}, {minX, maxY}, {midX, maxY}, {maxX, maxY}, {maxX, midY}, {maxX, minY}, {midX, minY}};
        return ends;
    }

    public static boolean isTrapped () throws GameActionException {
        for (Direction dir: directions) {
            if (checkDirMoveable(dir)) {
                return false;
            }
        }
        return true;
    }

    /*
    Tries to move in the target direction
    Returns the Direction that we moved in
    Returns null if did not move
    */
    public static Direction tryMoveInDirection (Direction dir) throws GameActionException {
        if (checkDirMoveable(dir)) {
            rc.move(dir);
            return dir;
        }
        return null;
    }

	/*
	---------------
	BUG PATHFINDING
	---------------
	Uses the bug pathfinding algorithm to navigate around obstacles towards a target MapLocation
	Details here: https://www.cs.cmu.edu/~motionplanning/lecture/Chap2-Bug-Alg_howie.pdf
	Taken/adapted from TheDuck314 Battlecode 2016
	Which was taken/adapted from Kryptonite Battlecode 2020
	Assumes that we are ready to move
	Returns the Direction we moved in
	Returns null if did not move
	*/

    final public static int MAX_BUG_HISTORY_LENGTH = 100;

    public static MapLocation bugTarget = null;

    public static boolean bugTracing = false;
    public static MapLocation bugLastWall = null;
    public static int bugClosestDistanceToTarget = 1000000000;
    public static int bugTurnsWithoutWall = 0;
    public static boolean bugRotateLeft = true; // whether we are rotating left or right

    public static MapLocation[] bugVisitedLocations = null;
    public static int bugVisitedLocationsIndex;
    public static int bugVisitedLocationsLength;

    public Direction bugNavigate (MapLocation target) throws GameActionException {
        System.out.println("Bug navigating to " + target);

        if (isTrapped()) {
            return null;
        }

        if (!target.equals(bugTarget)) {
            bugTarget = target;
            bugTracing = false;
            bugClosestDistanceToTarget = rc.getLocation().distanceSquaredTo(bugTarget);
        }

        if (rc.getLocation().equals(bugTarget)) {
            return null;
        }

        // bugClosestDistanceToTarget = Math.min(bugClosestDistanceToTarget, here.distanceSquaredTo(bugTarget));

        Direction destDir = rc.getLocation().directionTo(bugTarget);

        System.out.println("BUG_NAVIGATE");
        System.out.println("bugTarget: " + bugTarget);
        System.out.println("bugClosestDistanceToTarget: " + bugClosestDistanceToTarget);
        System.out.println("destDir: " + destDir);
        System.out.println("bugTracing: " + bugTracing);

        if (!bugTracing) { // try to go directly towards the target
            Direction tryMoveResult = tryMoveInDirection(destDir);
            if (tryMoveResult != null) {
                return tryMoveResult;
            } else {
                bugStartTracing();
            }
        } else { // we are on obstacle, trying to get off of it
            if (rc.getLocation().distanceSquaredTo(bugTarget) < bugClosestDistanceToTarget) {
                Direction tryMoveResult = tryMoveInDirection(destDir);
                System.out.println("on obstacle");
                if (tryMoveResult != null) { // we got off of the obstacle
                    System.out.println("We're free!");
                    bugTracing = false;
                    return tryMoveResult;
                }
            }
        }

        Direction moveDir = bugTraceMove(false);

        if (bugTurnsWithoutWall >= 2) {
            bugTracing = false;
        }

        return moveDir;
    }

    /*
    Runs if we just encountered an obstacle
    */
    public static void bugStartTracing() throws GameActionException {
        bugTracing = true;

        bugVisitedLocations = new MapLocation[MAX_BUG_HISTORY_LENGTH];
        bugVisitedLocationsIndex = 0;
        bugVisitedLocationsLength = 0;

        bugTurnsWithoutWall = 0;
        bugClosestDistanceToTarget = rc.getLocation().distanceSquaredTo(bugTarget);

        Direction destDir = rc.getLocation().directionTo(bugTarget);

        Direction leftDir = destDir;
        MapLocation leftDest;
        int leftDist = Integer.MAX_VALUE;
        for (int i = 0; i < 8; ++i) {
            leftDir = leftDir.rotateLeft();
            leftDest = rc.adjacentLocation(leftDir);
            if (checkDirMoveable(leftDir)) {
                leftDist = leftDest.distanceSquaredTo(bugTarget);
                break;
            }
        }

        Direction rightDir = destDir;
        MapLocation rightDest;
        int rightDist = Integer.MAX_VALUE;
        for (int i = 0; i < 8; ++i) {
            rightDir = rightDir.rotateRight();
            rightDest = rc.adjacentLocation(rightDir);
            if (checkDirMoveable(rightDir)) {
                rightDist = rightDest.distanceSquaredTo(bugTarget);
                break;
            }
        }


        if (leftDist < rightDist) { // prefer rotate right if equal
            bugRotateLeft = true;
            bugLastWall = rc.adjacentLocation(leftDir.rotateRight());
        } else {
            bugRotateLeft = false;
            bugLastWall = rc.adjacentLocation(rightDir.rotateLeft());
        }
        System.out.println("START_TRACING");
        System.out.println("bugRotateLeft: " + bugRotateLeft);
        System.out.println("bugLastWall: " + bugLastWall);
    }

    /*
    Returns the Direction that we moved in
    Returns null if we did not move
    */
    public static Direction bugTraceMove(boolean recursed) throws GameActionException {

        Direction curDir = rc.getLocation().directionTo(bugLastWall);

        // adds to array
        bugVisitedLocations[bugVisitedLocationsIndex] = rc.getLocation();
        bugVisitedLocationsIndex = (bugVisitedLocationsIndex + 1) % MAX_BUG_HISTORY_LENGTH;
        bugVisitedLocationsLength = Math.min(bugVisitedLocationsLength + 1, MAX_BUG_HISTORY_LENGTH);

        if (checkDirMoveable(curDir)) {
            bugTurnsWithoutWall += 1;
        } else {
            bugTurnsWithoutWall = 0;
        }

        for (int i = 0; i < 8; ++i) {
            if (bugRotateLeft) {
                curDir = curDir.rotateLeft();
            } else {
                curDir = curDir.rotateRight();
            }
            MapLocation curDest = rc.adjacentLocation(curDir);
            if (!rc.onTheMap(curDest) && !recursed) {
                System.out.println("Hit the edge of map, reverse and recurse");
                // if we hit the edge of the map, reverse direction and recurse
                bugRotateLeft = !bugRotateLeft;
                return bugTraceMove(true);
            }
            if (checkDirMoveable(curDir)) {
                rc.move(curDir);
                for (int x = 0; x < bugVisitedLocationsLength; x++) {
                    if (bugVisitedLocations[x].equals(curDest)) {
                        System.out.println("Resetting bugTracing");
                        bugTracing = false;
                        break;
                    }
                }
                return curDir;
            } else {
                bugLastWall = rc.adjacentLocation(curDir);
            }
        }

        return null;
    }

    public static boolean checkDirMoveable(Direction dir) throws GameActionException {
        return rc.canMove(dir) && rc.sensePassability(rc.getLocation().add(dir)) > THRESHOLD;
    }


    /*
    ---------------
    try bf distjra
    ---------------
     */


}
