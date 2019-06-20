package com.mishin870.labgenreloaded.api.network.messages;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Сообщение о:
 * <ul>
 *     <li>Результате ввода текста от пользователя (клиент -&gt; сервер)</li>
 *     <li>Запросе ввода текста (сервер -&gt; клиент)</li>
 * </ul>
 */
public class MessageInput extends Message {
	/**
	 * Мета запроса ввода текста, либо мета запрошенного ввода текста (если это ответ от клиента)
	 */
	public long meta;
	/**
	 * Значение по умолчанию в окне запроса ввода текста, либо
	 * результат ввода текста (если это ответ от клиента)
	 */
	public String text;
	/**
	 * Отображаемые вопрос в окне
	 */
	public String question;
	/**
	 * Заголовок окна
	 */
	public String title;
	
	public MessageInput() {}
	
	public MessageInput(long meta, String text, String question, String title) {
		this.meta = meta;
		this.text = text;
		this.question = question;
		this.title = title;
	}
	
	public MessageInput(long meta, String text, String question) {
		this(meta, text, question, "Ввод");
	}
	
	public MessageInput(String text) {
		this(-1, text, "", "Ввод");
	}
	
	@Override
	public int getCode() {
		return MessageController.MSG_INPUT;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageInput.class);
		}
	}
}
