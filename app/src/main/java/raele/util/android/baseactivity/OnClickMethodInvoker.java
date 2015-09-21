package raele.util.android.baseactivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import raele.util.android.log.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickMethodInvoker implements OnClickListener {
	
	private Object object;
	private Method method;

	public OnClickMethodInvoker(Object toInvoke, String methodName)
	throws IllegalArgumentException
	{
		Log.begin();
		try {
			object = toInvoke;
			method = toInvoke.getClass().getMethod(methodName, View.class);
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage());
			throw new IllegalArgumentException(e);
		} finally {
			Log.end();
		}
	}

	public OnClickMethodInvoker(Object toInvoke, Method method)
	{
		this.object = toInvoke;
		this.method = method;
	}

	@Override
	public void onClick(View v) {
		Log.begin();
		boolean accessible = method.isAccessible();
		try {
			method.setAccessible(true);
			method.invoke(object, v);
		} catch (IllegalAccessException e) {
			Log.error("Got IllegalAccessException: " + e.getMessage());
		} catch (InvocationTargetException e) {
			Log.printStackTrace(e.getCause());
		} catch (IllegalArgumentException e) {
			Log.error("Couldn't execute method " + method.getName());
			Log.error("Make sure this method accept only one parameter of type View.");
			Log.printStackTrace(e);
		} finally {
			method.setAccessible(accessible);
			Log.end();
		}
	}
	
}