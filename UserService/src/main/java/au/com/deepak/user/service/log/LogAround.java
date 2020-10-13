package au.com.deepak.user.service.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Aspect
@Component
public class LogAround {

	final static Logger logger = LogManager.getLogger(LogAround.class);
	
	@Around("execution(* au.com.aemo.mock.*Controller.*(..)) && args(headers, ..)")
	public Object log(ProceedingJoinPoint proceedingJoinPoint, MultiValueMap<String, String> headers) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
		logger.info("=========================  " + methodSignature.getName() + "  ==================");
		Utils.printAllHeaders(headers);
		Object result = proceedingJoinPoint.proceed();
		logger.info("=====================================================================================");
		return result;		
	}
	
	// https://howtodoinjava.com/log4j2/log4j-2-xml-configuration-example/
}
