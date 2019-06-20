package com.mishin870.labgenreloaded.api.network.messages;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Запрос перезагрузки страницы у пользовательского клиента (сервер -&gt; клиент)
 */
public class MessageReloadPage extends Message {
	
	public MessageReloadPage() {}
	
	@Override
	public int getCode() {
		return MessageController.MSG_RELOAD_PAGE;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageReloadPage.class);
		}
	}
}
