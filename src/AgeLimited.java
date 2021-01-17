import java.util.Calendar;
import java.util.Date;

public interface AgeLimited {
    int limitedAge = 12;

    static boolean checkAgeAvailable(Date birthday) {
        return false;
    }
}
