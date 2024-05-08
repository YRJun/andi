package com.summer.common.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Renjun Yu
 * @description
 * @date 2024/01/21 17:21
 */
@Slf4j
public class OtherUtils {
    /**
     * 获取客户端ip,由hutool ServletUtil.getClientIP()方法转换而来,修改了入参HttpServletRequest的包路径
     * @param request HttpServletRequest对象
     * @return 客户端ip
     */
    public static String getClientIP(HttpServletRequest request) {
        try {
            if (request == null) {
                return "unknown";
            }
            String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
            String ip;
            for (String header : headers) {
                ip = request.getHeader(header);
                if (!NetUtil.isUnknown(ip)) {
                    return NetUtil.getMultistageReverseProxyIp(ip);
                }
            }
            ip = request.getRemoteAddr();
            return NetUtil.getMultistageReverseProxyIp(ip);
        } catch (Exception e) {
            return "unknown";
        }
    }
    public static String getSnowflakeId() {
        final Snowflake snowflake = IdUtil.getSnowflake();
        final long l = snowflake.nextId();
        return String.valueOf(l);
    }
}
