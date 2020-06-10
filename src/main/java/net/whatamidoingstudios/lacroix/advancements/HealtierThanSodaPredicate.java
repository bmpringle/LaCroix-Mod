package net.whatamidoingstudios.lacroix.advancements;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.whatamidoingstudios.lacroix.ModObjects;

public class HealtierThanSodaPredicate extends ItemPredicate {
	public HealtierThanSodaPredicate(JsonObject jsonObject) { 
		
	}
	 @Override
	public boolean test(ItemStack item) {
		if(item.getItem() == ModObjects.purelacroix) {
			return true;
		}
		return false;
	}
}
