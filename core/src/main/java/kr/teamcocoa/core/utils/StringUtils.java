package kr.teamcocoa.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {

    public static final String TEAMCOCOA_API_KEY = "F824047CDF49B138D1990B4DA178A16D45CE2CC7DBDDDCA02CE5AB6BD8733A8F2CAB22F298484F110D3E93A02F3A73BA49C102131C8DCCD83AB3AE2EF9F413BA";

    public static String color(String string) {
        char[] b = string.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    public static String getTimestampToDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String formattedDate = sdf.format(date);
        return formattedDate + " (UTC +9)";
    }

    public static String getTimeFormat(int sec) {
        String minute = "0" + sec / 60;
        String second = "0" + sec % 60;
        return minute.substring(minute.length() - 2) + ":" + second.substring(second.length() - 2);
    }

}
