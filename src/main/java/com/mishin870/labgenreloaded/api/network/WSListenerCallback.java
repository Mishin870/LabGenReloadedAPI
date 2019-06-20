package com.mishin870.labgenreloaded.api.network;

/**
 * Обратная связь с хранителем вебсокета
 */
public interface WSListenerCallback {
	/**
	 * Закрыть сессию вебсокета, чтобы пользователь смог
	 * снова активировать действия плагинов
	 */
	void disconnect();
}
