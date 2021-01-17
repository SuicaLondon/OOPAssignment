import javax.swing.*;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class ClubMembership {
    public static void main(String[] args) {
        MembershipManagement membershipManagement = new MembershipManagement();

        JFrame listFrame = new MembershipsListFrame(membershipManagement);
        listFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        listFrame.setVisible(true);
    }
}
