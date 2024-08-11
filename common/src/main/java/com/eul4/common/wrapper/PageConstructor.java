package com.eul4.common.wrapper;

import com.eul4.common.Common;

import java.util.List;

@FunctionalInterface
public interface PageConstructor<T>
{
	Page<T> newInstance(Common plugin, int index, int pageSize, List<T> pageElements, List<T> fullList);
}
