package pers.liujunyi.cloud.photo.util;

import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.common.util.DayCompare;

import java.util.Date;

/**
 * 工具类
 * @author ljy
 */
public class PhotoUtils {

    /**
     * 在职年限
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getWorkingYears(Date startDate, Date endDate) {
        Date curEndDate = new Date();
        if (endDate != null) {
            curEndDate = endDate;
        }
        // 计算在职年限
        DayCompare dayCompare = DateTimeUtils.dayComparePrecise(startDate, curEndDate);
        StringBuffer duration = new StringBuffer();
        if (dayCompare.getYear() > 0) {
            duration.append(dayCompare.getYear()).append("年");
        }
        if (dayCompare.getMonth() > 0) {
            duration.append(dayCompare.getMonth()).append("月");
        }
        if (dayCompare.getDay() > 0) {
            duration.append(dayCompare.getDay()).append("日");
        }
        return duration.toString();
    }

    /**
     * 年龄
     * @param birthday
     * @return
     */
    public static Byte getAge(Date birthday) {
        DayCompare ageCompare = DateTimeUtils.dayComparePrecise(birthday, new Date());
        return (byte) ageCompare.getYear();
    }

}
