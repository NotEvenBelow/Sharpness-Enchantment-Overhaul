/**

package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.mixin;

import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config.Sharpness_enchantment_overhaulConfig;
import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.util.AttackContext;
import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageEnchantment.class)
public abstract class SharpnessPerEntityBlockMixin {

    /**
     * Combines all Sharpness filtering logic into one injection.
     * This mixin will:
     * 1. Check if the 'excludeSmiteAndBaneTargets' config is enabled and filter by EntityGroup.
     * 2. Check the per-entity exclusion list from the config ('sharpnessExcludedEntityIds').
     */
    /**@Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void seo$filterSharpness(int level, EntityGroup group, CallbackInfoReturnable<Float> cir) {

        if (((Enchantment)(Object)this) != Enchantments.SHARPNESS) return;

        var cfg = Sharpness_enchantment_overhaulConfig.get();


        if (cfg.excludeSmiteAndBaneTargets) {
            if (group == EntityGroup.UNDEAD || group == EntityGroup.ARTHROPOD) {
                cir.setReturnValue(0.0F);
                return; // Exit early
            }
        }


        LivingEntity target = AttackContext.getTarget();


        if (target == null) return;

        if (cfg.isSharpnessExcluded(target.getType())) {

            cir.setReturnValue(0.0F);
        }
    }
} **/