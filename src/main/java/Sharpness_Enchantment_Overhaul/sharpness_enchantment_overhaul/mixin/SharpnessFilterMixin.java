/**package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.mixin;

import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config.Sharpness_enchantment_overhaulConfig;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class SharpnessFilterMixin {

    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void seo$filterSharpnessOnly(int level, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (!Sharpness_enchantment_overhaulConfig.get().excludeSmiteAndBaneTargets) return;


        if (((Enchantment) (Object) this) == Enchantments.SHARPNESS) {
            if (group == EntityGroup.UNDEAD || group == EntityGroup.ARTHROPOD) {
                cir.setReturnValue(0.0F);
            }
        }
    }
} **/
