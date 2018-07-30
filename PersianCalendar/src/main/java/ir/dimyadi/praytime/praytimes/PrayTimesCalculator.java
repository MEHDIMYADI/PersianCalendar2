package ir.dimyadi.praytime.praytimes;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PrayTimesCalculator {
    // default times
    private static Map<PrayTime, Double> _defaultTimes;

    static {
        _defaultTimes = new HashMap<>();
        _defaultTimes.put(PrayTime.IMSAK, 5d / 24);
        _defaultTimes.put(PrayTime.FAJR, 5d / 24);
        _defaultTimes.put(PrayTime.SUNRISE, 6d / 24);
        _defaultTimes.put(PrayTime.DHUHR, 12d / 24);
        _defaultTimes.put(PrayTime.ASR, 13d / 24);
        _defaultTimes.put(PrayTime.SUNSET, 18d / 24);
        _defaultTimes.put(PrayTime.MAGHRIB, 18d / 24);
        _defaultTimes.put(PrayTime.ISHA, 18d / 24);
        _defaultTimes = Collections.unmodifiableMap(_defaultTimes); // immutable
    }

    private final MinuteOrAngleDouble _imsak = StaticUtils.min(10);
    private final MinuteOrAngleDouble _dhuhr = StaticUtils.min(0);
    private final CalculationMethod.AsrJuristics _asr = CalculationMethod.AsrJuristics.Standard;
    private final CalculationMethod.HighLatMethods _highLats = CalculationMethod.HighLatMethods.NightMiddle;
    private final CalculationMethod _method;
    private double _timeZone;
    private double _jDate;
    private Coordinate _coordinate;

    public PrayTimesCalculator() {
        this(CalculationMethod.MWL); // default method
    }

    public PrayTimesCalculator(CalculationMethod method) {
        _method = method; // default method
    }

    //
    // Calculation Logic
    //
    //
    private Map<PrayTime, Clock> calculate(Date date, Coordinate coordinate,
                                           Double timeZone, Boolean dst) {
        _coordinate = coordinate;
        _timeZone = timeZone != null ? timeZone : getTimeZone(date);
        boolean _dst = dst != null ? dst : getDst(date);
        if (_dst) {
            _timeZone++;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        _jDate = julian(year, month, day) - _coordinate.getLongitude() / (15d * 24d);

        // compute prayer times at given julian date
        Map<PrayTime, Double> times = new HashMap<>();
        times.put(PrayTime.IMSAK, sunAngleTime(_imsak, _defaultTimes.get(PrayTime.IMSAK), true));
        times.put(PrayTime.FAJR, sunAngleTime(_method.getFajr(), _defaultTimes.get(PrayTime.FAJR), true));
        times.put(PrayTime.SUNRISE, sunAngleTime(riseSetAngle(), _defaultTimes.get(PrayTime.SUNRISE), true));
        times.put(PrayTime.DHUHR, midDay(_defaultTimes.get(PrayTime.DHUHR)));
        times.put(PrayTime.ASR, asrTime(asrFactor(), _defaultTimes.get(PrayTime.ASR)));
        times.put(PrayTime.SUNSET, sunAngleTime(riseSetAngle(), _defaultTimes.get(PrayTime.SUNSET)));
        times.put(PrayTime.MAGHRIB, sunAngleTime(_method.getMaghrib(), _defaultTimes.get(PrayTime.MAGHRIB)));
        times.put(PrayTime.ISHA, sunAngleTime(_method.getIsha(), _defaultTimes.get(PrayTime.ISHA)));

        times = adjustTimes(times);

        // add midnight time
        times.put(PrayTime.MIDNIGHT, (_method.getMidnight() == CalculationMethod.MidnightType.Jafari)
                ? times.get(PrayTime.SUNSET) + timeDiff(times.get(PrayTime.SUNSET), times.get(PrayTime.FAJR)) / 2
                : times.get(PrayTime.SUNSET) + timeDiff(times.get(PrayTime.SUNSET), times.get(PrayTime.SUNRISE)) / 2);

        Map<PrayTime, Clock> result = new HashMap<>();
        for (Map.Entry<PrayTime, Double> i : times.entrySet()) {
            result.put(i.getKey(), Clock.fromDouble(i.getValue()));
        }
        return result;
    }

    private Map<PrayTime, Clock> calculate(Date date, Coordinate coordinate,
                                           Double timeZone) {
        return calculate(date, coordinate, timeZone, null);
    }

    public Map<PrayTime, Clock> calculate(Date date, Coordinate coordinate) {
        return calculate(date, coordinate, null);
    }

    // compute mid-day time
    private double midDay(double time) {
        double eqt = sunPosition(_jDate + time).getEquation();
        return StaticUtils.fixHour(12 - eqt);
    }

    // compute the time at which sun reaches a specific angle below horizon
    private double sunAngleTime(MinuteOrAngleDouble angle, double time,
                                boolean ccw) {
        // TODO: I must enable below line!
        // if (angle.isMin()) throw new IllegalArgumentException("angle argument must be degree, not minute!");
        double decl = sunPosition(_jDate + time).getDeclination();
        double noon = StaticUtils.dtr(midDay(time));
        double t = Math.acos((-Math.sin(StaticUtils.dtr(angle.getValue())) - Math.sin(decl)
                * Math.sin(StaticUtils.dtr(_coordinate.getLatitude())))
                / (Math.cos(decl) * Math.cos(StaticUtils.dtr(_coordinate.getLatitude())))) / 15d;
        return StaticUtils.rtd(noon + (ccw ? -t : t));
    }

    private double sunAngleTime(MinuteOrAngleDouble angle, double time) {
        return sunAngleTime(angle, time, false);
    }

    // compute asr time
    private double asrTime(double factor, double time) {
        double decl = sunPosition(_jDate + time).getDeclination();
        double angle = -Math.atan(1 / (factor + Math.tan(StaticUtils.dtr(_coordinate.getLatitude()) - decl)));
        return sunAngleTime(StaticUtils.deg(StaticUtils.rtd(angle)), time);
    }

    // compute declination angle of sun and equation of time
    // Ref: http://aa.usno.navy.mil/faq/docs/SunApprox.php
    private DeclEqt sunPosition(double jd) {
        double D = jd - 2451545d;
        double g = (357.529 + 0.98560028 * D) % 360;
        double q = (280.459 + 0.98564736 * D) % 360;
        double L = (q + 1.915 * Math.sin(StaticUtils.dtr(g)) + 0.020 * Math.sin(StaticUtils.dtr(2d * g))) % 360;

        // weird!
        // double R = 1.00014 - 0.01671 * Math.cos(dtr(g)) - 0.00014 *
        // Math.cos(dtr(2d * g));

        double e = 23.439 - 0.00000036 * D;

        double RA = StaticUtils.rtd(Math.atan2(Math.cos(StaticUtils.dtr(e)) * Math.sin(StaticUtils.dtr(L)), Math.cos(StaticUtils.dtr(L)))) / 15d;
        double eqt = q / 15d - StaticUtils.fixHour(RA);
        double decl = Math.asin(Math.sin(StaticUtils.dtr(e)) * Math.sin(StaticUtils.dtr(L)));

        return new DeclEqt(decl, eqt);
    }

    // convert Gregorian date to Julian day
    // Ref: Astronomical Algorithms by Jean Meeus
    private double julian(int year, int month, int day) {
        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double A = Math.floor((double) year / 100);
        double B = 2 - A + Math.floor(A / 4);

        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;
    }

    // adjust times
    private Map<PrayTime, Double> adjustTimes(Map<PrayTime, Double> times) {
        Map<PrayTime, Double> result = new HashMap<PrayTime, Double>();
        for (Map.Entry<PrayTime, Double> i : times.entrySet()) {
            result.put(i.getKey(), i.getValue() + _timeZone - _coordinate.getLongitude() / 15d);
        }

        if (_highLats != CalculationMethod.HighLatMethods.None) {
            result = adjustHighLats(result);
        }

        if (_imsak.isMin()) {
            result.put(PrayTime.IMSAK, result.get(PrayTime.FAJR) - _imsak.getValue() / 60);
        }
        if (_method.getMaghrib().isMin()) {
            result.put(PrayTime.MAGHRIB, result.get(PrayTime.SUNSET) + _method.getMaghrib().getValue() / 60d);
        }
        if (_method.getIsha().isMin()) {
            result.put(PrayTime.ISHA, result.get(PrayTime.MAGHRIB) + _method.getIsha().getValue() / 60d);
        }
        result.put(PrayTime.DHUHR, result.get(PrayTime.DHUHR) + _dhuhr.getValue() / 60d);

        return result;
    }

    // Section 2!! (Compute Prayer Time in JS code)
    //

    // get asr shadow factor
    private double asrFactor() {
        return _asr == CalculationMethod.AsrJuristics.Hanafi ? 2d : 1d;
    }

    // return sun angle for sunset/sunrise
    private MinuteOrAngleDouble riseSetAngle() {
        // var earthRad = 6371009; // in meters
        // var angle = DMath.arccos(earthRad/(earthRad+ elv));
        double angle = 0.0347 * Math.sqrt(_coordinate.getElevation()); // an approximation
        return StaticUtils.deg(0.833 + angle);
    }

    // adjust times for locations in higher latitudes
    private Map<PrayTime, Double> adjustHighLats(Map<PrayTime, Double> times) {
        double nightTime = timeDiff(times.get(PrayTime.SUNSET),
                times.get(PrayTime.SUNRISE));

        times.put(PrayTime.IMSAK, adjustHLTime(times.get(PrayTime.IMSAK),
                times.get(PrayTime.SUNRISE), _imsak.getValue(), nightTime, true));

        times.put(PrayTime.FAJR, adjustHLTime(times.get(PrayTime.FAJR), times.get(PrayTime.SUNRISE),
                _method.getFajr().getValue(), nightTime, true));

        times.put(PrayTime.ISHA, adjustHLTime(times.get(PrayTime.ISHA), times.get(PrayTime.SUNSET),
                _method.getIsha().getValue(), nightTime));

        times.put(PrayTime.MAGHRIB, adjustHLTime(times.get(PrayTime.MAGHRIB),
                times.get(PrayTime.SUNSET), _method.getMaghrib().getValue(), nightTime));

        return times;
    }

    // adjust a time for higher latitudes
    private double adjustHLTime(double time, double bbase, double angle,
                                double night, boolean ccw) {
        double portion = nightPortion(angle, night);
        double timeDiff = ccw ? timeDiff(time, bbase) : timeDiff(bbase, time);

        if (Double.isNaN(time) || timeDiff > portion)
            time = bbase + (ccw ? -portion : portion);
        return time;
    }

    private double adjustHLTime(double time, double bbase, double angle,
                                double night) {
        return adjustHLTime(time, bbase, angle, night, false);
    }

    // the night portion used for adjusting times in higher latitudes
    private double nightPortion(double angle, double night) {
        double portion = 1d / 2d;
        if (_highLats == CalculationMethod.HighLatMethods.AngleBased) {
            portion = 1d / 60d * angle;
        }
        if (_highLats == CalculationMethod.HighLatMethods.OneSeventh) {
            portion = 1 / 7;
        }
        return portion * night;
    }

    // get local time zone
    private double getTimeZone(Date date) {
        return TimeZone.getDefault().getRawOffset() / (60 * 60 * 1000.0);
    }

    //
    // Time Zone Functions
    //
    //

    // get daylight saving for a given date
    private boolean getDst(Date date) {
        return TimeZone.getDefault().inDaylightTime(date);
    }

    // compute the difference between two times
    private double timeDiff(double time1, double time2) {
        return StaticUtils.fixHour(time2 - time1);
    }

    //
    // Misc Functions
    //
    //

    private class DeclEqt {
        private final double declination;
        private final double equation;

        DeclEqt(double declination, double equation) {
            super();
            this.declination = declination;
            this.equation = equation;
        }

        double getDeclination() {
            return declination;
        }

        double getEquation() {
            return equation;
        }
    }
}
