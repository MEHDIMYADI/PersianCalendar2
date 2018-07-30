package ir.dimyadi.calendar;

public abstract class AbstractDate {

    public void setDate(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDayOfMonth(day);
    }

    public abstract int getYear();

    public abstract void setYear(int year);

    public abstract int getMonth();

    public abstract void setMonth(int month);

    public abstract int getDayOfMonth();

    public abstract void setDayOfMonth(int day);

    public abstract int getDayOfWeek();

    public abstract int getDayOfYear();

    public abstract int getWeekOfYear();

    public abstract int getWeekOfMonth();

    public abstract void rollDay(int amount, boolean up);

    public abstract void rollMonth(int amount, boolean up);

    public abstract void rollYear(int amount, boolean up);

    /**
     * Returns a string specifying the event of this date, or null if there are
     * no events for this year.
     */
    public abstract String getEvent();

    public abstract boolean isLeapYear();

    public abstract AbstractDate clone();
}
