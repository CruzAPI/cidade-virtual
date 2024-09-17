package com.eul4.i18n;

import com.eul4.common.i18n.BundleBaseName;
import com.eul4.common.i18n.Message;
import com.eul4.enums.Currency;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.Component.empty;

@Getter
public enum TutorialTownMessage implements Message
{
	BOSS_BAR_GO_TALK_WITH_ASSISTANT("boss-bar.go-talk-with-assistant"),
	BOSS_BAR_TALK_WITH_ASSISTANT("boss-bar.talk-with-assistant"),
	BOSS_BAR_COLLECT_LIKES("boss-bar.collect-likes"),
	BOSS_BAR_COLLECT_DISLIKES("boss-bar.collect-dislikes"),
	BOSS_BAR_UPGRADE_TOWN_HALL("boss-bar.upgrade-town-hall"),
	BOSS_BAR_WAIT_BUILD_FINISH("boss-bar.wait-finish-build"),
	BOSS_BAR_CLICK_TO_FINISH_BUILD("boss-bar.click-to-finish-build"),
	
	STEP_1("step-1", (bundle, args) -> new Component[]
	{
		empty(),
		(Component) args[0],
	}),
	
	STEP_2("step-2", (bundle, args) -> new Component[]
	{
		empty(),
		(Component) args[0],
	}),
	
	STEP_3("step-3", (bundle, args) -> new Component[]
	{
		empty(),
		(Component) args[0],
	}),
	
	STEP_4("step-4"),
	STEP_5("step-5"),
	STEP_6("step-6"),
	STEP_7("step-7"),
	STEP_8("step-8"),
	STEP_9("step-9"),
	STEP_10("step-10"),
	STEP_11("step-11"),
	
	STEP_12("step-12", (bundle, args) -> new Component[]
	{
		empty(),
		PluginMessage.BOLD_DECORATED_$CURRENCY_$VALUE.translate(bundle, Currency.LIKE, args[0])
	}),
	
	STEP_13("step-13"),
	STEP_14("step-14"),
	STEP_15("step-15"),
	STEP_16("step-16"),
	STEP_17("step-17"),
	STEP_18("step-18"),
	STEP_19("step-19"),
	STEP_20("step-20"),
	STEP_21("step-21"),
	STEP_22("step-22"),
	STEP_23("step-23"),
	STEP_24("step-24"),
	STEP_25("step-25"),
	
	TUTORIAL_FINISHED("tutorial-finished");
	
	private final String key;
	private final BundleBaseName bundleBaseName;
	private final BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction;
	
	TutorialTownMessage(String key)
	{
		this(key, empty());
	}
	
	TutorialTownMessage(String key, Component baseComponent)
	{
		this(key, (bundle, args) -> new Component[] { baseComponent });
	}
	
	TutorialTownMessage(String key, BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this(PluginBundleBaseName.TOWN_TUTORIAL, key, componentBiFunction);
	}
	
	TutorialTownMessage(BundleBaseName bundleBaseName,
			String key,
			BiFunction<ResourceBundle, Object[], Component[]> componentBiFunction)
	{
		this.bundleBaseName = bundleBaseName;
		this.key = key;
		this.componentBiFunction = componentBiFunction;
	}
}
