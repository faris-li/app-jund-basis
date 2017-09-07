package com.jund.base.filter;

import com.jund.framework.core.annotation.Logging;
import com.jund.framework.security.authentication.AuthenticationHolder;
import com.jund.log.remote.api.LogRemoteService;
import com.jund.platformwork.log.model.LogDto;
import com.jund.security.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作日志统一处理
 *
 * @author tanghui
 */
@Aspect
@Component
@Order(1)
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private LogRemoteService logRemoteService;

    @Autowired
    private Environment env;

    @Pointcut("@annotation(com.vpcloud.framework.core.annotation.Logging)")
    public void methodAspect() {
    }

    /**
     * 查询系统参数，是否记录操作日志
     */
    @Before("methodAspect()")
    public void enterMethod(JoinPoint joinPoint) {
//        try {
//            ResponseInfo result = paramRemoteService.findByParamCode(BaseConst.PARAM_LOG_FLAG);
//            if (result != null && BaseConst.ReturnCode.OK.equals(result.getCode())) {
//                LOG_SWITCH = (result.getData()).equals("1") ? BaseConst.LOG.OPEN : BaseConst.LOG.CLOSE;
//            }
//        } catch (RestClientResponseException e) {
//            logger.debug("参数服务调用异常,参数为：" + BaseConst.PARAM_LOG_FLAG);
//        }
    }

    @AfterReturning(pointcut = "methodAspect()", returning = "result")
    public void afterMethod(JoinPoint joinPoint, Object result) {
        try {
            if (hasOpen()) {
                String userName = AuthenticationHolder.getUsername();
                LogDto log = new LogDto();
                log.setClientIp(getClientIp());
                log.setLogType(1);
                log.setLogTitle(getTitle(joinPoint));
                log.setLogData(getData(joinPoint));
                log.setOptTime(new Date());
                log.setUserName(userName);
                logRemoteService.save(log);
            }
        } catch (Exception e) {
            logger.error("操作日志记录失败！", e);
        }
    }

    private String getTitle(JoinPoint joinPoint) {
        Class targetClazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Method[] methods = targetClazz.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == args.length) {
                    description = method.getAnnotation(Logging.class).title();
                    break;
                }
            }
        }
        return description;
    }

    @SuppressWarnings("rawtypes")
    private String getData(JoinPoint joinPoint) {
        Class targetClazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Method[] methods = targetClazz.getMethods();
        String data = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == args.length) {
                    data = method.getAnnotation(Logging.class).data();
                    break;
                }
            }
        }

        /**
         * 用参数替换占位符
         */
        Pattern pattern = Pattern.compile("\\{[\\d]\\}");
        Matcher matcher = pattern.matcher(data);
        while (matcher.find()) {
            String matcherStr = matcher.group();
            String indexStr = matcherStr.substring(1, matcherStr.length() - 1);
            int index = Integer.parseInt(indexStr);
            String value = "";
            if (index <= args.length) {
                value = covertArgsToString(args[index]);
            }
            data = data.replace(matcherStr, value);
        }
        return data;
    }

    private String covertArgsToString(Object obj) {
        if (obj instanceof Object[]) {
            Object[] arrs = (Object[]) obj;
            String str = "";
            for (Object item : arrs) {
                str += "," + String.valueOf(item);
            }
            str = str.length() > 1 ? str.substring(1) : "";
            return str;
        }
        return String.valueOf(obj);
    }

    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getRemoteAddr();
    }

    private Boolean hasOpen() {
        return env.getProperty(Constants.LOG.ENABLED_KEY, Boolean.class);
    }
}
