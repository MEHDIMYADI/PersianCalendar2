package ir.dimyadi.praytime.praytimes;

class StaticUtils {
    static double dtr(double d) {
        return (d * Math.PI) / 180d;
    }

    static double rtd(double r) {
        return (r * 180d) / Math.PI;
    }

    static MinuteOrAngleDouble deg(int value) {
        return deg((double) value);
    }

    public static MinuteOrAngleDouble min(int value) {
        return min((double) value);
    }

    static MinuteOrAngleDouble deg(double value) {
        return new MinuteOrAngleDouble(value, false);
    }

    private static MinuteOrAngleDouble min(double value) {
        return new MinuteOrAngleDouble(value, true);
    }

    static double fixHour(double a) {
        return fix(a, 24);
    }

    private static double fix(double a, double b) {
        double result = a % b;
        if (result < 0)
            result = b + result;
        return result;
    }
}
