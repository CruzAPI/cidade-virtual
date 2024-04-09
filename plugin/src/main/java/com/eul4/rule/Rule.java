package com.eul4.rule;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ToString
public class Rule<A extends GenericAttribute>
{
	public final Map<Integer, A> rules;
	
	public Rule()
	{
		this(new HashMap<>());
	}
	
	public void setRule(int level, A attribute)
	{
		rules.put(level, attribute);
	}
	
	public A getAttribute(int level)
	{
		return rules.get(level);
	}
	
	public boolean hasAttribute(int level)
	{
		return rules.containsKey(level);
	}
}
