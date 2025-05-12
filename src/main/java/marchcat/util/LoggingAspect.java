package marchcat.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	private Logger logger;
	
	public LoggingAspect() {
		this.logger = Logger.getLogger(LoggingAspect.class.getName());
		ConsoleHandler consoleHandler = new ConsoleHandler();
		logger.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
	}
	
	@Before(value = "mcControllers()")
	public void logControllers(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}
	
	@Before(value = "mcUsers()")
	public void logUsers(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}

	private String logClassCallInfo(JoinPoint joinPoint) {
		
		Signature signature = joinPoint.getSignature();
		Class clazz = signature.getDeclaringType();
		
		String out = "Called class: " + clazz.getName() +
				" | method: " + signature.getName() +
				" | arguments: " + joinPoint.getArgs().toString();
		return out;
	}
	
	@Pointcut("execution(public * marchcat.*.*.*(..))")
	private void mcAll() {}
	
	@Pointcut("execution(public * marchcat.users.*.*(..))")
	private void mcUsers() {}
	
	@Pointcut("execution(public * marchcat.controllers.*.*(..))")
	private void mcControllers() {}
}
