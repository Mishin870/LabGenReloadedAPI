package com.mishin870.labgenreloaded.api.network.special;

import com.jsoniter.any.Any;
import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.IMessageAbstractFactory;
import com.mishin870.labgenreloaded.api.network.Message;
import com.mishin870.labgenreloaded.api.network.MessageController;

/**
 * Идентификационное сообщение о старте работы плагина, которое обрабатывается
 * самим сервером, а не плагином. Содержит в себе указанное действие к выполнению ({@link #actionId}),
 * которое будет передано плагину по {@link #courseId} и {@link #labId} вместе с информацией {@link #userId}
 */
public class MessageStart extends Message {
	/**
	 * Идентификатор пользователя
	 */
	public long userId;
	/**
	 * Идентификатор лабораторной работы
	 */
	public long labId;
	/**
	 * Код курса
	 */
	public String courseId;
	/**
	 * Действие, запрошенное клиентом
	 */
	public String actionId;
	
	public MessageStart() {}
	
	public MessageStart(long userId, long labId, String courseId, String actionId) {
		this.userId = userId;
		this.courseId = courseId;
		this.labId = labId;
		this.actionId = actionId;
	}
	
	@Override
	public int getCode() {
		return MessageController.MSG_START;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
	
	public static class Factory implements IMessageAbstractFactory {
		@Override
		public Message create(Any data) {
			return data.as(MessageStart.class);
		}
	}
}
