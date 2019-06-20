package com.mishin870.labgenreloaded.api.network;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.network.messages.*;
import com.mishin870.labgenreloaded.api.network.special.MessageJavaTestResult;
import com.mishin870.labgenreloaded.api.network.special.MessageStart;

import java.io.IOException;

/**
 * Сетевая часть сервера<br>
 * Читает сообщения с канала связи
 */
public class MessageController {
	private static int INDEX = 0;
	public static final int MSG_ALERT = INDEX++;
	public static final int MSG_CLOSE = INDEX++;
	public static final int MSG_INPUT = INDEX++;
	public static final int MSG_START = INDEX++;
	public static final int MSG_FILE = INDEX++;
	public static final int MSG_JAVA_TEST_RESULT = INDEX++;
	public static final int MSG_RELOAD_PAGE = INDEX++;
	
	private static IMessageAbstractFactory[] factories;
	
	static {
		factories = new IMessageAbstractFactory[INDEX];
		INDEX = 0;
		factories[INDEX++] = new MessageAlert.Factory();
		factories[INDEX++] = new MessageClose.Factory();
		factories[INDEX++] = new MessageInput.Factory();
		factories[INDEX++] = new MessageStart.Factory();
		factories[INDEX++] = new MessageFile.Factory();
		factories[INDEX++] = new MessageJavaTestResult.Factory();
		factories[INDEX++] = new MessageReloadPage.Factory();
	}
	
	/**
	 * Прочесть и пропарсить сообщение из строки текста
	 *
	 * @param input исходная строка текста
	 * @return обработанное сообщение
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	public static Message read(String input) throws IOException {
		Any data = JsonIterator.deserialize(input);
		int code = data.toInt("code");
		
		try {
			return factories[code].create(data.get("data"));
		} catch (IndexOutOfBoundsException e) {
			throw new IOException("Неизвестный код сетевого пакета: " + code);
		}
	}
}
