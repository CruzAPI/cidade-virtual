package com.eul4.listener;

import com.eul4.Main;
import com.eul4.enums.Rarity;
import com.eul4.util.RarityUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class VillagerRarityListener implements Listener
{
	private final Main plugin;
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(InventoryOpenEvent event)
	{
		if(!(event.getInventory() instanceof MerchantInventory merchantInventory)
				|| (!(merchantInventory.getHolder() instanceof Villager villager)))
		{
			return;
		}
		
		Rarity villagerRarity = RarityUtil.getRarity(villager);
		Merchant merchant = merchantInventory.getMerchant();
		
		for(int i = 0; i < merchant.getRecipeCount(); i++)
		{
			MerchantRecipe merchantRecipe = merchant.getRecipe(i);
			
			List<ItemStack> ingredients = new ArrayList<>();
			
			for(ItemStack ingredient : merchantRecipe.getIngredients())
			{
				ingredients.add(RarityUtil.setRarity(ingredient, villagerRarity));
			}
			
			ItemStack result = merchantRecipe.getResult();
			result = RarityUtil.setRarity(result, villagerRarity);
			
			MerchantRecipe merchantRecipeClone = new MerchantRecipe(merchantRecipe);
			
			merchantRecipeClone.setResult(result);
			merchantRecipeClone.setIngredients(ingredients);
			
			merchantRecipeClone.setMaxUses(merchantRecipe.getMaxUses());
			merchantRecipeClone.setDemand(merchantRecipe.getDemand());
			merchantRecipeClone.setVillagerExperience(merchantRecipe.getVillagerExperience());
			merchantRecipeClone.setSpecialPrice(merchantRecipe.getSpecialPrice());
			merchantRecipeClone.setIgnoreDiscounts(merchantRecipe.shouldIgnoreDiscounts());
			merchantRecipeClone.setUses(merchantRecipe.getUses());
			merchantRecipeClone.setPriceMultiplier(merchantRecipe.getPriceMultiplier());
			merchantRecipeClone.setExperienceReward(merchantRecipe.hasExperienceReward());
			
			merchant.setRecipe(i, merchantRecipeClone);
		}
	}
}
