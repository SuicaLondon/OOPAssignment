import java.util.Calendar;
import java.util.Date;

public class Membership implements MembershipFee, AgeLimited {
    private long memberId;

    private String firstName;

    private String lastName;
    private Date birthday; // can be null
    private Gender gender;
    private String address;
    private String telephone;
    private String healthCondition;
    private String allergyInformation;
    private PayType payType;
    private Date startDate;

    private Date endDate;

    public Membership(long memberId, String firstName, String lastName, Date birthday, Gender gender, String address, String telephone, PayType payType, String healthCondition, String allergyInformation) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.telephone = telephone;
        this.payType = payType;
        this.healthCondition = healthCondition;
        this.allergyInformation = allergyInformation;
        if (payType != null) {
            startDate = new Date();
            endDate = calculateEndDate(startDate, payType);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public String getAllergyInformation() {
        return allergyInformation;
    }

    public void setAllergyInformation(String allergyInformation) {
        this.allergyInformation = allergyInformation;
    }

    public static boolean checkAgeAvailable(Date birthday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 9, 1);
        return DateConverter.getYearGap(birthday, cal.getTime()) >= limitedAge;
    }

    /**
     * Calculate the amount of membership fee (original data will return 0)
     * @param payType
     * @param membershipFee
     * @return
     */
    @Override
    public double getMembershipFeeAmount(PayType payType, double membershipFee) {
        if(payType == null){
            return 0;
        }
        if (payType.equals(PayType.MONTHLY)) {
            return membershipFee;
        } else if (payType.equals(PayType.QUARTERLY)) {
            return membershipFee * 3;
        } else if (payType.equals(PayType.YEARLY)) {
            return membershipFee * 12;
        } else {
            return 0;
        }
    }

    /**
     * Get the membership due date object
     * @param startDate
     * @param payType
     * @return
     */
    @Override
    public Date calculateEndDate(Date startDate, PayType payType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        if (payType.equals(PayType.MONTHLY)) {
            cal.add(Calendar.MONTH, 1);
            return cal.getTime();
        } else if (payType.equals(PayType.QUARTERLY)) {
            cal.add(Calendar.MONTH, 3);
            return cal.getTime();
        } else if (payType.equals(PayType.YEARLY)) {
            cal.add(Calendar.MONTH, 12);
            return cal.getTime();
        } else {
            return null;
        }
    }

}
