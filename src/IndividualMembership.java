import java.util.Date;

public class IndividualMembership extends Membership {
    private final double membershipFee = 36;
    private PayType payType;

    public IndividualMembership(long memberId, String firstName, String lastName, Date birthday, Gender gender, String address, String telephone, PayType payType, String healthCondition, String allergyInformation) {
        super(memberId, firstName, lastName, birthday, gender, address, telephone, payType, healthCondition, allergyInformation);
        this.payType = payType;
    }

    public double getMembershipFee() {
        return membershipFee;
    }

    @Override
    public PayType getPayType() {
        return payType;
    }

    @Override
    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public double getMembershipFeeAmount() {
        return super.getMembershipFeeAmount(payType, membershipFee);
    }
}
