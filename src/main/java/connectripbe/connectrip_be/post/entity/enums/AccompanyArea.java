package connectripbe.connectrip_be.post.entity.enums;

public enum AccompanyArea {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEJU("제주");

    private final String displayName;

    AccompanyArea(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AccompanyArea fromDisplayName(String displayName) {
        for (AccompanyArea area : AccompanyArea.values()) {
            if (area.getDisplayName().equals(displayName)) {
                return area;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}
