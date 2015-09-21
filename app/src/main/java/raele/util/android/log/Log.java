package raele.util.android.log;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class Log {
	
	private Log() {}
	
	private static final boolean ACTIVE = true;
	private static final String TAG = "trace.";
	
	private static int t = 0;
	private static StringBuilder builder = new StringBuilder();
	
	public static void begin()
	{
		if (ACTIVE) {
			String classMethod = getClassMethod();
			android.util.Log.d("" + TAG + classMethod, space() + " -> " + classMethod);
			t++;
			builder.append("    ");
		}
	}
	
	public static void end()
	{
		if (ACTIVE) {
			t--;
			builder = new StringBuilder();
			for (int i = 0; i < t; i++)
			{
				builder.append("    ");
			}
			String classMethod = getClassMethod();
			android.util.Log.d("" + TAG + classMethod, space() + " <- " + classMethod + (t == 0 ? "\n" : ""));
		}
	}
	
	public static void info(Object msg)
	{
		if (ACTIVE) {
			String classMethod = getClassMethod();
			android.util.Log.d("" + TAG + classMethod, space() + msg);
		}
	}

	public static void error(Object msg)
	{
		if (ACTIVE) {
			String classMethod = getClassMethod();
			android.util.Log.e("" + TAG + classMethod, space() + msg);
		}
	}
	
	private static String space()
	{
		if (ACTIVE) {
			return builder.toString();
		} else {
			return null;
		}
	}
	
	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @return method name
	 */
	private static String getClassMethod()
	{
		if (ACTIVE) {
			// Based on code found here: http://stackoverflow.com/questions/442747/getting-the-name-of-the-current-executing-method
			final int index = 4;
			StackTraceElement[] ste = Thread.currentThread().getStackTrace();
			StackTraceElement target = ste[index];
			String methodName = target.getMethodName();
			String className = target.getClassName().replaceAll(".*\\.", "");
			return className + '.' + methodName;
		} else {
			return null;
		}
	}

	public static void printStackTrace(Throwable e)
	{
		if (ACTIVE) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			error(sw.toString());
		}
	}

}
