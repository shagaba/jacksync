package com.shagaba.jacksync.diff;

import com.shagaba.jacksync.exception.JacksyncDiffException;

public interface DiffEngine<R, E> {
	
	public <T extends E> R diff(T source, T target) throws JacksyncDiffException;

}
