package finals.utils;

import static finals.Robot.debugOn;

public class Debug {

    public static void p(Object msg) {
        if (debugOn) System.out.println(msg);
    }
}
