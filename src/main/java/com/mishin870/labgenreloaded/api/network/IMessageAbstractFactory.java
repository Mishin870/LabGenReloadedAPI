package com.mishin870.labgenreloaded.api.network;

import com.jsoniter.any.Any;

public interface IMessageAbstractFactory {
	Message create(Any data);
}
