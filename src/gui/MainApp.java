package app;

import shared.MatchPool;
import threads.MatchMakerThread;
import threads.UserRole;
import threads.UserThread;

public class MainApp {

    public static void main(String[] args) {

        MatchPool pool = new MatchPool();
        MatchMakerThread matchMaker = new MatchMakerThread(pool);
        matchMaker.start();

        new UserThread("Isko-UPM", UserRole.STUDENT, pool).start();
        new UserThread("Iska-UPD", UserRole.STUDENT, pool).start();
        new UserThread("Prof-A", UserRole.PROFESSOR, pool).start();
        new UserThread("Prof-B", UserRole.PROFESSOR, pool).start();
    }
}
