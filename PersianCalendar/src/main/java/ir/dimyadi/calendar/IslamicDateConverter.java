package ir.dimyadi.calendar;

import java.util.HashMap;
import java.util.Map;

class IslamicDateConverter {
    //adad ghabli jam mishavd ba tedad roz mah hijri v hashmap badi peyda shavad EX: 2458375(hashmap ghabli)+29(tedadroozmahbaad).
    //date source: http://www.time.ir/fa/eventyear-%D8%AA%D9%82%D9%88%DB%8C%D9%85-%D8%B3%D8%A7%D9%84%DB%8C%D8%A7%D9%86%D9%87
    private static Map<Integer, int[]> yearsMonthsInJd = new HashMap<Integer, int[]>() {{
        put(1427, new int[] { 2453767, 2453797, 2453826, 2453855, 2453885, 2453914, 2453944, 2453974, 2454004, 2454034, 2454063, 2454092 });
        put(1428, new int[] { 2454122, 2454151, 2454181, 2454210, 2454239, 2454268, 2454298, 2454328, 2454357, 2454387, 2454417, 2454447 });
        put(1429, new int[] { 2454476, 2454506, 2454535, 2454565, 2454594, 2454623, 2454652, 2454682, 2454712, 2454741, 2454771, 2454801 });
        put(1430, new int[] { 2454830, 2454860, 2454890, 2454919, 2454948, 2454978, 2455007, 2455037, 2455066, 2455095, 2455125, 2455155 });
        put(1431, new int[] { 2455184, 2455214, 2455244, 2455273, 2455303, 2455332, 2455362, 2455391, 2455421, 2455450, 2455479, 2455509 });
        put(1432, new int[] { 2455538, 2455568, 2455598, 2455627, 2455657, 2455687, 2455717, 2455746, 2455775, 2455805, 2455834, 2455864 });
        put(1433, new int[] { 2455893, 2455922, 2455952, 2455981, 2456011, 2456041, 2456071, 2456100, 2456130, 2456159, 2456189, 2456218 });
        put(1434, new int[] { 2456248, 2456277, 2456306, 2456336, 2456365, 2456395, 2456425, 2456454, 2456484, 2456514, 2456543, 2456573 });
        put(1435, new int[] { 2456602, 2456631, 2456661, 2456690, 2456720, 2456749, 2456779, 2456808, 2456838, 2456868, 2456898, 2456927 });
        put(1436, new int[] { 2456957, 2456986, 2457016, 2457045, 2457074, 2457104, 2457133, 2457163, 2457192, 2457222, 2457251, 2457281 });
        put(1437, new int[] { 2457311, 2457340, 2457370, 2457400, 2457429, 2457459, 2457488, 2457517, 2457547, 2457576, 2457605, 2457635 });
        put(1438, new int[] { 2457665, 2457694, 2457724, 2457754, 2457784, 2457813, 2457843, 2457872, 2457901, 2457931, 2457960, 2457989 });
                            //zolhaje//moharam //safar //rabi1// //rabi2 //jamadi1//jamadi2 //rajab  //shaban  //ramzan //shaval //zolghade
        put(1439, new int[] { 2458019, 2458048, 2458078, 2458108, 2458138, 2458168, 2458197, 2458227, 2458256, 2458285, 2458315, 2458344 });
        put(1440, new int[] { 2458373, 2458403, 2458432, 2458462, 2458492, 2458522, 2458551, 2458580, 2458610, 2458639, 2458669, 2458698 });
        put(1441, new int[] { 2458727, 2458757, 2458786, 2458816, 2458845, 2458875, 2458905, 2458934, 2458964, 2458994, 2459023, 2459053});
        //put(1442, new int[] { 2459082, 2459111, 2459141, 2459170, 2459200, 2459229, 2459259, 2459288});
    }};

                                                //sal 1427//sal 1428 //sal 1429 //sal 1430// //sal 1431 //sal 1432//sal 1433 //sal 1434  //sal 1435  //sal 1436 //sal 1437 //sal 1438 //sal 1439 //sal 1440 //sal 1441
    private static int[] yearsStartJd = new int[] { 2453767, 2454122, 2454476, 2454830, 2455184, 2455538, 2455893, 2456248, 2456602, 2456957, 2457311, 2457665, 2458019, 2458373, 2458727, 2459082};

    private static int supportedYearsStart = 1427;

    static int hijriToJd(int year, int month, int day) {
        int supportedYearsEnd = 1442;
        if (year < supportedYearsStart || year > supportedYearsEnd)
            return -1;

        return yearsMonthsInJd.get(year)[month - 1] + day - 1;
    }

    private static int search(int[] array, int r) {
        int i = 0;
        while (i < array.length && array[i] < r) ++i;
        return i;
    }

    static int[] jdToHirji(int jd) {
        int jdSupportStart = 2453767;
        int jdSupportEnd = 2559082;
        if (jd < jdSupportStart || jd > jdSupportEnd)
            return null;

        int yearIndex = search(yearsStartJd, jd);
        int year = yearIndex + supportedYearsStart - 1;
        int[] yearMonths = yearsMonthsInJd.get(year);
        int month = search(yearMonths, jd);
        int day = jd - yearMonths[month - 1];
        return new int[] { year, month, day };
    }
}