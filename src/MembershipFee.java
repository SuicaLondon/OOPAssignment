import java.util.Date;

public interface MembershipFee {
    double membershipFee = 0;
    PayType payType = null;
    Date startDate = null;
    Date endDate = null;

    double getMembershipFeeAmount(PayType payType, double membershipFee);

    public Date calculateEndDate(Date startDate, PayType payType);
}
