package hu.elte.txtuml.export.uml2.transform.aspects;

import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.Collection;
import hu.elte.txtuml.export.uml2.utils.ImportWarningProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * This aspect contains advices supporting model import.
 * @author Adam Ancsin
 *
 */
public privileged aspect ImporterSupportingAspect extends AbstractImporterAspect {
	
	/**
	 * This advice shows an error message when a non-txtUML method is called from a method body
	 * during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Object around() : 
		call(* (!ModelElement+).*(..)) && 
		!call(* (java.lang..*).*(..)) &&
		!call(* (Collection+).*(..)) 
		&& isActive()
	{
		ImportWarningProvider.createWarning(
				"non-txtUML method call - " +
				thisJoinPoint.getSignature().getDeclaringType().getName() + 
				"." + 
				thisJoinPoint.getSignature().getName()
			);
		return proceed();
	}
	

	/**
	 * This advice hides all the synthetic methods from the result of Class.getDeclaredMethods() calls.
	 * It is needed to hide the private methods generated by AspectJ.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Method[] around(Object c) : target(c) && call(Method[] Class.getDeclaredMethods()) && withinExportUML2() {
		LinkedList<Method> methods = new LinkedList<>();
		for(Method m : proceed(c)) {
			if (!m.isSynthetic()) {
				methods.add(m);
			}
		}
		return methods.toArray(new Method[0]);
	}
	
	/**
	 * This advice hides all the synthetic fields from the result of Class.getDeclaredFields() calls.
	 * It is needed to hide the fields generated by the Java compiler. (e.g. this$0)
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	Field[] around(Object c) : target(c) && call(Field[] Class.getDeclaredFields()) && withinExportUML2() {
		LinkedList<Field> fields = new LinkedList<>();
		for(Field f : proceed(c)) {
			if (!f.isSynthetic()) {
				fields.add(f);
			}
		}
		return fields.toArray(new Field[0]);
	}
	
	/**
	 * This advice prevents execution of Action.start if called from outside the model (e.g. from glue code)
	 * during model import, so none of the ModelClass instances start functioning. 
	 *
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): call(void Action.start(..)) && importing() && !withinModel()
	{
		//do nothing
	}
	
	/**
	 * This advice prevents logging in models during model import.
	 * 
	 * @author Adam Ancsin
	 */
	@SuppressAjWarnings
	void around(): 
		(
			call(void Action.log(..)) || 
			call(void Action.logError(..)) ||
			call(void Action.executorLog(..)) ||
			call(void Action.executorFormattedLog(..)) ||
			call(void Action.executorErrorLog(..))
		)
		&& isActive()
	{
		//do nothing
	}
}