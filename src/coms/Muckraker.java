package coms;

import battlecode.common.*;

public class Muckraker extends Robot {


    private MapLocation wandLoc;
    private int offset = 0;

    public Muckraker(RobotController rc) {
        super(rc);
    }

    public void takeTurn() throws GameActionException {
        super.takeTurn();
        int closestPoliticianDist = 100000;
        MapLocation closestPolitician = null;
        int closestSlandererDist = 100000;
        MapLocation closestSlanderer = null;
        int maxSlanderer = -1;
        MapLocation maxSlandererLocation = null;
        for (RobotInfo robot : rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, enemy)) {
            if (robot.type.canBeExposed()) {
                int dist = rc.getLocation().distanceSquaredTo(robot.getLocation());
                if (dist <= closestSlandererDist) {
                    closestSlandererDist = dist;
                    closestSlanderer = robot.location;
                }
                if (dist <= RobotType.MUCKRAKER.actionRadiusSquared) {
                    maxSlanderer = Math.max(maxSlanderer, robot.getInfluence());
                    maxSlandererLocation=robot.location;
                }
            }
            if (robot.type == RobotType.POLITICIAN) {
                int dist = rc.getLocation().distanceSquaredTo(robot.getLocation());
                if (dist > 2 && dist <= closestPoliticianDist) {
                    closestPoliticianDist = dist;
                    closestPolitician = robot.location;
                }
            }
        }
        if (rc.isReady()) {
            // expose the max slanderer in range
            if (maxSlanderer != -1) {
                if (rc.senseNearbyRobots(maxSlandererLocation,1,enemy).length>0 & rc.canExpose(maxSlandererLocation)) {
                    rc.expose(maxSlandererLocation);
                }
            }
            int closestECDist = 100000;
            MapLocation closestEC = null;
            for (int i = 0; i < 12; i++) {
                if (enemyECs[i] != null) {
                    int dist = rc.getLocation().distanceSquaredTo(enemyECs[i]);
                    if (dist < closestECDist) {
                        closestECDist = dist;
                        closestEC = enemyECs[i];
                    }
                }
            }
            // move to the closest slanderer
            if (closestSlandererDist != 100000) {
                nav.bugNavigate(closestSlanderer);
            }
            // else move to the closest enemy HQ if known
            else if (closestEC != null) {
                if (!rc.getLocation().isAdjacentTo(closestEC)) {
                    if (rc.getLocation().isWithinDistanceSquared(closestEC, 13)) {
                        // move to an adjacent spot
                        int closestSpotDist = 100000;
                        MapLocation closestSpot = null;
                        for (Direction dir: directions) {
                            MapLocation loc = closestEC.add(dir);
                            if (!rc.isLocationOccupied(loc)) {
                                int dist = rc.getLocation().distanceSquaredTo(loc);
                                if (dist < closestSpotDist) {
                                    closestSpotDist = dist;
                                    closestSpot = loc;
                                }
                            }
                        }
                        if (closestSpot != null) {
                            nav.bugNavigate(closestSpot);
                        } else {
                            // TODO: call for attack
                        }
                    }
                    else nav.bugNavigate(closestEC);
                }
            }
            else {
                // else move to the nearest politician not adjacent
                if (closestPolitician != null) nav.bugNavigate(closestPolitician);
                    // otherwise wander
                else wander();
            }
        }
    }

    // wander around
    // TODO: what if you're already at a corner/side and you want to explore more (+3 to the end to explore?)
    public void wander() throws GameActionException {
        wandLoc = new MapLocation(nav.getEnds()[(rc.getID()+offset) % 8][0], nav.getEnds()[(rc.getID()+offset) % 8][1]);
        if (rc.getLocation().isWithinDistanceSquared(wandLoc, 8)) {
            offset++;
            wander();
            return;
        }
        nav.bugNavigate(wandLoc);
    }
}