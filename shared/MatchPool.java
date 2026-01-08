package shared;

import threads.UserRole;
import threads.UserThread;

import java.util.ArrayList;
import java.util.List;

public class MatchPool {

    private final List<UserThread> students = new ArrayList<>();
    private final List<UserThread> professors = new ArrayList<>();

    // PRODUCER: UserThread
    public synchronized void enterPool(UserThread user) {
        if (user.getRole() == UserRole.STUDENT) {
            students.add(user);
        } else {
            professors.add(user);
        }

        System.out.println("[POOL] " + user.getName() + " entered pool.");
        notifyAll(); // wake MatchMaker if sleeping
    }

    // CONSUMER: MatchMakerThread
    public synchronized UserThread[] getMatchIfAvailable() {
        if (students.size() >= 2) {
            return new UserThread[] {
                    students.remove(0),
                    students.remove(0)
            };
        }

        if (professors.size() >= 2) {
            return new UserThread[] {
                    professors.remove(0),
                    professors.remove(0)
            };
        }

        return null;
    }
}
