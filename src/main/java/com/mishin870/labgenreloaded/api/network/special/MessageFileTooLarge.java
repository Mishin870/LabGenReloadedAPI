package com.mishin870.labgenreloaded.api.network.special;

import com.mishin870.labgenreloaded.api.PluginBase;
import com.mishin870.labgenreloaded.api.network.Message;

/**
 * Сообщение о том, что присланный пользователем файл превысил максимальный
 * размер файла, поддерживаемый сервером, и файл был отброшен (сервер -&gt; сервер)
 */
public class MessageFileTooLarge extends Message {
	/**
	 * Размер файла, который пользователь пытался отправить
	 */
	public long fileSize;
	/**
	 * Максимальный размер файла, поддерживаемый сервером
	 */
	public long maxFileSize;
	
	public MessageFileTooLarge(long fileSize, long maxFileSize) {
		this.fileSize = fileSize;
		this.maxFileSize = maxFileSize;
	}
	
	public String getFileSizeText() {
		return makeReadableSize(fileSize);
	}
	
	public String getMaxFileSizeText() {
		return makeReadableSize(maxFileSize);
	}
	
	private String makeReadableSize(long bytes) {
		final int UNIT = 1024;
		if (bytes < UNIT) {
			return bytes + " байт";
		}
		int exp = (int) (Math.log(bytes) / Math.log(UNIT));
		return String.format(
				"%.1f %cб",
				bytes / Math.pow(UNIT, exp),
				"КМГТПЭ".charAt(exp - 1)
		);
	}
	
	@Override
	public int getCode() {
		return -1;
	}
	
	@Override
	public void visit(PluginBase pluginBase) {
		pluginBase.onMessage(this);
	}
}
