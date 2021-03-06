package finals;

import battlecode.common.*;
import finals.utils.Debug;

import java.util.PriorityQueue;

import static finals.Robot.*;
import static finals.utils.Constants.staleCooldown;

public class Coms {
    public static PriorityQueue<Integer> signalQueue = new PriorityQueue<>();
    public static int relevantSize = 0;
    public static int relevantInd = 0;
    public static int[] relevantFlags = new int[40];
    protected RobotInfo processingRobot = null;
    public static boolean relevantDiscovery = false;

    public int tempID = 0;


    // number of possible cases for InfoCategory enum class
    private static int numCase = 17;

    //[1,17] bits for information (16 bits) (msg)    ( msg&0x00ffff     )
    //[18,22] for IC (6 bits)               (msg<<16)((msg&0x3f0000)>>16)
    //[23,24] for robot type (2 bits)       (msg<<22)((msg&0xc00000)>>22)

    public Coms() {
    }

    // TODO: need to order in terms of priority
    public enum IC {
        RESET,
        MUCKRAKER_HELP,
        FRIEND_EC,
        EC_ID,
        ENEMY_EC,
        NEUTRAL_EC,
        EDGE_N,
        EDGE_E,
        EDGE_S,
        EDGE_W,
        NO_SYM,
        POLITICIAN,
        SLANDERER,
        MUCKRAKER_ID,
        MAP_CORNER,
        MAP_NW,
        MAP_NE,
        MAP_SW,
        MAP_SE,
        MUCKRAKER,
        ATTACK,
    }

    public static int getMessage(IC cat, MapLocation coord) {
        int message;
        switch (cat) {
            case RESET         : message = 1; break;
            case MUCKRAKER_HELP: message = 2; break;
            case FRIEND_EC     : message = 3; break;
            case EC_ID         : message = 4; break;
            case ENEMY_EC      : message = 5; break;
            case NEUTRAL_EC    : message = 6; break;
            case EDGE_N        : message = 7; break;
            case EDGE_E        : message = 8; break;
            case EDGE_S        : message = 9; break;
            case EDGE_W        : message = 10; break;
            case NO_SYM        : message = 11; break;
            case POLITICIAN    : message = 12; break;
            case SLANDERER     : message = 13; break;
            case MUCKRAKER_ID  : message = 14; break;
            case MAP_CORNER    : message = 15; break;
            case MAP_NW        : message = 16; break;
            case MAP_NE        : message = 17; break;
            case MAP_SW        : message = 18; break;
            case MAP_SE        : message = 19; break;
            case MUCKRAKER     : message = 20; break;
            case ATTACK        : message = 21; break;
            default            : message = 22;
        }
        message = addCoord(message, coord) + typeInt(rc.getType());
        return message;
    }
    public static int getMessage(IC cat, MapLocation coord, int inf) {
        int message;
        switch (cat) {
            case RESET         : message = 1; break;
            case MUCKRAKER_HELP: message = 2; break;
            case FRIEND_EC     : message = 3; break;
            case EC_ID         : message = 4; break;
            case ENEMY_EC      : message = 5; break;
            case NEUTRAL_EC    : message = 6; break;
            case EDGE_N        : message = 7; break;
            case EDGE_E        : message = 8; break;
            case EDGE_S        : message = 9; break;
            case EDGE_W        : message = 10; break;
            case NO_SYM        : message = 11; break;
            case POLITICIAN    : message = 12; break;
            case SLANDERER     : message = 13; break;
            case MUCKRAKER_ID  : message = 14; break;
            case MAP_CORNER    : message = 15; break;
            case MAP_NW        : message = 16; break;
            case MAP_NE        : message = 17; break;
            case MAP_SW        : message = 18; break;
            case MAP_SE        : message = 19; break;
            case MUCKRAKER     : message = 20; break;
            case ATTACK        : message = 21; break;
            default            : message = 22;
        }
        message = addCoord(message, coord) + typeInt(rc.getType()) + addInf(inf);
        return message;
    }

    public static int getMessage(IC cat, int ID) {
        int message;
        switch (cat) {
            case RESET         : message = 1; break;
            case MUCKRAKER_HELP: message = 2; break;
            case FRIEND_EC     : message = 3; break;
            case EC_ID         : message = 4; break;
            case ENEMY_EC      : message = 5; break;
            case NEUTRAL_EC    : message = 6; break;
            case EDGE_N        : message = 7; break;
            case EDGE_E        : message = 8; break;
            case EDGE_S        : message = 9; break;
            case EDGE_W        : message = 10; break;
            case NO_SYM        : message = 11; break;
            case POLITICIAN    : message = 12; break;
            case SLANDERER     : message = 13; break;
            case MUCKRAKER_ID  : message = 14; break;
            case MAP_CORNER    : message = 15; break;
            case MAP_NW        : message = 16; break;
            case MAP_NE        : message = 17; break;
            case MAP_SW        : message = 18; break;
            case MAP_SE        : message = 19; break;
            case MUCKRAKER     : message = 20; break;
            case ATTACK        : message = 21; break;
            default            : message = 22;
        }
        message = addID(message, ID) + typeInt(rc.getType());
        return message;
    }

