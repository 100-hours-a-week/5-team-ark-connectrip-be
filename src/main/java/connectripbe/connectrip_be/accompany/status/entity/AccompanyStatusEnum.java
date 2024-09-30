package connectripbe.connectrip_be.accompany.status.entity;

import lombok.Getter;

@Getter
public enum AccompanyStatusEnum {
    PROGRESSING("진행 중"),
    CLOSED("모집 마감"),
    FINISHED("동행 종료");

    private final String displayName;

    AccompanyStatusEnum(String displayName) {
        this.displayName = displayName;
    }
}
