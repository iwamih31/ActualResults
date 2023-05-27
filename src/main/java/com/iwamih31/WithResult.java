package com.iwamih31;

/**
 * method の return で
 * 返す object と一緒に
 * String の message
 * を返す為の class
 **/
public class WithResult {

	// 返却したい object
	Object object;

	// try～catch の結果と一緒に返すメッセージ
	String message;

	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