    public static int typeInt(RobotType type) {
        switch (type) {
            case POLITICIAN: return 0;
            case SLANDERER: return 1 << 22;
            case MUCKRAKER: return 2 << 22;
            case ENLIGHTENMENT_CENTER: return 3 << 22;
        }
        return 0;
    }
    public static int addCoord(int message, MapLocation coord) {
        return (message << 17) + ((coord.x % 128) << 7) + (coord.y % 128);
    }

    public static int addID(int message, int ID) {
        return (message << 17)+ID;
    }

    public static int addInf(int inf) { return inf << 14; }

    public static RobotType getTyp(int message) {
        switch (message >> 22) {
            case 0: return RobotType.POLITICIAN;
            case 1: return RobotType.SLANDERER;
            case 2: return RobotType.MUCKRAKER;
            case 3: return RobotType.ENLIGHTENMENT_CENTER;
        }
        return null;
    }


    public static IC getCat(int message) {
        message = message % (1 << 22);
        switch (message >> 17) {
            case 1: return IC.RESET;
            case 2: return IC.MUCKRAKER_HELP;
            case 3: return IC.FRIEND_EC;
            case 4: return IC.EC_ID;
            case 5: return IC.ENEMY_EC;
            case 6: return IC.NEUTRAL_EC;
            case 7: return IC.EDGE_N;
            case 8: return IC.EDGE_E;
            case 9: return IC.EDGE_S;
            case 10: return IC.EDGE_W;
            case 11: return IC.NO_SYM;
            case 12: return IC.POLITICIAN;
            case 13: return IC.SLANDERER;
            case 14: return IC.MUCKRAKER_ID;
            case 15: return IC.MAP_CORNER;
            case 16: return IC.MAP_NW;
            case 17: return IC.MAP_NE;
            case 18: return IC.MAP_SW;
            case 19: return IC.MAP_SE;
            case 20: return IC.MUCKRAKER;
            case 21: return IC.ATTACK;
            default: return null;
        }
    }

    public static int getID(int message) {
        return message % (1 << 17);
    }

    public static MapLocation getCoord(int message) {
        MapLocation here = rc.getLocation();
        int remX = here.x % 128;
        int remY = here.y % 128;
        message = message % (1 << 14);
        int x = message >> 7;
        int y = message % 128;
        if (Math.abs(x - remX) >= 64) {
            if (x > remX) x = here.x - remX - 128 + x;
            else x = here.x - remX + x + 128;
        } else x = here.x - remX + x;
        if (Math.abs(y - remY) >= 64) {
            if (y > remY) y = here.y - remY - 128 + y;
            else y = here.y + y + 128 - remY;
        } else y = here.y - remY + y;
        return new MapLocation(x, y);
    }

    public static int getInf(int message) { return getID(message) >> 14; }

