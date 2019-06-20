package com.mishin870.labgenreloaded.api.network.messages;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Сообщение для отображения алерта у пользователя (клиент -&gt; сервер)<br>
 * Алерт - это сообщение с одной кнопкой "ок"
 */
public class MessageAlert extends Message {
	/**
	 * Текст внутри алерта
	 */
	public String text;
	/**
	 * Заголовок окна алерта
	 */
	public String title;
	/**
	 * Мета алерта
	 */
	public long meta;
	
	public MessageAlert() {}
	
	public MessageAlert(long meta, String text, String title) {
		this.meta = meta;
		this.text = text;
		this.title = title;
	}
	
	public MessageAlert(long meta, String text) {
		this(meta, text, "");
	}
	
	public MessageAlert(String text) {
		this(-1, text, "");
	}
	
	@Override
	public int getCode() {
		return MessageController.MSG_ALERT;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageAlert.class);
		}
	}
}
