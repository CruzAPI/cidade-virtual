package com.eul4.wrapper;

import com.eul4.common.util.CommonMessageUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;

import static com.eul4.common.util.ComponentUtil.ALERT_SYMBOL;
import static com.eul4.common.util.ComponentUtil.INCORRECT_SYMBOL;

@RequiredArgsConstructor
public class StackedEnchantment
{
	@Getter
	private final Enchantment enchantment;
	@Getter
	private final int level;
	private final boolean downgraded;
	
	public boolean wasDowngraded()
	{
		return downgraded;
	}
	
	public Component getComponent()
	{
		Component levelComponent;
		
		if(level == 0)
		{
			levelComponent = INCORRECT_SYMBOL;
		}
		else
		{
			levelComponent = Component.text(CommonMessageUtil.intToRoman(level));
			
			if(downgraded)
			{
				levelComponent = levelComponent.appendSpace().append(ALERT_SYMBOL);
			}
		}
		
		return Component.translatable(enchantment.translationKey())
				.append(Component.text(": "))
				.append(levelComponent);
	}
}
