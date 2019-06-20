package com.mishin870.labgenreloaded.api.java.shadow;

/**
 * Возникает в случае вызова не реализованной функциональности в теневом объекте
 * См. также {@link ShadowType}
 */
public class ShadowNotImplementedException extends RuntimeException {
	
	public ShadowNotImplementedException(ShadowType shadowType, String functionName) {
		super("Данная функция запрещена к использованию: " + shadowType.getName() + "::" + functionName);
	}
}
