package com.mishin870.labgenreloaded.api.java;

import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.java.shadow.PrintStreamShadow;
import com.mishin870.labgenreloaded.api.network.special.MessageJavaTestResult;
import com.mishin870.labgenreloaded.api.types.LabState;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Результат тестирования Java приложения<br>
 * Возвращается результатом вызова {@link JavaTester#test(List, Class, PrintStreamShadow, long, LabState)}
 * и поступает в виде сообщения {@link MessageJavaTestResult} к {@link PluginBase}
 */
public class TestResult implements Serializable {
	/**
	 * Успех тестирования
	 */
	public boolean success;
	/**
	 * Текст сообщения об ошибке/успехе
	 */
	public String text;
	/**
	 * Возникшее исключение, если это сообщение об ошибке из-за
	 * исключенияы
	 */
	public Throwable throwable;
	
	public TestResult() {}
	
	public TestResult(boolean success, String text) {
		this(success, text, null);
	}
	
	public TestResult(boolean success, String text, Throwable throwable) {
		this.success = success;
		this.text = text;
		this.throwable = throwable;
	}
	
	/**
	 * @return возвращает результирующий текст с учётом наличия/отсутствия {@link #text} и/или {@link #throwable}
	 */
	public String getFinalMessage() {
		if (this.text != null) {
			return this.text + "\n" + (this.throwable != null ? ExceptionUtils.getStackTrace(this.throwable) : "");
		} else {
			if (this.throwable != null) {
				return ExceptionUtils.getStackTrace(this.throwable);
			} else {
				return "[Пустой ответ]";
			}
		}
	}
	
	@Override
	public String toString() {
		return "TestResult{" +
				"success=" + success +
				", text='" + text + '\'' +
				", throwable=" + throwable +
				'}';
	}
}
