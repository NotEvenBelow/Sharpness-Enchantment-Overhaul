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
public abstract class SharpnessScaleMixin {

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private void seo$scaleAndFilter(int level, EntityGroup group, CallbackInfoReturnable<Float> cir) {
        if (((Enchantment)(Object)this) != Enchantments.SHARPNESS) return;

        float baseDamage = cir.getReturnValueF();
        if (baseDamage <= 0.0f) return;

        var cfg = Sharpness_enchantment_overhaulConfig.get();


        if (cfg.excludeSmiteAndBaneTargets) {
            if (group == EntityGroup.UNDEAD || group == EntityGroup.ARTHROPOD) {
                cir.setReturnValue(0.0F);
                return;
            }
        }


        LivingEntity target = AttackContext.getTarget();
        if (target != null && cfg.isSharpnessExcluded(target.getType())) {
            cir.setReturnValue(0.0F);
            return;
        }


        float mult = Math.max(0.0f, cfg.sharpnessMultiplier);
        cir.setReturnValue(baseDamage * mult);
    }
}