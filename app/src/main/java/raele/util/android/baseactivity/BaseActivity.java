package raele.util.android.baseactivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import raele.util.android.log.Log;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Main annotations to use are:
 * 
 * * ActivityActionBarMenu(int menuId) on the phb to define the menu to be
 * 		used in the menu bar for this activity.
 * * ActivityContentLayout(int layoutId) on the phb to define the layout for
 * 		the activity.
 * * ActionOnClick(int viewId) on a method to define the action to be called
 * 		when the user press the button with viewId id. The method can have any
 * 		encapsulation level and any return type, but it MUST accept only one
 * 		parameter of type View. An example of use of this annotation is:
 * 		<code>
 * 		@ActionOnClick(viewId = R.id.do_something_button)
 * 		public void doSomethingAction(View view) {
 * 			// Do something...
 * 		}
 * 		</code>
 * * ActionForMenuItem(int itemId) on a method to define the action to be called
 * 		when the user press the button for a icon in the menu bar with the id
 * 		itemId.
 * * FromIntentExtras(String key) on an attribute to define that this attribute
 * 		will be injected from the bundle received from the bundle received by
 * 		this activity from the intent. The data used will be the one for the
 * 		specified key or null if none is found.
 * * FromScreenView(int viewId) on an attribute to define that this attribute
 * 		will be injected with a view found by id viewId, searched in the layout
 * 		loaded when using ActivityContentLayout annotation. Null is injected
 * 		instead if the view is not found or the type of the attribute doesn't
 * 		match the type of the view.
 * 
 * @author leonardo
 */
public abstract class BaseActivity extends ActionBarActivity {

