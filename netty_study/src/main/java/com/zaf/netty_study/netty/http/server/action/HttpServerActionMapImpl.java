package com.zaf.netty_study.netty.http.server.action;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaf.netty_study.netty.http.util.IMyApplicationContext;

@Component
public class HttpServerActionMapImpl implements IHttpServerActionMap {

	private final static Map<String, IHttpServerAction> actionMap = new HashMap<String, IHttpServerAction>();

	@Autowired
	private IMyApplicationContext applicationContext;

	@PostConstruct
	public final void initAactionMap() {

		actionMap.put("^/hello$", getHttpServerAction("helloAction"));
	}

	/**
	 * According to the action name get IHttpServerAction instance
	 * 
	 * @param IHttpServerAction
	 *            httpServerAction name
	 * 
	 * @return the IHttpServerAction to which the specified key is mapped
	 * 
	 * @exception NoSuchElementException
	 *                if there is no value present
	 */
	@Override
	public final IHttpServerAction get(String uri) {
		return actionMap.get(getActionName(uri));
	}

	/**
	 * according the http uri, get the matched httpServer action<br>
	 * 
	 * NOTICE: Iuse java8 stream funtion, <br>
	 * if there is no value present, here will throw
	 * {@code NoSuchElementException}.<br>
	 * I can catch it and return null value, but I don't do it.<br>
	 * I'm worried about catch it will impact of performance. <br>
	 * if you must return null value, you can modify it.<br>
	 * 
	 * @param uri
	 *            http request or request uri
	 * @return the non-null value held by actionMap
	 * @exception NoSuchElementException
	 *                if there is no value present
	 */
	private final String getActionName(String uri) {
		return actionMap.keySet().stream().filter((prefixReg) -> (uri.matches(prefixReg))).findFirst().get();
	}

	private final IHttpServerAction getHttpServerAction(String beanName) {
		return (IHttpServerAction) applicationContext.getBean(beanName);
	}
}
