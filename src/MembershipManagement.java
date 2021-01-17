

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MembershipManagement {
    private ArrayList<Membership> memberships;
    private static ArrayList<AccessRecord> accessRecords = new ArrayList<>();
    private static ArrayList<AccessRecord> visitorAccessRecords = new ArrayList<>();

    public static ArrayList<AccessRecord> getAccessRecords() {
        return accessRecords;
    }

    /**
     * get all access record data with String array
     * @return
     */
    public static String[][] getAccessRecordsStringsList() {
        String[][] strings = new String[accessRecords.size()][4];
        for (int i = 0; i < accessRecords.size(); i++) {
            AccessRecord accessRecord = accessRecords.get(i);
            strings[i][0] = accessRecord.getMemberId() != 0 ? accessRecord.getMemberId() + "" : "";
            strings[i][1] = accessRecord.getFacilityType().toString();
            strings[i][2] = accessRecord.getVisitType().toString();
            strings[i][3] = DateConverter.dateToString(accessRecord.getAccessTime());
        }
        return strings;
    }

    public static void setAccessRecords(ArrayList<AccessRecord> accessRecords) {
        MembershipManagement.accessRecords = accessRecords;
    }

    public static ArrayList<AccessRecord> getVisitorAccessRecords() {
        return visitorAccessRecords;
    }
    /**
     * get all visitor access record data with String array
     * @return
     */
    public static String[][] getVisitorAccessRecordsStringsList() {
        String[][] strings = new String[visitorAccessRecords.size()][3];
        for (int i = 0; i < visitorAccessRecords.size(); i++) {
            AccessRecord accessRecord = visitorAccessRecords.get(i);
            strings[i][0] = accessRecord.getFacilityType().toString();
            strings[i][1] = accessRecord.getVisitType().toString();
            strings[i][2] = DateConverter.dateToString(accessRecord.getAccessTime());
        }
        return strings;
    }

    public static void setVisitorAccessRecords(ArrayList<AccessRecord> visitorAccessRecords) {
        MembershipManagement.visitorAccessRecords = visitorAccessRecords;
    }

    private static double ticketIncome = 0; // total

    public MembershipManagement() {
        this.memberships = new ArrayList<>();
    }

    /**
     * import CSV and save in the list
     */
    public void importCSV(String path) {
        try {
            ArrayList<String[]> usersInformation = CSVManager.readCSV(path);
            for (String[] userInformation : usersInformation) {
                long memberId = (userInformation[0].equals("")) ? createMemberId() : Long.parseLong(userInformation[0]);
                String lastName = userInformation[1];
                String firstName = userInformation[2];
                // check repeat
                if (checkRepeat(firstName, lastName)) continue;
                Date birthday = null;
                String address = "", telephone = "";
                // Original data may not have so many column of data, so it must check one by one
                if (userInformation.length > 3 && !userInformation[3].equals("")) {
                    birthday = DateConverter.stringToDate(userInformation[3], new SimpleDateFormat("dd/MM/yy"));
                }
                Gender gender = null;
                if (userInformation.length > 4) {
                    if (userInformation[4].equals("male")) {
                        gender = Gender.MALE;
                    } else if (userInformation[4].equals("female")) {
                        gender = Gender.FEMALE;
                    }
                }
                if (userInformation.length > 5) {
                    address = userInformation[5];
                }
                if (userInformation.length > 6) {
                    telephone = userInformation[6];
                }
                String healthCondition = "", allergyInformation = "";
                if (userInformation.length > 7) {
                    healthCondition = userInformation[7];
                }
                if (userInformation.length > 8) {
                    allergyInformation = userInformation[8];
                }
                PayType payType = null;
                if (userInformation.length > 9) {
                    switch (userInformation[9]) {
                        case "MONTHLY":
                            payType = PayType.MONTHLY;
                            break;
                        case "QUARTERLY":
                            payType = PayType.QUARTERLY;
                            break;
                        case "YEARLY":
                            payType = PayType.YEARLY;
                            break;
                        case "TIMES":
                            payType = PayType.TIMES;
                            break;
                    }
                }
                Date startTime = null;
                Date endTime = null;
                AccessRecord.VisitType visitType = AccessRecord.VisitType.INDIVIDUAL;
                if (userInformation.length > 10 && !userInformation[10].isEmpty()) {
                    startTime = DateConverter.stringToDate(userInformation[10]);
                }
                if (userInformation.length > 11 && !userInformation[11].isEmpty()) {
                    endTime = DateConverter.stringToDate(userInformation[11]);
                }
                if (userInformation.length > 12) {
                    switch (userInformation[12]) {
                        case "individual":
                            initIndividualMember(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation, startTime, endTime);
                            break;
                        case "family":
                            initFamilyMember(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation, startTime, endTime);
                            break;
                    }
                } else {
                    // not type mean original data (family type is new feature)
                    initIndividualMember(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation, startTime, endTime);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * export to the path
     * @param path
     */
    public void exportCSV(String path) {
        ArrayList<String> dataList = new ArrayList<>();
        for (String[] data : getMemberShipsByStringArray()) {
            for (int i = 0; i < data.length; i++) {
                if (data[i].contains(",")) {
                    data[i] = "\"" + data[i] + "\"";
                }
            }
            dataList.add(String.join(",", data));
        }
        if (dataList.size() > 0) {
            CSVManager.writeCSV(dataList, path);
        }
    }

    public void initIndividualMember(long memberId, String firstName, String lastName, Date birthday, Gender gender,
                                     String address, String telephone, PayType payType, String healthCondition, String allergyInformation, Date startTime, Date endTime) {
        IndividualMembership individualMembership = new IndividualMembership(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
        individualMembership.setStartDate(startTime);
        individualMembership.setEndDate(endTime);
        memberships.add(individualMembership);
    }

    public void initFamilyMember(long memberId, String firstName, String lastName, Date birthday, Gender gender,
                                 String address, String telephone, PayType payType, String healthCondition, String allergyInformation, Date startTime, Date endTime) {
        FamilyMembership familyMembership = new FamilyMembership(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
        familyMembership.setStartDate(startTime);
        familyMembership.setEndDate(endTime);
        memberships.add(familyMembership);
    }

    /**
     * create random UUID as member id
     * @return
     */
    public long createMemberId() {
        long memberId = UUID.randomUUID().toString().hashCode();
        memberId = memberId > 0 ? memberId : -memberId;
        return memberId;
    }

    public Membership addIndividualMember(String firstName, String lastName, Date birthday, Gender gender, String address, String telephone, PayType payType, String healthCondition, String allergyInformation) {
        if (IndividualMembership.checkAgeAvailable(birthday)) { // check if age is higher than limit
            Membership memberShip = new IndividualMembership(createMemberId(), firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
            this.memberships.add(memberShip);
            return memberShip;
        } else {
            System.out.println("User are too young");
            return null;
        }
    }

    public Membership addFamilyMember(String firstName, String lastName, Date birthday, Gender gender, String address, String telephone, PayType payType, String healthCondition, String allergyInformation) {
        if (FamilyMembership.checkAgeAvailable(birthday)) { // check if age is higher than limit
            Membership memberShip = new FamilyMembership(createMemberId(), firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
            this.memberships.add(memberShip);
            return memberShip;
        } else {
            System.out.println("User are too young");
            return null;
        }
    }

    public void deleteMember(long memberId) {
        memberships.removeIf(memberShip -> memberShip.getMemberId() == memberId);
    }

    public boolean editMember(long memberId, Membership newMember) {
        if (Membership.checkAgeAvailable(newMember.getBirthday())) {
            for (Membership membership : memberships) {
                if (memberId == membership.getMemberId()) {
                    memberships.set(memberships.indexOf(membership), newMember);
                    return true;
                }
            }
        } else {
            System.out.println("User are too young");
        }
        return false;
    }

    public Membership searchMember(long memberId) {
        return memberships.stream().filter(memberShip -> memberShip.getMemberId() == memberId).findAny().orElse(null);
    }

    public boolean individualCheckIn(long memberId, Facility.FacilityType type) {
        if (checkMembershipValid(memberId)) { // if membership is still valid
            AccessRecord accessRecord = new AccessRecord(AccessRecord.VisitType.INDIVIDUAL, memberId, type);
            accessRecords.add(accessRecord);
            return true;
        } else { // not valid outside should handle
            return false;
        }
    }

    public boolean familyCheckIn(long memberId, Facility.FacilityType type) {
        if (checkMembershipValid(memberId)) { // if membership is still valid
            AccessRecord accessRecord = new AccessRecord(AccessRecord.VisitType.FAMILY, memberId, type);
            accessRecords.add(accessRecord);
            return true;
        } else { // not valid outside should handle
            return false;
        }
    }

    public boolean childrenCheckIn(long memberId, Facility.FacilityType type) {
        if (checkMembershipValid(memberId)) { // if membership is still valid
            AccessRecord accessRecord = new AccessRecord(AccessRecord.VisitType.INDIVIDUAL, memberId, type);
            accessRecords.add(accessRecord);
            return true;
        } else { // not valid outside should handle
            return false;
        }
    }

    public boolean visitorCheckIn(Facility.FacilityType type) {
        AccessRecord accessRecord = new AccessRecord(AccessRecord.VisitType.INDIVIDUAL, 0, type);
        accessRecords.add(accessRecord);
        ticketIncome += Facility.getFacilityFee(type);
        if (visitorAccessRecords.size() > 0) { // only record today data
            Date firstDateInList = visitorAccessRecords.get(0).getAccessTime();
            Calendar calendar = DateConverter.getCalendarFromDate(firstDateInList);
            // It record the data every day
            if (calendar.get(Calendar.DAY_OF_YEAR) != DateConverter.getCalendarFromDate(new Date()).get(Calendar.DAY_OF_YEAR)) {
                visitorAccessRecords.clear();
            }
        }
        visitorAccessRecords.add(accessRecord);
        return true;
    }


    public boolean checkRepeat(String firstName, String lastName) {
        for (Membership member : memberships) {
            if ((member.getLastName() + member.getFirstName()).equals(lastName + firstName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkMembershipValid(long memberId) {
        for (Membership membership : memberships) {
            if (membership.getMemberId() == memberId) {
                Date now = new Date();
                if (membership.getEndDate() == null) {
                    // old user dont has this data
                    return true;
                }
                if (membership.getEndDate().getTime() > now.getTime()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public ArrayList<Membership> getMemberships() {
        return memberships;
    }

    /**
     * get String array of membership list (for table)
     * @return
     */
    public String[][] getMemberShipsByStringArray() {
        if (memberships.size() > 0) {
            String[][] memberShipStrings = new String[memberships.size()][5];
            for (int i = 0; i < memberships.size(); i++) {
                Membership membership = memberships.get(i);
                memberShipStrings[i] = getMembershipStringArray(membership);
            }
            return memberShipStrings;
        } else {
            return new String[0][0];
        }
    }

    /**
     * get String array of membership list (for table)
     * @param memberShip
     * @return
     */
    public String[] getMembershipStringArray(Membership memberShip) {
        String[] memberShipStrings = new String[13];
        memberShipStrings[0] = String.valueOf(memberShip.getMemberId());
        memberShipStrings[1] = memberShip.getFirstName();
        memberShipStrings[2] = memberShip.getLastName();
        memberShipStrings[3] = memberShip.getBirthday() != null ? DateConverter.dateToString(memberShip.getBirthday()) : "";
        memberShipStrings[4] = memberShip.getGender() != null ? memberShip.getGender().toString() : "";
        memberShipStrings[5] = memberShip.getAddress();
        memberShipStrings[6] = memberShip.getTelephone();
        memberShipStrings[7] = memberShip.getHealthCondition();
        memberShipStrings[8] = memberShip.getAllergyInformation();
        memberShipStrings[9] = memberShip.getPayType() != null ? memberShip.getPayType().toString() : "";
        memberShipStrings[10] = memberShip.getStartDate() != null ? DateConverter.dateToString(memberShip.getStartDate()) : "";
        memberShipStrings[11] = memberShip.getEndDate() != null ? DateConverter.dateToString(memberShip.getEndDate()) : "";
        memberShipStrings[12] = memberShip instanceof FamilyMembership ? "family" : "individual";
        return memberShipStrings;
    }

    /**
     * check if the inputed date is the same year as today (for calculate income)
     * @param date
     * @return
     */
    public boolean checkSameYear(Date date) {
        Calendar cal = Calendar.getInstance();
        // income is calculated from the start of this year, so we only need check if those years are the same
        return cal.get(Calendar.YEAR) == DateConverter.getCalendarFromDate(date).get(Calendar.YEAR);
    }

    /**
     * Membership fee income
     * @return
     */
    public double getOverallIncomeFromMembershipFee() {
        double overallMembershipFeeIncome = 0;
        for (Membership membership : memberships) {
            if (membership.getStartDate() != null && checkSameYear(membership.getStartDate())) {
                if (membership instanceof IndividualMembership) {
                    overallMembershipFeeIncome += ((IndividualMembership) membership).getMembershipFeeAmount();
                } else if (membership instanceof FamilyMembership) {
                    overallMembershipFeeIncome += ((FamilyMembership) membership).getMembershipFeeAmount();
                }
            }
        }
        return overallMembershipFeeIncome;
    }

    /**
     * ticket fee income (access)
     * @return
     */
    public double getOverallIncomeFromAccessFee() {
        double overallTicketIncome = 0;
        for (AccessRecord visitorAccessRecord : visitorAccessRecords) {
            if (checkSameYear(visitorAccessRecord.getAccessTime())) {
                overallTicketIncome += Facility.getFacilityFee(visitorAccessRecord.getFacilityType());
            }
        }
        return overallTicketIncome;
    }
}
