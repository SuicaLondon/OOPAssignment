import java.util.Calendar;
import java.util.Date;

public class FamilyMembership extends Membership {
    private final double membershipFee = 60;
    private PayType payType;
    private static final int limitedAge = 18;

    public FamilyMembership(long memberId, String firstName, String lastName, Date birthday, Gender gender, String address, String telephone, PayType payType, String healthCondition, String allergyInformation) {
        super(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
        this.payType = payType;
    }

    public double getMembershipFee() {
        return membershipFee;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public int getLimitedAge() {
        return limitedAge;
    }

    public static boolean checkAgeAvailable(Date birthday) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), 9, 1);
        return DateConverter.getYearGap(birthday, cal.getTime()) >= limitedAge;
    }

    public double getMembershipFeeAmount() {
        return super.getMembershipFeeAmount(payType, membershipFee);
    }
}
