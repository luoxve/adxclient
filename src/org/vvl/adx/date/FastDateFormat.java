package org.vvl.adx.date;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FastDateFormat extends DateFormat {
    private static final FastDateFormat instance = new FastDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private DateFormat df;
    private long lastSec = -1;
    private StringBuffer sb = new StringBuffer();

    public FastDateFormat(DateFormat df) {
        this.df = df;
    }

    public static FastDateFormat getDateFormat() {
        return instance;
    }

    public Date parse(String text, ParsePosition pos) {
        return df.parse(text, pos);
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        long ds = date.getTime() / 1000;
        StringBuffer sb = this.sb;
        if (ds != lastSec) {
            synchronized (this) {
                if (ds != lastSec) {
                    sb = new StringBuffer();
                    df.format(date, sb, fieldPosition);
                    lastSec = ds;
                    this.sb = sb;
                } else {
                    sb = this.sb;
                }
            }
        }
        toAppendTo.append(sb);
        return toAppendTo;
    }
}
