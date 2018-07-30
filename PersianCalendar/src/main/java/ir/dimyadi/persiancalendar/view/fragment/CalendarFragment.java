package ir.dimyadi.persiancalendar.view.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.adapter.CalendarAdapter;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.dialog.GPSLocationDialog;
import ir.dimyadi.persiancalendar.view.dialog.GPSNetworkDialog;
import ir.dimyadi.persiancalendar.view.dialog.SelectDayDialog;
import ir.dimyadi.praytime.praytimes.Clock;
import ir.dimyadi.praytime.praytimes.Coordinate;
import ir.dimyadi.praytime.praytimes.PrayTime;
import ir.dimyadi.praytime.praytimes.PrayTimesCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ir.dimyadi.calendar.CivilDate;
import ir.dimyadi.calendar.DateConverter;
import ir.dimyadi.calendar.PersianDate;

public class CalendarFragment extends Fragment
        implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager monthViewPager;
    private Utils utils;

    private Calendar calendar = Calendar.getInstance();

    private Coordinate coordinate;

    private PrayTimesCalculator prayTimesCalculator;
    private TextView fajrTextView;
    private TextView dhuhrTextView;
    private TextView asrTextView;
    private TextView maghribTextView;
    private TextView ishaTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView midnightTextView;

    private TextView weekDayName;
    private TextView zekrDayName;
    private TextView gregorianDate;
    private TextView islamicDate;
    private TextView shamsiDate;
    private TextView eventTitle;
    private TextView holidayTitle;
    private TextView today;
    private AppCompatImageView todayIcon;

    private AppCompatImageView moreOwghat;

    private CardView owghat;
    private CardView event;

    private RelativeLayout fajrLayout;
    private RelativeLayout sunriseLayout;
    private RelativeLayout dhuhrLayout;
    private RelativeLayout asrLayout;
    private RelativeLayout sunsetLayout;
    private RelativeLayout maghribLayout;
    private RelativeLayout ishaLayout;
    private RelativeLayout midnightLayout;

    private AppCompatImageView owghat_location;

    private int viewPagerPosition;

    AlphaAnimation inAnimation;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        utils = Utils.getInstance(getContext());
        viewPagerPosition = 0;

        fajrLayout = (RelativeLayout) view.findViewById(R.id.fajrLayout);
        sunriseLayout = (RelativeLayout) view.findViewById(R.id.sunriseLayout);
        dhuhrLayout = (RelativeLayout) view.findViewById(R.id.dhuhrLayout);
        asrLayout = (RelativeLayout) view.findViewById(R.id.asrLayout);
        sunsetLayout = (RelativeLayout) view.findViewById(R.id.sunsetLayout);
        maghribLayout = (RelativeLayout) view.findViewById(R.id.maghribLayout);
        ishaLayout = (RelativeLayout) view.findViewById(R.id.ishaLayout);
        midnightLayout = (RelativeLayout) view.findViewById(R.id.midnightLayout);

        owghat_location = (AppCompatImageView) view.findViewById(R.id.owghat_location);

        gregorianDate = (TextView) view.findViewById(R.id.gregorian_date);
        utils.setFont(gregorianDate);
        islamicDate = (TextView) view.findViewById(R.id.islamic_date);
        utils.setFont(islamicDate);
        shamsiDate = (TextView) view.findViewById(R.id.shamsi_date);
        utils.setFont(shamsiDate);
        zekrDayName = (TextView) view.findViewById(R.id.TextWeekDayZekr);
        utils.setFont(zekrDayName);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_ZEKRROZ);
        zekrDayName.setTypeface(type);
        weekDayName = (TextView) view.findViewById(R.id.week_day_name);
        utils.setFont(weekDayName);
        today = (TextView) view.findViewById(R.id.today);
        todayIcon = (AppCompatImageView) view.findViewById(R.id.today_icon);

        fajrTextView = (TextView) view.findViewById(R.id.fajr);
        utils.setFont(fajrTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.fajrText));

        dhuhrTextView = (TextView) view.findViewById(R.id.dhuhr);
        utils.setFont(dhuhrTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.dhuhrText));

        asrTextView = (TextView) view.findViewById(R.id.asr);
        utils.setFont(asrTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.asrText));

        maghribTextView = (TextView) view.findViewById(R.id.maghrib);
        utils.setFont(maghribTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.maghribText));

        ishaTextView = (TextView) view.findViewById(R.id.isgha);
        utils.setFont(ishaTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.ishaText));

        sunriseTextView = (TextView) view.findViewById(R.id.sunrise);
        utils.setFont(sunriseTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.sunriseText));

        sunsetTextView = (TextView) view.findViewById(R.id.sunset);
        utils.setFont(sunsetTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.sunsetText));

        midnightTextView = (TextView) view.findViewById(R.id.midnight);
        utils.setFont(midnightTextView);
        utils.setFontAndShape((TextView) view.findViewById(R.id.midnightText));


        moreOwghat = (AppCompatImageView) view.findViewById(R.id.more_owghat);

        eventTitle = (TextView) view.findViewById(R.id.event_title);
        utils.setFont(eventTitle);
        holidayTitle = (TextView) view.findViewById(R.id.holiday_title);
        utils.setFont(holidayTitle);

        owghat = (CardView) view.findViewById(R.id.owghat);
        event = (CardView) view.findViewById(R.id.cardEvent);

        monthViewPager = (ViewPager) view.findViewById(R.id.calendar_pager);

        coordinate = utils.getCoordinate();
        prayTimesCalculator = new PrayTimesCalculator(utils.getCalculationMethod());

        monthViewPager.setAdapter(new CalendarAdapter(getChildFragmentManager()));
        monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);

        monthViewPager.addOnPageChangeListener(this);

        owghat.setOnClickListener(this);
        owghat_location.setOnClickListener(this);
        today.setOnClickListener(this);
        todayIcon.setOnClickListener(this);
        gregorianDate.setOnClickListener(this);
        islamicDate.setOnClickListener(this);
        shamsiDate.setOnClickListener(this);

        utils.setFontAndShape((TextView) view.findViewById(R.id.event_card_title));
        utils.setFontAndShape((TextView) view.findViewById(R.id.today));
        utils.setFontAndShape((TextView) view.findViewById(R.id.owghat_text));

        String cityName = utils.getCityName(false);
        if (!TextUtils.isEmpty(cityName)) {
            ((TextView) view.findViewById(R.id.owghat_text))
                    .append(" (" + utils.shape(cityName) + ")");
        }

        // This will immediately be replaced by the same functionality on fragment but is here to
        // make sure enough space is dedicated to actionbar's title and subtitle, kinda hack anyway
        PersianDate today = utils.getToday();
        utils.setActivityTitleAndSubtitle(getActivity(), utils.getMonthName(today),
                utils.formatNumber(today.getYear()));

        //change background of ir.dimyadi.calendar
        switch (utils.getSeason()) {
            case SPRING:
                LinearLayout spring = (LinearLayout)view.findViewById(R.id.calendar_background);
                spring.setBackgroundResource(R.drawable.spring);
                break;

            case SUMMER:
                LinearLayout summer = (LinearLayout)view.findViewById(R.id.calendar_background);
                summer.setBackgroundResource(R.drawable.summer);
                break;

            case FALL:
                LinearLayout fall = (LinearLayout)view.findViewById(R.id.calendar_background);
                fall.setBackgroundResource(R.drawable.autumn);
                break;

            case WINTER:
                LinearLayout winter = (LinearLayout)view.findViewById(R.id.calendar_background);
                winter.setBackgroundResource(R.drawable.winter);
                break;
        }

        final Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake_location_icon);
        @SuppressLint("CutPasteId") ImageView imageview = (AppCompatImageView) view.findViewById(R.id.owghat_location);
        imageview.startAnimation(animShake);


        return view;
    }

    public void changeMonth(int position) {
        monthViewPager.setCurrentItem(monthViewPager.getCurrentItem() + position, true);
    }

    public void selectDay(PersianDate persianDate) {
        weekDayName.setText(utils.shape(utils.getWeekDayName(persianDate)));
        zekrDayName.setText(utils.shape(utils.getZekrDayName(persianDate)));

        shamsiDate.setText(utils.shape(utils.dateToString(persianDate)));
        CivilDate civilDate = DateConverter.persianToCivil(persianDate);
        gregorianDate.setText(utils.shape(utils.dateToString(civilDate)));
        //say error if no date detail
        try {
            islamicDate.setText(utils.shape(utils.dateToString(
                DateConverter.civilToIslamic(civilDate, utils.getIslamicOffset()))));
        } catch (RuntimeException e) {
            islamicDate.setText(getString(R.string.date_exception));
        }
        if (utils.getToday().equals(persianDate)) {
            today.setVisibility(View.GONE);
            todayIcon.setVisibility(View.GONE);
            if (utils.iranTime) {
                weekDayName.setText(weekDayName.getText() +
                        utils.shape(" (" + getString(R.string.iran_time) + ")"));
                zekrDayName.setText(zekrDayName.getText());

            }
        } else {
            today.setVisibility(View.VISIBLE);
            todayIcon.setVisibility(View.VISIBLE);
        }

        setOwghat(civilDate);
        showEvent(persianDate);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void addEventOnCalendar(PersianDate persianDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        CivilDate civil = DateConverter.persianToCivil(persianDate);

        intent.putExtra(CalendarContract.Events.DESCRIPTION,
                utils.dayTitleSummary(persianDate));

        Calendar time = Calendar.getInstance();
        time.set(civil.getYear(), civil.getMonth() - 1, civil.getDayOfMonth());

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                time.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
    }

    private void showEvent(PersianDate persianDate) {
        String holidays = utils.getEventsTitle(persianDate, true);
        String events = utils.getEventsTitle(persianDate, false);

        event.setVisibility(View.GONE);
        holidayTitle.setVisibility(View.GONE);
        eventTitle.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(holidays)) {
            holidayTitle.setText(utils.shape(holidays));
            holidayTitle.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(events)) {
            eventTitle.setText(utils.shape(events));
            eventTitle.setVisibility(View.VISIBLE);
            event.setVisibility(View.VISIBLE);
        }
    }

    private void setOwghat(CivilDate civilDate) {
        if (coordinate == null) {
            return;
        }

        calendar.set(civilDate.getYear(), civilDate.getMonth() - 1, civilDate.getDayOfMonth());
        Date date = calendar.getTime();

        Map<PrayTime, Clock> prayTimes = prayTimesCalculator.calculate(date, coordinate);

        fajrTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.FAJR)));
        sunriseTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.SUNRISE)));
        dhuhrTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.DHUHR)));
        asrTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.ASR)));
        sunsetTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.SUNSET)));
        maghribTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.MAGHRIB)));
        ishaTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.ISHA)));
        midnightTextView.setText(utils.getPersianFormattedClock(prayTimes.get(PrayTime.MIDNIGHT)));

        owghat.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.owghat:
                fajrLayout.setVisibility(View.VISIBLE);
                sunriseLayout.setVisibility(View.VISIBLE);
                dhuhrLayout.setVisibility(View.VISIBLE);
                asrLayout.setVisibility(View.VISIBLE);
                sunsetLayout.setVisibility(View.VISIBLE);
                maghribLayout.setVisibility(View.VISIBLE);
                ishaLayout.setVisibility(View.VISIBLE);
                midnightLayout.setVisibility(View.VISIBLE);

                owghat_location.setVisibility(View.VISIBLE);

                moreOwghat.setVisibility(View.GONE);
                break;

            case R.id.today:
            case R.id.today_icon:
                bringTodayYearMonth();
                break;

            case R.id.islamic_date:
            case R.id.shamsi_date:
            case R.id.gregorian_date:
                utils.copyToClipboard(v);
            case R.id.owghat_location:
                //check whether gps provider and network providers are enabled or not
                Context context = getContext();
                LocationManager gps = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                NetworkInfo info = ((ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                boolean gps_enabled = false;

                try {
                    gps_enabled = gps.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ignored) {}

                if(!gps_enabled || info == null) {
                    // Custom Android Alert Dialog Title
                    DialogFragment frag = new GPSNetworkDialog();
                    frag.show(getActivity().getSupportFragmentManager(), "GPSNetworkDialog");
                } else {
                    DialogFragment frag = new GPSLocationDialog();
                    frag.show(getActivity().getSupportFragmentManager(), "GPSDialog");
                }
                break;
        }
    }

    private void bringTodayYearMonth() {
        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT,
                Constants.BROADCAST_TO_MONTH_FRAGMENT_RESET_DAY);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        if (monthViewPager.getCurrentItem() != Constants.MONTHS_LIMIT / 2) {
            monthViewPager.setCurrentItem(Constants.MONTHS_LIMIT / 2);
        }

        selectDay(utils.getToday());
    }

    public void bringDate(PersianDate date) {
        PersianDate today = utils.getToday();
        viewPagerPosition =
                (today.getYear() - date.getYear()) * 12 + today.getMonth() - date.getMonth();

        monthViewPager.setCurrentItem(viewPagerPosition + Constants.MONTHS_LIMIT / 2);

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, date.getDayOfMonth());

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        selectDay(date);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        viewPagerPosition = position - Constants.MONTHS_LIMIT / 2;

        Intent intent = new Intent(Constants.BROADCAST_INTENT_TO_MONTH_FRAGMENT);
        intent.putExtra(Constants.BROADCAST_FIELD_TO_MONTH_FRAGMENT, viewPagerPosition);
        intent.putExtra(Constants.BROADCAST_FIELD_SELECT_DAY, -1);

        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);

        today.setVisibility(View.VISIBLE);
        todayIcon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.action_goto, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to:
                SelectDayDialog dialog = new SelectDayDialog();
                dialog.show(getChildFragmentManager(), SelectDayDialog.class.getName());
                return true;
        }
        return false;
    }

    public int getViewPagerPosition() {
        return viewPagerPosition;
    }
}
