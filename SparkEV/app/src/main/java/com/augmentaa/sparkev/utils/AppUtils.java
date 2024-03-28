package com.augmentaa.sparkev.utils;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;


import com.augmentaa.sparkev.R;
import com.augmentaa.sparkev.SPINApplication;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okio.Buffer;

public class AppUtils {
    public static int project_id = 4;

    public static int DATA_NOT_FOUND_CODE = 1;
    public static int SYSTEM_ERROR_CODE = 2;
    public static int INVALID_REQUEST_CODE = 3;
    public static int SUCCESS_CODE = 200;
    public static int SERVER_ERROR_CODE = 502;
    public static int REQUEST_ERROR_CODE = 501;
    public static int BAD_REQUEST_CODE = 400;
    public static int URL_NOT_FOUND_CODE = 404;

    public static String DATA_NOT_FOUND = "DATA_NOT_FOUND";
    public static String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static String INVALID_REQUEST = "INVALID_REQUEST";
    public static String SUCCESS = "SUCCESS";
    public static String SERVER_ERROR = "SERVER_ERROR";
    public static String REQUEST_ERROR = "REQUEST_ERROR";
    public static String BAD_REQUEST = "BAD_REQUEST";
    public static String URL_NOT_FOUND = "URL_NOT_FOUND";

    public static String JSON_PARSING = "JSON_PARSING";
    public static boolean isProgressHomepage = false;


    public static void showDefaultDialog(Context context, String title, String message) {
        if (context != null) {
            new AlertDialog.Builder(context)
                    .setTitle(TextUtils.isEmpty(title) ? context.getString(R.string.app_name) : title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    public static void showDefaultDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        if (context != null) {
            new AlertDialog.Builder(context)
                    .setTitle(TextUtils.isEmpty(title) ? context.getString(R.string.app_name) : title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }
     /*AppUtils.showDefaultDialog(getActivity(), getString(R.string.app_name), "A new version of " + versionUpdated + " is available. Please update to version " + versionUpdated + " now", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {


        }
    });*/


    public static void showDefaultDialog(Context context, String title, String message, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener listener2) {
        if (context != null) {
            new AlertDialog.Builder(context)
                    .setTitle(TextUtils.isEmpty(title) ? context.getString(R.string.app_name) : title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, listener)
                    .setNegativeButton(android.R.string.cancel, listener2)
                    .setCancelable(false)
                    .create()
                    .show();
        }
    }

    public static String getAddressCity(LatLng latLng) {
        Geocoder geocoder = new Geocoder(SPINApplication.get());
        String addresss = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                String locality = addressList.get(0).getAddressLine(0);
                String country = addressList.get(0).getCountryName();
                String subLocality = addressList.get(0).getSubLocality();
                String city = addressList.get(0).getLocality();
                Logger.e("Address    " + subLocality + "    " + locality + "      " + city + "      " + country);
                Logger.e("Address Locality    " + locality);
                addresss = locality;
                /*if (subLocality != null && !country.isEmpty()) {
                    addresss = locality;
                } else {
                    addresss = city;

                }*/
            }
            Logger.e("Address 123   " + addresss);

        } catch (IOException e) {
            Logger.e("Exp " + e.getMessage());

            e.printStackTrace();
        }
        return addresss;
    }

    public static String bodyToStringCharger(final RequestBody request) {
        try {

            final RequestBody copy = request;
            final Buffer buffer = new Buffer();

            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }

            buffer.readUtf8().replaceAll("\\\\", "");
            Logger.e("Request parameter " + buffer.readUtf8().replaceAll("\\\\", ""));
            return buffer.readUtf8().replaceAll("\\\\", "");
        } catch (final IOException e) {
            return "did not work";
        }
    }


    public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            Logger.e(buffer.readUtf8());
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    public static String convertDate(final String source_date) {
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = originalFormat.parse(source_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);  // 20120821


        return formattedDate;
    }

    public static String getDate(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE" + " " + formatted);
        return formatted;
    }

    public static String getDateTwo(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy-HH:mm");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE" + " " + formatted);
        return formatted;
    }


    public static String getOnlyDate(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE" + " " + formatted);
        return formatted;
    }


    public static String getDateWarranty(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE" + " " + formatted);
        return formatted;
    }



    public static boolean isTimeValidation(String currentdate, String futureDate) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("HH:mm").parse(currentdate);
            Date time2 = new SimpleDateFormat("HH:mm").parse("23:59");
            Date d = new SimpleDateFormat("HH:mm").parse(futureDate);
            if (time1.before(d) && time2.after(d)) {
                isBetween = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }







    public static boolean isTimeBetweenNineToSixValidation(String cTime, String sTime) {
        boolean isBetween = false;
        try {
            Date time1 = new SimpleDateFormat("HH:mm").parse(cTime);
            Date time2 = new SimpleDateFormat("HH:mm").parse(cTime);
            Date d = new SimpleDateFormat("HH:mm").parse(sTime);

            if (time1.before(d) && time2.after(d)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }


    public static int timeConvertintoDays(int time) {
        return time / 24 / 60;
    }

    public static boolean isDateValidation(String currentdate, String futureDate) {
        boolean isBetween = false;
        try {
            Date curr_date = new SimpleDateFormat("dd-MM-yyyy").parse(currentdate);
            Date fut_date = new SimpleDateFormat("dd-MM-yyyy").parse(futureDate);

            if (curr_date.before(fut_date)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }


    public static boolean isDateValidationSch(String currentdate, String futureDate) {
        boolean isBetween = false;
        try {
            Date curr_date = new SimpleDateFormat("dd-MM-yyyy").parse(currentdate);
            Date fut_date = new SimpleDateFormat("dd-MM-yyyy").parse(futureDate);

            if (curr_date.before(fut_date)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }


    public static boolean isDateValidationSchedule(String currentdate, String futureDate) {
        boolean isBetween = false;
        try {
            Date curr_date = new SimpleDateFormat("dd-MM-yyyy").parse(currentdate);
            Date fut_date = new SimpleDateFormat("dd-MM-yyyy").parse(futureDate);

            if (curr_date.before(fut_date)) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String geDateTime(String date) {
        Logger.e("input date" + " " + date);


        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MM yyyy-HH:mm");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            Logger.e("Date Ex== " + e);
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);
        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy-HH:mm", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
//            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            Logger.e("Date Ex=1212= " + e);
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String geDateSummary(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static String geDateSessionHistory(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE11111" + " " + formatted);


        return formatted;
    }


    public static String getDateWarrantyPurchase(String date) {
        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        Logger.e("DATE11111" + " " + formatted);


        return formatted;
    }


    public static String getDateonChart(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static String getDateonChartNew(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);


       /* SimpleDateFormat df = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        return formatted;
    }


    public static final int[] MATERIAL_COLORS = {
            rgb("#00454D"), rgb("#006B73"), rgb("#009499"), rgb("#06BFBF"), rgb("#4CD9CF"),
            rgb("#75E6DA"), rgb("#A2F2E8")
    };

    public static String getDateonNotification(String date) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("MMM dd, yyyy");
        Date d = null;
        try {
            d = input.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = null;
        String formatted = output.format(d);
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = df.parse(formatted);
            df.setTimeZone(TimeZone.getDefault());
            formattedDate = df.format(date1);
            Logger.e("DATE11111" + " " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
