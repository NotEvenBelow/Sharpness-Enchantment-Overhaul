package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.mixin;

import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config.Sharpness_enchantment_overhaulConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Enchantment.class)
public abstract class EqualizeDamageWeightsMixin {
    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    private void seo$equalizeRarity(CallbackInfoReturnable<Rarity> cir) {
        if (!Sharpness_enchantment_overhaulConfig.get().equalWeightForDamageEnchantments) return;

        Enchantment self = (Enchantment) (Object) this;
        if (self == Enchantments.SHARPNESS
                || self == Enchantments.SMITE
                || self == Enchantments.BANE_OF_ARTHROPODS) {
            cir.setReturnValue(Rarity.UNCOMMON);
        }
    }
}