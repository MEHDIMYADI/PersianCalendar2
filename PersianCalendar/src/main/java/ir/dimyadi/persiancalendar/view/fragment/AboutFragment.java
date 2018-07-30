package ir.dimyadi.persiancalendar.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import ir.dimyadi.donate.Purchase;
import ir.dimyadi.donate.PurchaseActivity;
import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

import static android.app.Activity.RESULT_OK;

public class AboutFragment extends Fragment {

    RelativeLayout bug,mail,translate,rate,donate,licenses,libLicences,whatsnew;

    //donate
    RelativeLayout buy;
    SharedPreferences preferences;
    private boolean firstVisit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        final Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.about), "");

        AppCompatImageView AppIcon = (AppCompatImageView)view.findViewById(R.id.ic_app);
        AppIcon.setBackgroundResource(R.drawable.ic_launcher);

        final String version = utils.programVersion();

        TextView versionTextView = (TextView) view.findViewById(R.id.version);
        utils.setFont(versionTextView);
        versionTextView.setText(utils.shape(getString(R.string.version)) + " " +
                utils.formatNumber(version.split("-")[0]));

        //TextView licenseTextView = (TextView) view.findViewById(R.id.license);
        //licenseTextView.setText("Android Persian Calendar Version " + version + "\n" +
                //utils.readRawResource(R.raw.credits));

        //Linkify.addLinks(licenseTextView, Linkify.ALL);

        //whats new
        whatsnew = (RelativeLayout) view.findViewById(R.id.whatsNew);
        whatsnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view = inflater.inflate(R.layout.dialog_whatsnew, null);
                AppCompatImageView AppIconWhatsNew = (AppCompatImageView) view.findViewById(R.id.ic_app);
                AppIconWhatsNew.setBackgroundResource(R.drawable.ic_launcher);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view).setTitle(null)
                        .setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        //report bug
        bug = (RelativeLayout) view.findViewById(R.id.reportBug);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getString(R.string.mailto), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                try {
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n\n\n\n" +
                            "===Device Information===\nManufacturer: " + Build.MANUFACTURER + "\nModel: " + Build.MODEL + "\nAndroid Version: " + Build.VERSION.RELEASE + "\nApp Version Code: " + version.split("-")[0]);
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.sendMail)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getString(R.string.noclient), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //send mail
        mail = (RelativeLayout) view.findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.mailto)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, getString(R.string.sendMail)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), getString(R.string.noclient), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //translate
        translate = (RelativeLayout) view.findViewById(R.id.translate);
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dimyadi.ir/" + getActivity().getPackageName()));
                startActivity(browserIntent);
            }
        });

        //rate
        rate = (RelativeLayout) view.findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Uri uri = Uri.parse("myket://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Uri uriSite = Uri.parse("http://www.dimyadi.ir/" + getActivity().getPackageName());
                Intent goToSite = new Intent(Intent.ACTION_VIEW, uriSite);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(goToSite);
                }
            }
        });

        //donate
        buy= (RelativeLayout) view.findViewById(R.id.donate);
        preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        updateUi();
        firstVisit = true;

        //donate
        donate = (RelativeLayout) view.findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.donateDlg);
                builder.setCancelable(true);
                builder.setPositiveButton(R.string.donate, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        if (isPackageInstalled("ir.mservices.market",getActivity().getPackageManager())){
                            Intent intent = new Intent(getActivity(), PurchaseActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(),R.string.install_app,Toast.LENGTH_LONG).show();
                            String url = "https://www.myket.ir/get/app";
                            // Chrome is not installed
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(i);
                        }
                    }
                });
                //builder.setNegativeButton(R.string.paypal, new DialogInterface.OnClickListener() {

                //    @Override
                //    public void onClick(DialogInterface arg0, int arg1) {
                //        arg0.cancel();
                //        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                //        startActivity(browserIntent);
                //    }
                //});
                builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        //licenses
        licenses = (RelativeLayout) view.findViewById(R.id.licenses);
        licenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                WebView wv = new WebView(getActivity());
                wv.loadUrl("file:///android_asset/license.html");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.license));
                builder.setView(wv);
                builder.setCancelable(true);
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        //LibLicences
        libLicences = (RelativeLayout) view.findViewById(R.id.LibLicences);
        libLicences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {

                final SpannableString sorce1 = new SpannableString("https://github.com/ebraminio/DroidPersianCalendar");
                final TextView tx1 = new TextView(getActivity());
                tx1.setText(utils.formatNumber("1:\n")+
                        sorce1+
                        utils.formatNumber("\n"));
                tx1.setAutoLinkMask(RESULT_OK);
                tx1.setMovementMethod(LinkMovementMethod.getInstance());

                Linkify.addLinks(sorce1, Linkify.WEB_URLS);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.source))
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                    }
                                })

                        .setView(tx1).show();

            }
        });

        return view;
    }

    //donate
    public boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename,PackageManager.GET_ACTIVITIES);
            return packageManager.getApplicationInfo( packagename, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    //for fragment
    @Override
    public void onResume() {
        //other stuff
        super.onResume();
        if (firstVisit) {
            updateUi();
            firstVisit = false;
        }
    }
    //for activity
    //@Override
    //protected void onRestart() {
    //    // TODO Auto-generated method stub
    //    super.onRestart();
    //    updateUi();
    //}
    public void updateUi(){
        if (preferences.getBoolean("ispermiom",false)) {
            buy.setVisibility(View.INVISIBLE);
            buy.setEnabled(false);
        }
    }
}
