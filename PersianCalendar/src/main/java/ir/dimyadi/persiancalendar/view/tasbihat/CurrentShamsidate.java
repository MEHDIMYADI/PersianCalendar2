package ir.dimyadi.persiancalendar.view.tasbihat;

public class CurrentShamsidate {

    public CurrentShamsidate(int year, int month, int day)
    {
        //one month +1
        setGregorianDate(year,month+1,day);
    }

    public String getIranianDate()
    {
        return (irYear+"/"+irMonth+"/"+irDay);
    }

    private String getGregorianDate()
    {
        return (gYear+"/"+gMonth+"/"+gDay);
    }

    private String getJulianDate()
    {
        return (juYear+"/"+juMonth+"/"+juDay);
    }

    private String getWeekDayStr()
    {
        String weekDayStr[]={
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"};
        return (weekDayStr[getDayOfWeek()]);
    }

    public String toString()
    {
        return (getWeekDayStr()+
                ", Gregorian:["+getGregorianDate()+
                "], Julian:["+getJulianDate()+
                "], Iranian:["+getIranianDate()+"]");
    }

    private int getDayOfWeek()
    {
        return (JDN % 7);
    }

    private void setGregorianDate(int year, int month, int day)
    {
        gYear = year;
        gMonth = month;
        gDay = day;
        JDN = gregorianDateToJDN(year,month,day);
        JDNToIranian();
        JDNToJulian();
        JDNToGregorian();
    }

    private void IranianCalendar()
    {
        // Iranian years starting the 33-year rule
        int Breaks[]=
                {-61, 9, 38, 199, 426, 686, 756, 818,1111,1181,
                        1210,1635,2060,2097,2192,2262,2324,2394,2456,3178} ;
        int jm,N,leapJ,leapG,jp,j,jump;
        gYear = irYear + 621;
        leapJ = -14;
        jp = Breaks[0];
        // Find the limiting years for the Iranian year 'irYear'
        j=1;
        do{
            jm=Breaks[j];
            jump = jm-jp;
            if (irYear >= jm)
            {
                leapJ += (jump / 33 * 8 + (jump % 33) / 4);
                jp = jm;
            }
            j++;
        } while ((j<20) && (irYear >= jm));
        N = irYear - jp;
        // Find the number of leap years from AD 621 to the begining of the current
        // Iranian year in the Iranian (Jalali) ir.dimyadi.calendar
        leapJ += (N/33 * 8 + ((N % 33) +3)/4);
        if ( ((jump % 33) == 4 ) && ((jump-N)==4))
            leapJ++;
        // And the same in the Gregorian date of Farvardin the first
        leapG = gYear/4 - ((gYear /100 + 1) * 3 / 4) - 150;
        march = 20 + leapJ - leapG;
        // Find how many years have passed since the last leap year
        if ( (jump - N) < 6 )
            N = N - jump + ((jump + 4)/33 * 33);
        leap = (((N+1) % 33)-1) % 4;
        if (leap == -1)
            leap = 4;
    }

    private void JDNToIranian()
    {
        JDNToGregorian();
        irYear = gYear - 621;
        IranianCalendar(); // This invocation will update 'leap' and 'march'
        int JDN1F = gregorianDateToJDN(gYear,3,march);
        int k = JDN - JDN1F;
        if (k >= 0)
        {
            if (k <= 185)
            {
                irMonth = 1 + k/31;
                irDay = (k % 31) + 1;
                return;
            }
            else
                k -= 186;
        }
        else
        {
            irYear--;
            k += 179;
            if (leap == 1)
                k++;
        }
        irMonth = 7 + k/30;
        irDay = (k % 30) + 1;
    }

    private void JDNToJulian()
    {
        int j= 4 * JDN + 139361631;
        int i= ((j % 1461)/4) * 5 + 308;
        juDay = (i % 153) / 5 + 1;
        juMonth = ((i/153) % 12) + 1;
        juYear = j/1461 - 100100 + (8-juMonth)/6;
    }

    private int gregorianDateToJDN(int year, int month, int day)
    {
        int jdn = (year + (month - 8) / 6 + 100100) * 1461/4 + (153 * ((month+9) % 12) + 2)/5 + day - 34840408;
        jdn = jdn - (year + 100100+(month-8)/6)/100*3/4+752;
        return (jdn);
    }

    private void JDNToGregorian()
    {
        int j= 4 * JDN + 139361631;
        j = j + (((((4* JDN +183187720)/146097)*3)/4)*4-3908);
        int i= ((j % 1461)/4) * 5 + 308;
        gDay = (i % 153) / 5 + 1;
        gMonth = ((i/153) % 12) + 1;
        gYear = j/1461 - 100100 + (8-gMonth)/6;
    }


    private int irYear; // Year part of a Iranian date
    private int irMonth; // Month part of a Iranian date
    private int irDay; // Day part of a Iranian date
    private int gYear; // Year part of a Gregorian date
    private int gMonth; // Month part of a Gregorian date
    private int gDay; // Day part of a Gregorian date
    private int juYear; // Year part of a Julian date
    private int juMonth; // Month part of a Julian date
    private int juDay; // Day part of a Julian date
    private int leap; // Number of years since the last leap year (0 to 4)
    private int JDN; // Julian Day Number
    private int march; // The march day of Farvardin the first (First day of jaYear)
} // End of Class 'JavaSource_Calendar