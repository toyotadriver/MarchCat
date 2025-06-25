package marchcat.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
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
	
	@Before(value = "mcPictures()")
	public void logPictures(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}
	@Before(value = "mcStorage()")
	public void logStorage(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}
	@AfterThrowing(value = "mcStorage()")
	public void logStorageExceptions(JoinPoint joinPoint) {
		logger.info(logClassCallInfo(joinPoint));
	}
	
	@AfterThrowing(pointcut = "mcPicturesExceptions()", throwing = "e")
	public void logPicturesExceptions(JoinPoint joinPoint, Throwable e) {
		logger.info(logExceptions(joinPoint, e));
	}
	
	@AfterThrowing(pointcut = "mcControllersExceptions()", throwing = "e")
	public void logControllersExceptions(JoinPoint joinPoint, Throwable e) {
		logger.info(logExceptions(joinPoint, e));
	}
	
	@AfterThrowing(pointcut = "mcStorage()", throwing = "e")
	public void logStorageExceptions(JoinPoint joinPoint, Throwable e) {
		logger.info(logExceptions(joinPoint, e));
	}

	private String logClassCallInfo(JoinPoint joinPoint) {
		
		Signature signature = joinPoint.getSignature();
		Class clazz = signature.getDeclaringType();
		
		String out = "Called class: " + clazz.getName() +
				" | method: " + signature.getName() +
				" | arguments: " + joinPoint.getArgs().toString();
		return out;
	}
	
	/**
	 * Log the exception and the cause (if != null).
	 * @param joinPoint
	 * @param e
	 * @return
	 */
	private String logExceptions(JoinPoint joinPoint, Throwable e) {
		StringBuilder message = new StringBuilder();
		message.append(logClassCallInfo(joinPoint));
		message.append("Thrown an exception: \n");
		message.append(e.getMessage() + "\n");
		if(e.getCause() != null)
			message.append("Caused by: " + e.getCause().getMessage());
		return message.toString();
	}
	
	@Pointcut("execution(public * marchcat.pictures.*.*(..))")
	private void mcPictures() {};
	
	@Pointcut("execution(public * marchcat.*.*.*(..))")
	private void mcAll() {}
	
	@Pointcut("execution(public * marchcat.users.*.*(..))")
	private void mcUsers() {}
	
	@Pointcut("execution( * marchcat.controllers.*.*(..))")
	private void mcControllers() {}
	
	@Pointcut("execution( * marchcat.controllers.*.*(..))")
	private void mcControllersExceptions() {}
	
	@Pointcut("execution( * marchcat.pictures.*.*(..))")
	private void mcPicturesExceptions() {}
	
	@Pointcut("execution( * marchcat.storage.*.*(..))")
	private void mcStorage() {}

}
