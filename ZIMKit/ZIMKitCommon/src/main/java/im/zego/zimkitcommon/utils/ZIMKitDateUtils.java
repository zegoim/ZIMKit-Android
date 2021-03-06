package im.zego.zimkitcommon.utils;


import android.app.Application;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import im.zego.zimkitcommon.R;


public class ZIMKitDateUtils {
    private static Application sApplication;
    public static SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-M-dd", Locale.CHINA);
    public static SimpleDateFormat sdfYMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static SimpleDateFormat sdfYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static SimpleDateFormat sdfYMDHMS2 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    public static SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static SimpleDateFormat sdfMD = new SimpleDateFormat("MM-dd", Locale.CHINA);
    public static SimpleDateFormat sdfHM = new SimpleDateFormat("HH:mm", Locale.CHINA);
    public static SimpleDateFormat sdfHMS = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    public static SimpleDateFormat sdfMDHM = new SimpleDateFormat("MM/dd HH:mm", Locale.CHINA);
    public static SimpleDateFormat sdfMDAndHM = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
    public static SimpleDateFormat sdfYM = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    public static SimpleDateFormat sdfMD2 = new SimpleDateFormat("MM-dd", Locale.CHINA);
    private static String[] weekOfDays = null;
    public static SimpleDateFormat sdfUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.CHINA);

    public static void setContext(Application application) {
        sApplication = application;
    }

    public static String getUct(String data) {
        Date result;
        try {
            result = sdfUTC.parse(data);
            sdfYMDHMS.setTimeZone(TimeZone.getDefault());
            return sdfYMDHMS.format(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ???????????????"X??????"?????????"X?????????"?????????"X?????????"(X<=5)???????????????/??????/??????"HH:mm"?????????"MM-dd"?????????
     */
    public static String getExactDate(long date) {
        if (sdfYMD.format(date).equals(sdfYMD.format(new Date()))) {
            long range = System.currentTimeMillis() - date;
            if (range < 1000) {//??????1??????????????????????????????????????????
                return "??????";
            } else if (range <= 60 * 1000) {
                return "??????";
            } else if (range <= 60 * 60 * 1000) {
                return range / 60000 + "?????????";
            } else if (range <= 6 * 60 * 60 * 1000) {
                return range / 3600000 + "?????????";//??????6??????
            } else {
                return "??????" + sdfHM.format(date);
            }
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 2 * 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else {
            return sdfMDAndHM.format(date);
        }
    }

    /**
     * ?????????????????????/??????/??????"HH:mm"?????????"yyyy-MM-dd"?????????
     */
    public static String getYMDTDate(long date) {
        if (sdfYMD.format(date).equals(sdfYMD.format(new Date()))) {
            return "??????" + sdfHM.format(date);
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 2 * 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else {
            return sdfYMD.format(date);
        }
    }

    /**
     * ????????????"yyyy-MM-dd"??????
     */
    public static String getYMDDate(long date) {
        return sdfYMD.format(date);
    }

    /**
     * ????????????"HH:mm:ss"??????
     */
    public static String getHMSDate(long date) {
        return sdfHMS.format(date);
    }

    /**
     * ???????????????"X??????"?????????"X?????????"??????"MM-dd HH:mm"??????
     */
    public static String getMDHMDate(long date) {
        if (sdfMD.format(date).equals(sdfMD.format(new Date()))) {
            long range = System.currentTimeMillis() - date;
            if (range < 1000) {//??????1??????????????????????????????????????????
                return "1??????";
            } else if (range <= 60 * 1000 && range >= 1000) {
                return range / 1000 + "??????";
            } else if (range <= 60 * 60 * 1000 && range >= 60 * 1000) {
                return range / 60000 + "?????????";
            } else {
                return sdfMDHM.format(date);
            }
        } else {
            return sdfMDHM.format(date);
        }
    }

    /**
     * ????????????"MM-dd"?????????????????????
     */
    public static String getMDDate(long date) {
        if (sdfYMD.format(date).equals(sdfYMD.format(new Date()))) {
            return "??????";
        }
        return sdfMD.format(date);
    }

    /**
     * ?????????????????????/??????/??????"HH:mm"?????????"MM-dd"?????????
     */
    public static String getMDTDate(long date) {
        if (sdfYMD.format(date).equals(sdfYMD.format(new Date()))) {
            return "??????" + sdfHM.format(date);
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else if (sdfYMD.format(date).equals(sdfYMD.format(new Date().getTime() - 2 * 24 * 60 * 60 * 1000))) {
            return "??????" + sdfHM.format(date);
        } else {
            return sdfMD.format(date);
        }
    }

    /**
     * ??????"????????????"
     *
     * @param date
     * @return
     */
    public static String getMDDate2(long date) {
        return sdfMD2.format(date);
    }

    /**
     * ?????????????????????
     */
    public static String getMessageDate(long date, boolean showDetailTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date(date));
        Calendar nowCal1 = Calendar.getInstance();
        nowCal1.setTime(new Date());
        if (sdfYMD.format(date).equals(sdfYMD.format(new Date()))) {
            return sdfHM.format(date);
        } else if (nowCal1.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR) == 1) {
            return sApplication.getString(R.string.common_yesterday) + (showDetailTime ? " " + sdfHM.format(date) : "");
        } else if (nowCal1.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR) < 7) {
            return getWeekOfDate(date) + (showDetailTime ? " " + sdfHM.format(date) : "");
        } else if (cal1.get(Calendar.YEAR) != nowCal1.get(Calendar.YEAR)) {
            if (showDetailTime) {
                return sdfYMDHM.format(date);
            } else {
                return sdfYMD.format(date);
            }
        } else {
            if (showDetailTime) {
                return sdfMDAndHM.format(date);
            } else {
                return sdfMD.format(date);
            }
        }
    }


    /**
     * ????????????"yyyy-MM-dd HH:mm"??????
     */
    public static String getYMDHMDate(long date) {
        return sdfYMDHM.format(date);
    }

    /**
     * ????????????"yyyy-MM"??????
     */
    public static String getYMDate(long date) {
        return sdfYM.format(date);
    }

    /**
     * ????????????"HH:mm"??????
     */
    public static String getHMDate(long date) {
        return sdfHM.format(date);
    }

    /**
     * ????????????"yyyy-MM-dd HH:mm:ss"??????
     */
    public static String getYMDHMSDate(long date) {
        return sdfYMDHMS.format(date);
    }

    /**
     * ????????????"yyyyMMddHHmmss"??????
     */
    public static String getYMDHMSDate2(long date) {
        return sdfYMDHMS2.format(date);
    }

    /**
     * ?????????yyyy-M-dd??????????????????yyyy-MM-dd????????????
     */
    public static long parseLong(String date) {
        try {
            return sdfParse.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * ?????????yyyy-MM-dd???????????????
     */
    public static long parseLong2(String date) {
        try {
            return sdfYMD.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * ?????????yyyy-MM-dd HH:mm:ss???????????????
     */
    public static long parseLong3(String date) {
        try {
            // FIXME: 2021/10/8 api??????????????????null,?????????crash
            if (TextUtils.isEmpty(date)) {
                return 0;
            }
            return sdfYMDHMS.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * ?????????yyyy-MM-dd HH:mm???????????????
     */
    public static long parseLong4(String date) {
        try {
            // FIXME: 2021/10/8 api??????????????????null,?????????crash
            if (TextUtils.isEmpty(date)) {
                return 0;
            }
            return sdfYMDHM.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * ???????????????????????????
     */
    public static long parseLong(String date, SimpleDateFormat format) {
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * ??????????????????????????? ?????????null????????????????????????????????????
     *
     * @param milliseconds
     * @return
     */
    public static String getWeekOfDate(Long milliseconds) {
        if (weekOfDays == null) {
            weekOfDays = sApplication.getResources().getStringArray(R.array.common_weeks);
        }
        return getWeekOfDate(milliseconds, weekOfDays);
    }

    public static String getWeekOfDate(Long milliseconds, String[] weekOfDays) {
        Calendar calendar = Calendar.getInstance();
        if (milliseconds != null) {
            calendar.setTimeInMillis(milliseconds);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }

    /**
     * ??????date??????n????????????
     */
    public static Date getDateAfter(Date date, int n) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + n);
        return now.getTime();
    }

    /**
     * ??????date??????n????????????
     */
    public static Date getDateBefore(Date date, int n) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - n);
        return now.getTime();
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int calcIntervalDays(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        int days = (int) ((calendar2.getTime().getTime() - calendar1.getTime().getTime()) / (1000 * 3600 * 24));
        return days;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {

        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
    }

    public static long parseUTCTime(String utc) {
        sdfUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdfUTC.parse(utc).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * ????????? 00???00???00
     *
     * @param seconds
     * @return
     */
    public static String formatTime(long seconds) {
        int temp;
        StringBuffer sb = new StringBuffer();
        if (seconds > 3600) {
            temp = (int) (seconds / 3600);
            sb.append((seconds / 3600) < 10 ? "0" + temp + ":" : temp + ":");
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        } else {
            temp = (int) (seconds % 3600 / 60);
            changeSeconds(seconds, temp, sb);
        }
        return sb.toString();
    }

    private static void changeSeconds(long seconds, int temp, StringBuffer sb) {
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = (int) (seconds % 3600 % 60);
        sb.append((temp < 10) ? "0" + temp : "" + temp);
    }

}