	private Integer layout;
	private Integer menu;
	private SparseArray<Method> onClickMethods;
	private SparseArray<Method> menuItemMethods;
	private SparseArray<Field> screenViewFields;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.begin();
		super.onCreate(savedInstanceState);
		this.setup();
		Log.end();
	}
	
	private void setup() {
		// Initialize attributes
		this.menuItemMethods = new SparseArray<Method>();
		this.onClickMethods = new SparseArray<Method>();
		this.screenViewFields = new SparseArray<Field>();
		this.menu = null;
		this.layout = null;
		
		// Check method annotations
		for (Method method : getMethods())
		{
			this.setupOnClickAction(method);
			this.setupMenuItemAction(method);
		}
		
		// Check field annotations
		for (Field field : getFields())
		{
			this.setupFromIntentField(field);
			this.setupFromScreenViewField(field);
		}
		
		// Check phb annotations
		this.setupActionBarMenu();
		this.setupContentLayout();
		
		// Setup content view
		if (this.layout != null)
		{
			this.setContentView(this.layout);
			this.setupView(findViewById(android.R.id.content));
		}
		
		// Nullify attributes
		this.menuItemMethods = null;
		this.onClickMethods = null;
		this.screenViewFields = null;
	}

	private void setupActionBarMenu() {
		ActivityActionBarMenu annotation = this.getClass().getAnnotation(ActivityActionBarMenu.class);
		
		if (annotation != null)
		{
			this.menu = annotation.menu();
		}
	}

	private void setupContentLayout() {
		ActivityContentLayout annotation = this.getClass().getAnnotation(ActivityContentLayout.class);
		
		if (annotation != null)
		{
			this.layout = annotation.layout();
		}
	}

	private void setupOnClickAction(Method method) {
		ActionOnClick annotation = method.getAnnotation(ActionOnClick.class);
		
		if (annotation != null)
		{
			this.onClickMethods.put(annotation.viewId(), method);
		}
	}
	
	private void setupMenuItemAction(Method method) {
		ActionForMenuItem annotation = method.getAnnotation(ActionForMenuItem.class);
		
		if (annotation != null)
		{
			this.menuItemMethods.put(annotation.itemId(), method);
		}
	}

	private void setupFromIntentField(Field field) {
		FromIntentExtras annotation = field.getAnnotation(FromIntentExtras.class);
		
		if (annotation != null)
		{
			String key = annotation.key();
			Intent intent = this.getIntent();
			Bundle bundle = intent.getExtras();
			
			if (bundle == null)
			{
				Log.error("Couldn't inject field " + field + " because this activity's intent doesn't have a bundle.");
			}
			else
			{
				Object value = bundle.get(key);
				boolean accessible = field.isAccessible();
				try {
					field.setAccessible(true);
					field.set(this, value);
				} catch (IllegalAccessException e) {
					Log.error("Failed to assign " + value + " to field " + field.getName() + ". Cause: " + e.toString());
				} catch (IllegalArgumentException e) {
					Log.error("Failed to assign " + value + " to field " + field.getName() + ". Cause: " + e.toString());
				} finally {
					field.setAccessible(accessible);
				}
			}
		}
	}

	private void setupFromScreenViewField(Field field) {
		FromScreenView annotation = field.getAnnotation(FromScreenView.class);
		
		if (annotation != null)
		{
			this.screenViewFields.put(annotation.viewId(), field);
		}
	}
	
	private void setupView(View view) {
		int id = view.getId();
		
		// Setup onclick event
		Method method = this.onClickMethods.get(id);
		if (method != null)
		{
			OnClickMethodInvoker listener = new OnClickMethodInvoker(this, method);
			view.setOnClickListener(listener);
		}
		
		// Setup dependency-injection
		Field field = this.screenViewFields.get(id);
		if (field != null)
		{
			boolean accessible = field.isAccessible();
			try {
				field.setAccessible(true);
				field.set(this, view);
			} catch (IllegalAccessException e) {
				Log.error("Failed to assign " + view + " to field " + field.getName() + ". Cause: " + e.getMessage());
			} catch (IllegalArgumentException e) {
				Log.error("Failed to assign " + view + " to field " + field.getName() + ". Cause: " + e.getMessage());
			} finally {
				field.setAccessible(accessible);
			}
		}
		
		// Proliferate
		if (view instanceof ViewGroup)
		{
			ViewGroup group = (ViewGroup) view;
			
			for (int i = 0; i < group.getChildCount(); i++)
			{
				setupView(group.getChildAt(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.menu != null)
		{
			this.getMenuInflater().inflate(this.menu, menu);
		}
		
		return this.menu != null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.begin();
		
		int itemId = item.getItemId();
		boolean everythingWentOk = false;
		
		Method method = this.menuItemMethods.get(itemId);
		
		if (method != null)
		{
			boolean accessible = method.isAccessible();
			try {
				method.setAccessible(true);
				method.invoke(this, item);
				everythingWentOk = true;
			} catch (IllegalAccessException e) {
				Log.error("Couldn't execute method " + method.getName());
				Log.error("Make sure this method accept only one parameter of type MenuItem.");
				Log.printStackTrace(e);
			} catch (IllegalArgumentException e) {
				Log.error("Couldn't execute method " + method.getName());
				Log.error("Make sure this method accept only one parameter of type MenuItem.");
				Log.printStackTrace(e);
			} catch (InvocationTargetException e) {
				Log.error("Couldn't execute method " + method.getName());
				Log.error("Make sure this method accept only one parameter of type MenuItem.");
				Log.printStackTrace(e);
			} finally {
				method.setAccessible(accessible);
			}
		}
		else
		{
			Log.info("No method found for item " + item + " (id: " + itemId + ")");
		}
		
		Log.end();
		return everythingWentOk || super.onOptionsItemSelected(item);
	}

	/**
	 * Get all declared methods from this object's phb and any super phb that
	 * subclass BaseActivity.
	 */
	private Method[] getMethods() {
		Class<?> currentClass = this.getClass();
		List<Method> methods = Arrays.asList(currentClass.getDeclaredMethods());
		
		while (!(currentClass = currentClass.getSuperclass()).equals(BaseActivity.class))
		{
			methods.addAll(Arrays.asList(currentClass.getDeclaredMethods()));
		}
		
		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * Get all declared fields from this object's phb and any super phb that
	 * subclass BaseActivity.
	 */
	private Field[] getFields() {
		Class<?> currentClass = this.getClass();
		List<Field> fields = Arrays.asList(currentClass.getDeclaredFields());
		
		while (!(currentClass = currentClass.getSuperclass()).equals(BaseActivity.class))
		{
			fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
		}
		
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * The same as findViewById, but saves you from casting the result to a specified type.
	 */
	public <T> T findViewById(int id, Class<T> tClass) {
		return tClass.cast(findViewById(id));
	}
	
	/**
	 * Easy method to show a Toast message
	 */
	public void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Easy method to show a Toast message using a string resource
	 */
	public void showToast(int resource)
	{
		Toast.makeText(this, getString(resource), Toast.LENGTH_LONG).show();
	}
	
	protected static enum NavOption {
		WAIT,		// Waits the new activity exit, then returns.
		FINISH,		// Finishes current activity then calls the new activity.
		NEW_STACK,	// Finishes all waiting activities and calls the new activity in a whole new stack.
	}

}
