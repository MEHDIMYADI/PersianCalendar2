package ir.dimyadi.praytime.praytimes;

public enum CalculationMethod {
    MWL("Muslim World League", StaticUtils.deg(18), StaticUtils.deg(17)),
    ISNA("Islamic Society of North America (ISNA)", StaticUtils.deg(15), StaticUtils.deg(15)),
    Egypt("Egyptian General Authority of Survey", StaticUtils.deg(19.5), StaticUtils.deg(17.5)),
    Makkah("Umm Al-Qura University, Makkah", StaticUtils.deg(18.5), StaticUtils.min(90)),
    Karachi("University of Islamic Sciences, Karachi", StaticUtils.deg(18), StaticUtils.min(18)),
    Tehran("Institute of Geophysics, University of Tehran", StaticUtils.deg(17.7), StaticUtils.deg(14), StaticUtils.deg(4.5), MidnightType.Jafari),
    Jafari("Shia Ithna-Ashari, Leva Institute, Qum", StaticUtils.deg(16), StaticUtils.deg(14), StaticUtils.deg(4), MidnightType.Jafari);

    private final String name;
    private final MinuteOrAngleDouble fajr;
    private final MinuteOrAngleDouble isha;
    private final MinuteOrAngleDouble maghrib;
    private final MidnightType midnight;

    CalculationMethod(String name, MinuteOrAngleDouble fajr,
                      MinuteOrAngleDouble isha, MinuteOrAngleDouble maghrib,
                      MidnightType midnight) {
        this.name = name;
        this.fajr = fajr;
        this.isha = isha;
        this.maghrib = maghrib == null ? StaticUtils.min(0) : maghrib;
        this.midnight = midnight == null ? MidnightType.Standard : midnight;
    }

    CalculationMethod(String name, MinuteOrAngleDouble fajr,
                      MinuteOrAngleDouble isha, MinuteOrAngleDouble maghrib) {
        this(name, fajr, isha, maghrib, null);
    }

    CalculationMethod(String name, MinuteOrAngleDouble fajr,
                      MinuteOrAngleDouble isha) {
        this(name, fajr, isha, null);
    }

    public String getName() {
        return name;
    }

    public MinuteOrAngleDouble getFajr() {
        return fajr;
    }

    public MinuteOrAngleDouble getIsha() {
        return isha;
    }

    public MinuteOrAngleDouble getMaghrib() {
        return maghrib;
    }

    public MidnightType getMidnight() {
        return midnight;
    }

    // Midnight Mode
    public enum MidnightType {
        Standard, // Mid Sunset to Sunrise
        Jafari // Mid Sunset to Fajr
    }

    // Asr Juristic Methods
    public enum AsrJuristics {
        Standard, // Shafi`i, Maliki, Ja`fari, Hanbali
        Hanafi // Hanafi
    }

    // Adjust Methods for Higher Latitudes
    public enum HighLatMethods {
        NightMiddle, // middle of night
        AngleBased, // angle/60th of night
        OneSeventh, // 1/7th of night
        None // No adjustment
    }
}
