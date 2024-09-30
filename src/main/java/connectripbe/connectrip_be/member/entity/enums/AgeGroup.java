package connectripbe.connectrip_be.member.entity.enums;

import lombok.Getter;

@Getter
public enum AgeGroup {
    TEENAGER("10대"),
    SIXTIES_AND_ABOVE("60대 이상");

    private final String label;

    AgeGroup(String label) {
        this.label = label;
    }

    public static String fromAge(int age) {
        if (age < 20) {
            return TEENAGER.getLabel();
        }
        if (age >= 60) {
            return SIXTIES_AND_ABOVE.getLabel();
        }
        int decade = (age / 10) * 10;
        return decade + "대";
    }
}
