public enum PayType {
    MONTHLY, QUARTERLY, YEARLY, TIMES;


    @Override
    public String toString() {
        switch (this) {
            case MONTHLY:
                return "MONTHLY";
            case QUARTERLY:
                return "QUARTERLY";
            case YEARLY:
                return "YEARLY";
            case TIMES:
                return "TIMES";
            default:
                return "";
        }
    }
}

