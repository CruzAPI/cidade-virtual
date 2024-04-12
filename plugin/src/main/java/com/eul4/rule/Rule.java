package com.eul4.rule;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Rule<A extends GenericAttribute>
{
	private final Map<Integer, A> rules;
	private final A defaultAttribute;
	
	public Rule(A defaultAttribute)
	{
		this.rules = new HashMap<>();
		this.defaultAttribute = defaultAttribute;
	}
	
	public void setRule(int level, A attribute)
	{
		rules.put(level, attribute);
	}
	
	public A getAttribute(int level)
	{
		return rules.get(level);
	}
	
	public A getAttributeOrDefault(int level)
	{
		return rules.getOrDefault(level, defaultAttribute);
	}
	
	public boolean hasAttribute(int level)
	{
		return rules.containsKey(level);
	}
}
