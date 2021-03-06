package pers.liujunyi.cloud.photo.util;

/**
 * 常量信息
 * @author ljy
 */
public final class Constant {
    /** 禁用状态 */
    public static final Byte DISABLE_STATUS = 1;
    /** 启用状态 */
    public static final Byte ENABLE_STATUS = 0;
    public static final String DATA_GRID_MESSAGE = "无数据";


    /**
     * 数据状态值
     * @param status
     * @return
     */
    public static String getStatusValue(Byte status) {
        String statusValue = null;
        switch (status.byteValue()) {
            case 1:
                statusValue = "禁用";
                break;
            case 0:
                statusValue = "正常";
                break;
            default:
                statusValue = "正常";
                break;
        }
        return statusValue;
    }
}
