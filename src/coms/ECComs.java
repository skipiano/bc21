package coms;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import static coms.Robot.*;

public class ECComs extends Coms {

    private static int IDcheck = 10000;
    private static boolean allSearched = false;

    public ECComs(RobotController r) {
        super(r);
        ECLoc.put(rc.getID(), rc.getLocation());
        ECIds[0] = rc.getID();
        ECs[0] = rc.getLocation();
    }

    // can perform computation through multiple turns, but needs to be called once per turn until it is all done
    // returns whether the looping through flags process has finished
    public boolean loopFlags() throws GameActionException {
        while (Clock.getBytecodesLeft() >= 100 && IDcheck <= 14096) {
            if (rc.canGetFlag(IDcheck)) {
                int flag = rc.getFlag(IDcheck);
                if (getCat(flag) == InformationCategory.EC_ID) {
                    // found an EC!
                    int ID = getID(flag);
                    boolean knownID = false;
                    for (int i = 0; i < 12; i++) {
                        if (ECIds[i] == ID) {
                            knownID = true;
                            break;
                        }
                    }
                    if (!knownID) {
                        for (int i = 0; i < 12; i++) {
                            if (ECIds[i] == 0) {
                                ECIds[i] = ID;
                                break;
                            }
                        }
                    }
                    signalQueue.add(getMessage(InformationCategory.EC_ID, ID));
                }
            }
            IDcheck++;
        }
        if (IDcheck == 14097) {
            allSearched = true;
        }
        return allSearched;
    }


    public void getInfo() {

    }
}
