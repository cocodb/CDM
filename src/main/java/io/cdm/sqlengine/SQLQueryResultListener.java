package io.cdm.sqlengine;


public interface SQLQueryResultListener<T> {

	public void onResult(T result);

}
