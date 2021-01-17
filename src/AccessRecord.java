import java.util.Date;

public class AccessRecord {
    private VisitType visitType;
    private long memberId;
    private Facility.FacilityType facilityType;
    private Date accessTime;

    enum VisitType {
        INDIVIDUAL, FAMILY, CHILDREN, VISITOR
    }

    public AccessRecord(VisitType visitType, long memberId, Facility.FacilityType facilityType) {
        this.visitType = visitType;
        this.memberId = memberId;
        this.facilityType = facilityType;
        // create access time immediately
        this.accessTime = new Date();
    }

    public VisitType getVisitType() {
        return visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public Facility.FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(Facility.FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }
}
