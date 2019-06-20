package com.mishin870.labgenreloaded.api;

import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.WSListenerCallback;
import com.mishin870.labgenreloaded.api.network.messages.*;
import com.mishin870.labgenreloaded.api.network.special.MessageFileTooLarge;
import com.mishin870.labgenreloaded.api.network.special.MessageJavaTestResult;
import com.mishin870.labgenreloaded.api.network.special.MessageStart;
import com.mishin870.labgenreloaded.api.utils.UtilsBundle;
import org.eclipse.jetty.websocket.api.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Рабочая логика плагина<br>
 * Общается с пользователем и решает, что делать<br>
 * Реализация этого класса <b>обязательна</b>
 */
public abstract class PluginBase {
	private final WSListenerCallback callback;
	
	/**
	 * Фабрика, создавшая этот экземпляр
	 */
	protected final PluginFactory factory;
	/**
	 * Набор серверных утилит для различных задач
	 */
	protected final UtilsBundle utilsBundle;
	/**
	 * Сессия вебсокета, с помощью которой можно отправлять
	 * сообщения пользователю
	 */
	protected final Session session;
	/**
	 * Идентификатор пользователя, запустившего этот плагин
	 */
	protected final long userId;
	/**
	 * Идентификатор действия, которое запросил пользователь
	 */
	protected final String action;
	
	public PluginBase(PluginFactory factory, UtilsBundle utilsBundle, WSListenerCallback callback, Session session, long userId, String action) {
		this.factory = factory;
		this.utilsBundle = utilsBundle;
		this.callback = callback;
		this.session = session;
		this.userId = userId;
		this.action = action;
	}
	
	/**
	 * Обёртка для отправки алерта с указанной метой, текстом и заголовком
	 *
	 * @param meta мета
	 * @param text текст алерта
	 * @param title заголовок окна
	 */
	protected final void alert(long meta, String text, String title) {
		new MessageAlert(meta, text, title).send(session);
	}
	/**
	 * Обёртка для отправки алерта с указанным текстом и заголовком
	 *
	 * @param text текст алерта
	 * @param title заголовок окна
	 */
	protected final void alert(String text, String title) {
		new MessageAlert(-1, text, title).send(session);
	}
	/**
	 * Обёртка для отправки ошибки с указанным текстом
	 *
	 * @param text текст ошибки
	 */
	protected final void error(String text) {
		new MessageAlert(-1, text, "Ошибка").send(session);
	}
	/**
	 * Перезагрузить страницу у пользователя
	 */
	protected final void reloadPage() {
		new MessageReloadPage().send(session);
	}
	
	/**
	 * Остановить сессию и закрыть соединение с пользователем<br>
	 * Без вызова этой функции сессия продолжит висеть и пользователь не сможет
	 * ещё раз запустить действие плагина
	 */
	protected final void stop() {
		this.callback.disconnect();
	}
	
	/**
	 * Переместить файл из {@code source} в {@code destination} с заменой
	 * @param source источник
	 * @param destination назначение
	 * @throws IOException в случае ошибок ввода-вывода
	 */
	protected final void moveWithOverwrite(File source, File destination) throws IOException {
		Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	private void defaultOnMessageStub(Message message) {
		this.error(String.format(
				"Сообщение %s не поддерживается плагином лабораторной работы",
				message.getClass().getName()
		));
		this.stop();
	}
	
	// Visitor pattern
	public void onMessage(MessageAlert alert) {
		defaultOnMessageStub(alert);
	}
	public void onMessage(MessageClose close) {
		defaultOnMessageStub(close);
	}
	public void onMessage(MessageFile file) {
		defaultOnMessageStub(file);
	}
	public void onMessage(MessageInput input) {
		defaultOnMessageStub(input);
	}
	public void onMessage(MessageJavaTestResult javaTestResult) {
		defaultOnMessageStub(javaTestResult);
	}
	public void onMessage(MessageStart start) {
		defaultOnMessageStub(start);
	}
	public void onMessage(MessageReloadPage reloadPage) {
		defaultOnMessageStub(reloadPage);
	}
	public void onMessage(MessageFileTooLarge fileTooLarge) {
		error(String.format(
				"Вы пытаетесь отправить слишком большой файл (%s). Максимальный размер файла - %s",
				fileTooLarge.getFileSizeText(),
				fileTooLarge.getMaxFileSizeText()
		));
		this.stop();
	}
}