    // relay information about surroundings
    public void collectInfo() throws GameActionException {
        // temporary fix for reducing bytecode for slanderers: fix
        if (rc.getType() == RobotType.SLANDERER) {
            RobotInfo[] enemies = rc.senseNearbyRobots(-1, team.opponent());
            for (RobotInfo r : enemies) {
                if (r.getType() == RobotType.MUCKRAKER) {
                    signalQueue.add(getMessage(IC.MUCKRAKER_HELP, r.getLocation()));
                    break;
                }
            }
        } else {
            // first check for any edges
            if (!explored) {
                for (int a = 0; a < 4; a++) {
                    if (edges[a]) continue;
                    Direction dir = Direction.cardinalDirections()[a];
                    MapLocation checkLoc = rc.getLocation().add(dir);
                    while (checkLoc.isWithinDistanceSquared(rc.getLocation(), rc.getType().sensorRadiusSquared)) {
                        if (!rc.onTheMap(checkLoc)) {
                            Debug.p("I see an edge");
                            edges[a] = true;
                            if (a == 0) {
                                maxY = checkLoc.y - 1;
                                signalQueue.add(getMessage(IC.EDGE_N, maxY));
                                addRelevantFlag(getMessage(IC.EDGE_N, maxY));
                                if (mapGenerated) {
                                    // rule out some spots
                                    for (int i = 7; i >= 0; i--) {
                                        for (int j = 7; j >= 0; j--) {
                                            if (mapSpots[i][j].y > maxY) visited[i][j] = true;
                                        }
                                    }
                                }
                            } else if (a == 1) {
                                maxX = checkLoc.x - 1;
                                signalQueue.add(getMessage(IC.EDGE_E, maxX));
                                addRelevantFlag(getMessage(IC.EDGE_E, maxX));
                                if (mapGenerated) {
                                    // rule out some spots
                                    for (int i = 7; i >= 0; i--) {
                                        for (int j = 7; j >= 0; j--) {
                                            if (mapSpots[i][j].x > maxX) visited[i][j] = true;
                                        }
                                    }
                                }
                            } else if (a == 2) {
                                minY = checkLoc.y + 1;
                                signalQueue.add(getMessage(IC.EDGE_S, minY));
                                addRelevantFlag(getMessage(IC.EDGE_S, minY));
                                if (mapGenerated) {
                                    // rule out some spots
                                    for (int i = 7; i >= 0; i--) {
                                        for (int j = 7; j >= 0; j--) {
                                            if (mapSpots[i][j].y < minY) visited[i][j] = true;
                                        }
                                    }
                                }
                            } else if (a == 3) {
                                minX = checkLoc.x + 1;
                                signalQueue.add(getMessage(IC.EDGE_W, minX));
                                addRelevantFlag(getMessage(IC.EDGE_W, minX));
                                if (mapGenerated) {
                                    // rule out some spots
                                    for (int i = 7; i >= 0; i--) {
                                        for (int j = 7; j >= 0; j--) {
                                            if (mapSpots[i][j].x < minX) visited[i][j] = true;
                                        }
                                    }
                                }
                            }
                            Debug.p("updated " + a + "th edge");
                            break;
                        }
                        checkLoc = checkLoc.add(dir);
                    }
                }
                if (mapGenerated && rc.getType() != RobotType.ENLIGHTENMENT_CENTER) {
                    int offsetX = (rc.getLocation().x - mapSpots[0][0].x + 8) % 8;
                    int offsetY = (rc.getLocation().y - mapSpots[0][0].y + 8) % 8;
                    if ((offsetX == 0 || offsetX == 7) && (offsetY == 0 || offsetY == 7)) {
                        Debug.p("I'm at one of the middle spots");
                        // at one of the middle spots
                        int x = (rc.getLocation().x - mapSpots[0][0].x + 1) / 8;
                        int y = (rc.getLocation().y - mapSpots[0][0].y + 1) / 8;
                        if (!visited[x][y]) {
                            visited[x][y] = true;
                            if (x < 4 && y < 4) {
                                // SW
                                int msgSum = 0;
                                for (int i = 0; i < 4; i++) {
                                    for (int j = 0; j < 4; j++) {
                                        if (visited[i][j]) msgSum += (1 << (i * 4 + j));
                                    }
                                }
                                if (rc.getType() == RobotType.MUCKRAKER) signalQueue.add(getMessage(IC.MAP_SW, msgSum));
                            }
                            if (x >= 4 && y < 4) {
                                // SE
                                int msgSum = 0;
                                for (int i = 4; i < 8; i++) {
                                    for (int j = 0; j < 4; j++) {
                                        if (visited[i][j]) msgSum += (1 << ((i - 4) * 4 + j));
                                    }
                                }
                                if (rc.getType() == RobotType.MUCKRAKER) signalQueue.add(getMessage(IC.MAP_SE, msgSum));
                            }
                            if (x < 4 && y >= 4) {
                                // NW
                                int msgSum = 0;
                                for (int i = 0; i < 4; i++) {
                                    for (int j = 4; j < 8; j++) {
                                        if (visited[i][j]) msgSum += (1 << (i * 4 + j - 4));
                                    }
                                }
                                if (rc.getType() == RobotType.MUCKRAKER) signalQueue.add(getMessage(IC.MAP_NW, msgSum));
                            }
                            if (x >= 4 && y >= 4) {
                                // NE
                                int msgSum = 0;
                                for (int i = 4; i < 8; i++) {
                                    for (int j = 4; j < 8; j++) {
                                        if (visited[i][j]) msgSum += (1 << ((i - 4) * 4 + j - 4));
                                    }
                                }
                                if (rc.getType() == RobotType.MUCKRAKER) signalQueue.add(getMessage(IC.MAP_NE, msgSum));
                            }
                        }
                    }
                }
            }
            if (!corners) {
                if (minX != 9999 && maxX != 30065 && minY != 9999 && maxY != 30065) {
                    corners = true;
                    // check all the symmetry stuff
                    for (int i = 0; i < 12; i++) {
                        if (totalECs[i] != null) {
                            symmetry(totalECs[i]);
                        }
                    }
                }
            }
            // whether you're a guarding a slanderer
            RobotInfo[] closeRobots = rc.senseNearbyRobots(20, team);
            boolean guard = false;
            for (RobotInfo rob : closeRobots) {
                if (getTyp(rc.getFlag(rob.getID())) == RobotType.SLANDERER) {
                    guard = true;
                    break;
                }
            }
            boolean foundSlanderer = false;
            for (RobotInfo r: robots) {
                // check for any ECs
                if (r.getType() == RobotType.ENLIGHTENMENT_CENTER) {
                    Debug.p("Found an EC!");
                    int id = r.getID();
                    Debug.p("ID is: " + id);
                    MapLocation loc = r.getLocation();
                    Debug.p("Location is: " + loc);
                    int mInd = -1;
                    boolean added = false;
                    for (int i = 11; i >= 0; i--) {
                        if (totalECs[i] == null) {
                            mInd = i;
                        } else if (totalECs[i].equals(r.getLocation())) {
                            added = true;
                            break;
                        }
                    }
                    if (mInd != -1 && !added) {
                        // new ec location, do stuff with symmetry
                        totalECs[mInd] = r.getLocation();
                        if (corners) symmetry(totalECs[mInd]);
                    }
                    if (r.getTeam() == team) {
                        int minInd = -1;
                        boolean seen = false;
                        for (int i = 11; i >= 0; i--) {
                            if (ECIds[i] == 0) {
                                minInd = i;
                            }
                            if (ECIds[i] == id) {
                                seen = true;
                                break;
                            }
                        }
                        if (minInd != -1 && !seen) {
                            ECIds[minInd] = id;
                            Debug.p("ID: Adding to signal queue");
                            signalQueue.add(getMessage(IC.EC_ID, id));
                            addRelevantFlag(getMessage(IC.EC_ID, id));
                        }
                        for (int i = 0; i < 12; i++) {
                            if (loc.equals(enemyECs[i])) {
                                enemyECs[i] = null;
                                removeRelevantFlag(getMessage(IC.ENEMY_EC, loc));
                                break;
                            }
                            if (loc.equals(neutralECs[i])) {
                                neutralECs[i] = null;
                                removeNeutralRelevantFlag(getMessage(IC.NEUTRAL_EC, loc));
                                //removeRelevantFlag(getMessage(IC.NEUTRAL_EC, loc, neutralInf[i]));
                                neutralInf[i] = -1;
                                neutralCooldown[i] = 0;
                                break;
                            }
                        }
                        minInd = -1;
                        seen = false;
                        for (int i = 11; i >= 0; i--) {
                            if (friendECs[i] == null) {
                                minInd = i;
                            } else if (friendECs[i].equals(r.getLocation())) {
                                seen = true;
                                break;
                            }
                        }
                        if (minInd != -1 && !seen) {
                            friendECs[minInd] = r.getLocation();
                            Debug.p("FRIENDLY: Adding to signal queue");
                            signalQueue.add(getMessage(IC.FRIEND_EC, loc));
                            addRelevantFlag(getMessage(IC.FRIEND_EC, loc));
                        }
                    } else if (r.getTeam() == team.opponent()) {
                        for (int i = 0; i < 12; i++) {
                            if (loc.equals(friendECs[i])) {
                                friendECs[i] = null;
                                removeRelevantFlag(getMessage(IC.FRIEND_EC, loc));
                                break;
                            }
                            if (loc.equals(neutralECs[i])) {
                                neutralECs[i] = null;
                                removeNeutralRelevantFlag(getMessage(IC.NEUTRAL_EC, loc));
                                //removeRelevantFlag(getMessage(IC.NEUTRAL_EC, loc, neutralInf[i]));
                                neutralInf[i] = -1;
                                neutralCooldown[i] = 0;
                                break;
                            }
                        }
                        int minInd = -1;
                        boolean seen = false;
                        for (int i = 11; i >= 0; i--) {
                            if (enemyECs[i] == null) {
                                minInd = i;
                            } else if (enemyECs[i].equals(loc)) {
                                seen = true;
                                break;
                            }
                        }
                        if (minInd != -1 && !seen) {
                            enemyECs[minInd] = r.getLocation();
                            Debug.p("ENEMY: Adding to signal queue");
                            signalQueue.add(getMessage(IC.ENEMY_EC, loc));
                            addRelevantFlag(getMessage(IC.ENEMY_EC, loc));
                        }
                    } else {
                        int minInd = -1;
                        boolean seen = false;
                        for (int i = 11; i >= 0; i--) {
                            if (neutralECs[i] == null) {
                                minInd = i;
                            } else if (neutralECs[i].equals(r.getLocation())) {
                                seen = true;
                                break;
                            }
                        }
                        if (minInd != -1 && !seen) {
                            neutralECs[minInd] = r.getLocation();
                            neutralInf[minInd] = Math.min(r.getInfluence()/70, 7);
                            Debug.p("NEUTRAL: Adding to signal queue");
                            signalQueue.add(getMessage(IC.NEUTRAL_EC, loc, neutralInf[minInd]));
                            addRelevantFlag(getMessage(IC.NEUTRAL_EC, loc, neutralInf[minInd]));
                        }
                    }
                }
                if (rc.getType() != RobotType.ENLIGHTENMENT_CENTER && r.getType() == RobotType.MUCKRAKER && r.getTeam() == team.opponent()) {
                    if (rc.getType() == RobotType.SLANDERER)
                        signalQueue.add(getMessage(IC.MUCKRAKER_HELP, r.getLocation()));
                    else if (guard) {
                        // if you're near a slanderer
                        signalQueue.add(getMessage(IC.MUCKRAKER, r.getLocation()));
                    }
                }
                // if you're a muckraker, check for units
                if (rc.getType() == RobotType.MUCKRAKER && !foundSlanderer) {
                    if (r.getType() == RobotType.SLANDERER && r.getTeam() == team.opponent()) {
                        foundSlanderer = true;
                        signalQueue.add(getMessage(IC.SLANDERER, r.getLocation()));
                    }
                }
            }
            // check for possible symmetry locations
            for (int i = 0; i < ECSize; i++) {
                if (foundECs[i] != 0 && rc.canSenseLocation(possibleECs[i])) {
                    RobotInfo r = rc.senseRobotAtLocation(possibleECs[i]);
                    if (r == null || r.getType() != RobotType.ENLIGHTENMENT_CENTER) {
                        if (foundECs[i] == 1 && vert) {
                            vert = false;
                            for (int a = 0; a < ECSize; a++) {
                                if (foundECs[a] == 1) {
                                    foundECs[a] = 0;
                                }
                            }
                            signalQueue.add(getMessage(IC.NO_SYM, 1));
                        }
                        if (foundECs[i] == 2 && horz) {
                            horz = false;
                            for (int a = 0; a < ECSize; a++) {
                                if (foundECs[a] == 2) {
                                    foundECs[a] = 0;
                                }
                            }
                            signalQueue.add(getMessage(IC.NO_SYM, 2));
                        }
                        if (foundECs[i] == 3 && diag) {
                            diag = false;
                            for (int a = 0; a < ECSize; a++) {
                                if (foundECs[a] == 3) {
                                    foundECs[a] = 0;
                                }
                            }
                            signalQueue.add(getMessage(IC.NO_SYM, 3));
                        }
                    }
                    foundECs[i] = 0;
                }
            }
        }
    }

