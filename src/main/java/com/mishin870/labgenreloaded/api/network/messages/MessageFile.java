package com.mishin870.labgenreloaded.api.network.messages;

import com.jsoniter.annotation.JsonIgnore;
import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

import java.io.File;

/**
 * Сообщение о:
 * <ul>
 *     <li>Полном приёме файла от пользователя (клиент -&gt; сервер)</li>
 *     <li>Запросе ввода файла (сервер -&gt; клиент)</li>
 * </ul>
 */
public class MessageFile extends Message {
	/**
	 * Мета запроса файла, либо мета запрошенного файла (если это ответ от клиента)
	 */
	public long meta;
	/**
	 * Заголовок окна выбора файла
	 */
	public String title;
	/**
	 * Текст в окне выбора файла
	 */
	public String text;
	/**
	 * Маска выбора файла (не гарантирует работу)<br>
	 * Пример маски: {@code .jar}
	 */
	public String mask;
	/**
	 * Отправленный серверу файл (если это ответ от клиента)
	 */
	@JsonIgnore
	public File file;
	
	public MessageFile() {}
	
	public MessageFile(long meta, String title, String text, String mask) {
		this.meta = meta;
		this.title = title;
		this.text = text;
		this.mask = mask;
	}
	
	@Override
	public boolean needServerPreprocess() {
		return true;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	@Override
	public int getCode() {
		return MessageController.MSG_FILE;
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageFile.class);
		}
	}
}
