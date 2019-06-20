package com.mishin870.labgenreloaded.api.network.messages;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Сообщение о том, что пользователь закрыл окно ввода текста/файла (клиент -&gt; сервер)
 */
public class MessageClose extends Message {
	/**
	 * Мета исходного сообщения ввода текста/файла
	 */
	public long meta;
	
	public MessageClose() {}
	
	@Override
	public int getCode() {
		return MessageController.MSG_CLOSE;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageClose.class);
		}
	}
}