    // get information from flags
    public void getInfo() throws GameActionException {
        // first get the flags from ECs
        for (int i = 0; i < 12; i++) {
            if (ECIds[i] != 0) {
                if (rc.canGetFlag(ECIds[i])) {
                    tempID = ECIds[i];
                    processFlag(rc.getFlag(ECIds[i]));
                }
            }
        }
        // get the flags from other nearby units
        for (RobotInfo r : robots) {
            if (r.getTeam() == team && rc.canGetFlag(r.getID())) {
                processingRobot = r;
                tempID = r.getID();
                processFlag(rc.getFlag(r.getID()));
            }
        }
    }


    // todo: allow ec switching sides
    // process the information gained from flag
    public void processFlag(int flag) {
        IC cat = getCat(flag);
        if (flag % (1 << 22) == 0 || cat == null) return;
        MapLocation coord = getCoord(flag);
        int ID = getID(flag);
        Debug.p("Received from: " + tempID);
        Debug.p("Signal type: " + cat.toString());
        Debug.p("Signal Coords: " + coord.toString());
        Debug.p("Signal ID: " + ID);
        int minInd;
        boolean seen;
        switch (cat) {
            case EDGE_N:
                if (!edges[0] && !explored) {
                    edges[0] = true;
                    maxY = ID;
                    Debug.p("updated "+0+"th edge");
                    addRelevantFlag(getMessage(IC.EDGE_N, maxY));
                    if (mapGenerated) {
                        // rule out some spots
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                if (mapSpots[i][j].y > maxY) visited[i][j] = true;
                            }
                        }
                    }
                }
                break;
            case EDGE_E:
                if (!edges[1] && !explored) {
                    edges[1] = true;
                    maxX = ID;
                    Debug.p("updated "+1+"st edge");
                    addRelevantFlag(getMessage(IC.EDGE_E, maxX));
                    if (mapGenerated) {
                        // rule out some spots
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                if (mapSpots[i][j].x > maxX) visited[i][j] = true;
                            }
                        }
                    }
                }
                break;
            case EDGE_S:
                if (!edges[2] && !explored) {
                    edges[2] = true;
                    minY = ID;
                    Debug.p("updated "+2+"nd edge");
                    addRelevantFlag(getMessage(IC.EDGE_S, minY));
                    if (mapGenerated) {
                        // rule out some spots
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                if (mapSpots[i][j].y < minY) visited[i][j] = true;
                            }
                        }
                    }
                }
                break;
            case EDGE_W:
                if (!edges[3] && !explored) {
                    edges[3] = true;
                    minX = ID;
                    Debug.p("updated "+3+"rd edge");
                    addRelevantFlag(getMessage(IC.EDGE_W, minX));
                    if (mapGenerated) {
                        // rule out some spots
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                if (mapSpots[i][j].x < minX) visited[i][j] = true;
                            }
                        }
                    }
                }
                break;
            case MAP_CORNER:
                if (!mapGenerated && !explored) {
                    mapType = ID;
                    if (mapType == 0 && edges[0] && edges[1]) {
                        mapGenerated = true;
                        Debug.p("Generating map on NE corner");
                        // NE corner
                        int initX = maxX-3;
                        int initY = maxY-3;
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                visited[i][j] = false;
                                mapSpots[i][j] = new MapLocation(initX-8*(7-i), initY-8*(7-j));
                            }
                        }
                        addRelevantFlag(getMessage(IC.MAP_CORNER, 0));
                    }
                    if (mapType == 1 && edges[1] && edges[2]) {
                        mapGenerated = true;
                        Debug.p("Generating map on SE corner");
                        // SE corner
                        int initX = maxX-3;
                        int initY = minY+4;
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                visited[i][j] = false;
                                mapSpots[i][j] = new MapLocation(initX-8*(7-i), initY+8*j);
                            }
                        }
                        addRelevantFlag(getMessage(IC.MAP_CORNER, 1));
                    }
                    if (mapType == 2 && edges[2] && edges[3]) {
                        mapGenerated = true;
                        Debug.p("Generating map on SW corner");
                        // SW corner
                        int initX = minX+4;
                        int initY = minY+4;
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                visited[i][j] = false;
                                mapSpots[i][j] = new MapLocation(initX+8*i, initY+8*j);
                            }
                        }
                        addRelevantFlag(getMessage(IC.MAP_CORNER, 2));
                    }
                    if (mapType == 3 && edges[3] && edges[0]) {
                        mapGenerated = true;
                        Debug.p("Generating map on NW corner");
                        // NW corner
                        int initX = minX+4;
                        int initY = maxY-3;
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                visited[i][j] = false;
                                mapSpots[i][j] = new MapLocation(initX+8*i, initY-8*(7-j));
                            }
                        }
                        addRelevantFlag(getMessage(IC.MAP_CORNER, 3));
                    }
                    if (mapGenerated) {
                        for (int i = 7; i >= 0; i--) {
                            for (int j = 7; j >= 0; j--) {
                                MapLocation loc = mapSpots[i][j];
                                if (loc.x > maxX || loc.y > maxY || loc.x < minX || loc.y < minY) visited[i][j] = true;
                            }
                        }
                    }
                }
                break;
            case MAP_NE:
                if (!explored) {
                    for (int i = 4; i < 8; i++) {
                        for (int j = 4; j < 8; j++) {
                            if (!visited[i][j] && (ID & (1 << ((i - 4) * 4 + j - 4))) != 0) {
                                visited[i][j] = true;
                                updateNE = true;
                            }
                        }
                    }
                }
                break;
            case MAP_NW:
                if (!explored) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 4; j < 8; j++) {
                            if (!visited[i][j] && (ID & (1 << (i * 4 + j - 4))) != 0) {
                                visited[i][j] = true;
                                updateNW = true;
                            }
                        }
                    }
                }
                break;
            case MAP_SE:
                if (!explored) {
                    for (int i = 4; i < 8; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (!visited[i][j] && (ID & (1 << ((i - 4) * 4 + j))) != 0) {
                                visited[i][j] = true;
                                updateSE = true;
                            }
                        }
                    }
                }
                break;
            case MAP_SW:
                if (!explored) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (!visited[i][j] && (ID & (1 << (i * 4 + j))) != 0) {
                                visited[i][j] = true;
                                updateSW = true;
                            }
                        }
                    }
                }
                break;
            case ENEMY_EC:
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (totalECs[i] == null) minInd = i;
                    else if (coord.equals(totalECs[i]))  {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    totalECs[minInd] = coord;
                    if (corners) {
                        for (int i = 0; i < 36; i++) {
                            if (coord.equals(possibleECs[i])) {
                                foundECs[i] = 0;
                                seen = true;
                                break;
                            }
                        }
                        if (!seen) symmetry(coord);
                    }
                }
                for (int i = 0; i < 12; i++) {
                    if (coord.equals(friendECs[i])) {
                        friendECs[i] = null;
                        removeRelevantFlag(getMessage(IC.FRIEND_EC, coord));
                        break;
                    }
                    if (coord.equals(neutralECs[i])) {
                        neutralECs[i] = null;
                        removeNeutralRelevantFlag(getMessage(IC.NEUTRAL_EC, coord));
                        //removeRelevantFlag(getMessage(IC.NEUTRAL_EC, coord, neutralInf[i]));
                        neutralInf[i] = -1;
                        neutralCooldown[i] = 0;
                        break;
                    }
                }
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (enemyECs[i] == null) {
                        minInd = i;
                    } else if (enemyECs[i].equals(coord)) {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    enemyECs[minInd] = coord;
                    addRelevantFlag(getMessage(IC.ENEMY_EC, coord));
                }

                break;
            case FRIEND_EC:
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (totalECs[i] == null) minInd = i;
                    else if (coord.equals(totalECs[i]))  {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    totalECs[minInd] = coord;
                    if (corners) {
                        for (int i = 0; i < 36; i++) {
                            if (coord.equals(possibleECs[i])) {
                                foundECs[i] = 0;
                                seen = true;
                                break;
                            }
                        }
                        if (!seen) symmetry(coord);
                    }
                }
                for (int i = 0; i < 12; i++) {
                    if (coord.equals(enemyECs[i])) {
                        enemyECs[i] = null;
                        removeRelevantFlag(getMessage(IC.ENEMY_EC, coord));
                        break;
                    }
                    if (coord.equals(neutralECs[i])) {
                        neutralECs[i] = null;
                        removeNeutralRelevantFlag(getMessage(IC.NEUTRAL_EC, coord));
                        //removeRelevantFlag(getMessage(IC.NEUTRAL_EC, coord, neutralInf[i]));
                        neutralInf[i] = -1;
                        neutralCooldown[i] = 0;
                        break;
                    }
                }
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (friendECs[i] == null) {
                        minInd = i;
                    } else if (friendECs[i].equals(coord)) {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    friendECs[minInd] = coord;
                    addRelevantFlag(getMessage(IC.FRIEND_EC, coord));
                }
                break;
            case NEUTRAL_EC:
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (totalECs[i] == null) minInd = i;
                    else if (coord.equals(totalECs[i]))  {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    totalECs[minInd] = coord;
                    if (corners) {
                        for (int i = 0; i < 36; i++) {
                            if (coord.equals(possibleECs[i])) {
                                foundECs[i] = 0;
                                seen = true;
                                break;
                            }
                        }
                        if (!seen) symmetry(coord);
                    }
                }
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (neutralECs[i] == null) {
                        minInd = i;
                    }
                    else if (neutralECs[i].equals(coord)) {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    neutralECs[minInd] = coord;
                    Debug.p("Getting inf: " + getInf(flag));
                    neutralInf[minInd] = getInf(flag);
                    addRelevantFlag(getMessage(IC.NEUTRAL_EC, coord, neutralInf[minInd]));
                }
                break;
            case EC_ID:
                minInd = -1;
                seen = false;
                for (int i = 11; i >= 0; i--) {
                    if (ECIds[i] == 0) {
                        minInd = i;
                    }
                    if (ECIds[i] == ID) {
                        seen = true;
                        break;
                    }
                }
                if (minInd != -1 && !seen) {
                    ECIds[minInd] = ID;
                    addRelevantFlag(getMessage(IC.EC_ID, ID));
                }
                break;
            case ATTACK:
                if (rc.getType() == RobotType.ENLIGHTENMENT_CENTER) break;
                moveAway = true;
                attacker = processingRobot.getLocation();
                attackDist = attacker.distanceSquaredTo(coord);
                break;
            case MUCKRAKER:
                if (rc.getType() == RobotType.SLANDERER) {
                    runAway = true;
                    danger = coord;
                }
                break;
            case MUCKRAKER_HELP:
                if (rc.getType() == RobotType.POLITICIAN &&
                    rc.getLocation().distanceSquaredTo(coord) <= RobotType.POLITICIAN.sensorRadiusSquared) {
                    defendSlanderer = true;
                    enemyMuck = coord;
                }
                if (rc.getType() == RobotType.SLANDERER) {
                    runAway = true;
                    danger = coord;
                }
                break;
            case SLANDERER:
                boolean inserted = false;
                for (int i = 0; i < 6; i++) {
                    if (slandererLoc[i] == null || slandererLoc[i].isWithinDistanceSquared(coord, 9)) {
                        slandererLoc[i] = coord;
                        staleness[i] = staleCooldown;
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    int minStale = 1000;
                    int minStaleInd = -1;
                    for (int i = 0; i < 6; i++) {
                        if (staleness[i] < minStale) {
                            minStale = staleness[i];
                            minStaleInd = i;
                        }
                    }
                    slandererLoc[minStaleInd] = coord;
                    staleness[minStaleInd] = staleCooldown;
                }
                break;
            case RESET:
                explored = true;
                break;
            case NO_SYM:
                if (ID == 1 && vert) {
                    vert = false;
                    for (int i = 0; i < ECSize; i++) {
                        if (foundECs[i] == 1) {
                            foundECs[i] = 0;
                        }
                    }
                    addRelevantFlag(getMessage(IC.NO_SYM, ID));
                }
                if (ID == 2 && horz) {
                    horz = false;
                    for (int i = 0; i < ECSize; i++) {
                        if (foundECs[i] == 2) {
                            foundECs[i] = 0;
                        }
                    }
                    addRelevantFlag(getMessage(IC.NO_SYM, ID));
                }
                if (ID == 3 && diag) {
                    diag = false;
                    for (int i = 0; i < ECSize; i++) {
                        if (foundECs[i] == 3) {
                            foundECs[i] = 0;
                        }
                    }
                    addRelevantFlag(getMessage(IC.NO_SYM, ID));
                }
                break;
        }
    }


    public void displaySignal() throws GameActionException {
        if (!signalQueue.isEmpty()) {
            int flag = signalQueue.poll();
            rc.setFlag(flag);
        } else {
            rc.setFlag(typeInt(rc.getType()));
        }
    }

    public static void addRelevantFlag(int flag) {
        for (int i = 0; i < 40; i++) {
            if (relevantFlags[i] == 0) {
                relevantFlags[i] = flag;
                relevantSize++;
                break;
            }
        }
        relevantDiscovery = true;
    }

    public static void removeRelevantFlag(int flag) {
        for (int i = 0; i < 40; i++) {
            if (relevantFlags[i] == flag) {
                relevantFlags[i] = 0;
                relevantSize--;
                break;
            }
        }
    }

    public static void removeNeutralRelevantFlag(int flag) {
        for (int i = 0; i < 40; i++) {
            if (getCat(relevantFlags[i]) == IC.NEUTRAL_EC && getCoord(relevantFlags[i]).equals(getCoord(flag))) {
                relevantFlags[i] = 0;
                relevantSize--;
                break;
            }
        }
    }

    // reset all the variables
    public static void resetVariables() {
        moveAway = false;
        defendSlanderer = false;
        runAway = false;
        updateNE = false;
        updateNW = false;
        updateSE = false;
        updateSW = false;
        relevantDiscovery = false;
    }

    // figure out possible ec locations with symmetry
    // assumes that we know all the corners
    public static void symmetry(MapLocation loc) {
        Debug.p("Calling symmetry on: " + loc);
        for (int j = 0; j < ECSize; j++) {
            if (possibleECs[j].equals(loc)) return;
        }
        if (vert) {
            MapLocation temp = new MapLocation(minX+maxX-loc.x, loc.y);
            boolean seen = false;
            for (int i = 0; i < 12; i++) {
                if (temp.equals(totalECs[i])) {
                    seen = true;
                    break;
                }
            }
            for (int i = 0; i < ECSize; i++) {
                if (temp.equals(possibleECs[i])) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                possibleECs[ECSize] = new MapLocation(minX + maxX - loc.x, loc.y);
                foundECs[ECSize] = 1;
                ECSize++;
            }
        }
        if (horz) {
            MapLocation temp = new MapLocation(loc.x, minY+maxY-loc.y);
            boolean seen = false;
            for (int i = 0; i < 12; i++) {
                if (temp.equals(totalECs[i])) {
                    seen = true;
                    break;
                }
            }
            for (int i = 0; i < ECSize; i++) {
                if (temp.equals(possibleECs[i])) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                possibleECs[ECSize] = new MapLocation(loc.x, minY + maxY - loc.y);
                foundECs[ECSize] = 2;
                ECSize++;
            }
        }
        if (diag) {
            MapLocation temp = new MapLocation(minX+maxX-loc.x, minY+maxY-loc.y);
            boolean seen = false;
            for (int i = 0; i < 12; i++) {
                if (temp.equals(totalECs[i])) {
                    seen = true;
                    break;
                }
            }
            for (int i = 0; i < ECSize; i++) {
                if (temp.equals(possibleECs[i])) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                possibleECs[ECSize] = new MapLocation(minX + maxX - loc.x, minY + maxY - loc.y);
                foundECs[ECSize] = 3;
                ECSize++;
            }
        }
    }
}