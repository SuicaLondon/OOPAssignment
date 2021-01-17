public class Facility {
    private static double SWIMMING_FEE = 3;
    private static double GYM_FEE = 4;
    private static double YOGA_FEE = 5;
    private static double AEROBICS_FEE = 6;
    enum FacilityType {
        SWIMMING_POOL, GYM, YOGA, AEROBICS;


        @Override
        public String toString() {
            switch (this){
                case SWIMMING_POOL:
                    return "Swimming pool";
                case GYM:
                    return "Gym";
                case YOGA:
                    return "Yoga";
                case AEROBICS:
                    return "Aerobics";
                default:
                    return "";
            }
        }
    }

    public static double getFacilityFee(FacilityType type) {
        switch (type) {
            case GYM:
                return GYM_FEE;
            case YOGA:
                return YOGA_FEE;
            case AEROBICS:
                return AEROBICS_FEE;
            case SWIMMING_POOL:
                return SWIMMING_FEE;
            default:
                return 0;
        }
    }

    /**
     * decode the String to FacilityType
     * @param typeString
     * @return
     */
    public static FacilityType getFacilityType(String typeString) {
        switch (typeString.toLowerCase()){
            case "swim":
            case "swimming":
            case "swimming pool":
                return FacilityType.SWIMMING_POOL;
            case "gym":
                return FacilityType.GYM;
            case "yoga":
                return FacilityType.YOGA;
            case "aerobics":
                return FacilityType.AEROBICS;
            default:
                return null;
        }
    }
}
