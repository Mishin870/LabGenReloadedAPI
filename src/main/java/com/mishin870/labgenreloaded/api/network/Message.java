package com.mishin870.labgenreloaded.api.network;

import com.jsoniter.output.JsonStream;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.messages.MessageFile;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

/**
 * Сущность, определяющая базовый функционал для сетевых сообщений
 * между клиентом на JavaScript и данным сервером на Java
 */
public abstract class Message {
	
	/**
	 * @return код сообщения (используется только в {@link MessageController})
	 */
	public abstract int getCode();
	
	/**
	 * Отправить сообщение пользователю (клиенту)
	 *
	 * @param session сессия клиента
	 */
	public final void send(Session session)  {
		try {
			if (!session.isOpen()) {
				throw new IOException("Соединение с клиентом было разорвано");
			}
			session.getRemote().sendString(JsonStream.serialize(
					new MessageCover(
							getCode(),
							this
					)
			));
		} catch (IOException ioe) {
			session.close();
		}
	}
	
	/**
	 * Вспомогательная функция для реализации паттерна Visitor
	 *
	 * @param pluginBase плагин, который необходимо "посетить" сообщению
	 */
	public abstract void visit(PluginBase pluginBase);
	
	/**
	 * @return требует ли сообщение особой предварительной обработки сервером?
	 *          Например, сообщение {@link MessageFile} удерживается сервером до полного
	 *          прихода бинарных данных файла от клиента
	 */
	public boolean needServerPreprocess() {
		return false;
	}
	
	/**
	 * Обёртка сообщения для его конвертации в JSON и отправки пользователю
	 */
	private static class MessageCover {
		int code;
		Message data;
		
		public MessageCover(int code, Message data) {
			this.code = code;
			this.data = data;
		}
	}
}
