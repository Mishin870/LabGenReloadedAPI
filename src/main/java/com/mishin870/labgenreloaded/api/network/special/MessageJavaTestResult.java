package com.mishin870.labgenreloaded.api.network.special;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.java.TestResult;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Внутреннее сообщение о результате тестирования Java приложения
 * от тестировочного процесса (тестер -&gt; сервер)
 */
public class MessageJavaTestResult extends Message {
	/**
	 * Итоговый результат тестирования
	 */
	public TestResult testResult;
	
	public MessageJavaTestResult() {}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public MessageJavaTestResult(TestResult testResult) {
		this.testResult = testResult;
	}
	
	@Override
	public int getCode() {
		return MessageController.MSG_JAVA_TEST_RESULT;
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageJavaTestResult.class);
		}
	}
}
