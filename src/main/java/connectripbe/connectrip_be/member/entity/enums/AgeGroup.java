package connectripbe.connectrip_be.member.entity.enums;

public enum AgeGroup {
    TEENAGER("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    FIFTIES("50대"),
    SIXTIES_AND_ABOVE("60대 이상");

    private final String label;

    AgeGroup(String label) {
        this.label = label;
    }

    public static AgeGroup fromAge(int age) {
        if (age < 20) {
            return TEENAGER;
        } else if (age < 30) {
            return TWENTIES;
        } else if (age < 40) {
            return THIRTIES;
        } else if (age < 50) {
            return FORTIES;
        } else if (age < 60) {
            return FIFTIES;
        } else {
            return SIXTIES_AND_ABOVE;
        }
    }

    public String getLabel() {
        return label;
    }
}
