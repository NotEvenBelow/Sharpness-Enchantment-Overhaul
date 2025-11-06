package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.mixin;

import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.config.Sharpness_enchantment_overhaulConfig;
import Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.util.AttackContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class CaptureAttackTargetMixin {

    @Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    private void seo$captureAndApplyEffects(Entity entity, CallbackInfo ci) {
        // This is the fix: We must *explicitly* set null if the target isn't
        // a LivingEntity. Otherwise, the ThreadLocal might hold a STALE value.
        if (entity instanceof LivingEntity living) {
            AttackContext.setTarget(living);

            // --- NEW LOGIC FOR ON-HIT EFFECTS ---
            // We apply effects here, at the moment of the attack.

            PlayerEntity player = (PlayerEntity) (Object) this;
            World world = player.getWorld();
            if (world.isClient) return; // Only run on server

            ItemStack stack = player.getMainHandStack();
            if (stack.isEmpty()) return;

            var cfg = Sharpness_enchantment_overhaulConfig.get();

            int sharpnessLvl = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack);
            int smiteLvl = EnchantmentHelper.getLevel(Enchantments.SMITE, stack);
            int baneLvl = EnchantmentHelper.getLevel(Enchantments.BANE_OF_ARTHROPODS, stack);

            EntityGroup group = living.getGroup();

            // --- Sharpness Logic ---
            if (sharpnessLvl > 0 && cfg.sharpnessApplyOffTargetEffect) {
                if (group == EntityGroup.UNDEAD) {
                    if (world.random.nextFloat() < cfg.sharpnessUndeadChance) {
                        living.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.WEAKNESS,
                                cfg.sharpnessUndeadWeaknessDuration * 20,
                                cfg.sharpnessUndeadWeaknessLevel - 1)); // Potion levels are 0-indexed
                    }
                } else if (group == EntityGroup.ARTHROPOD) {
                    if (world.random.nextFloat() < cfg.sharpnessArthropodChance) {
                        living.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.SLOWNESS,
                                cfg.sharpnessArthropodSlownessDuration * 20,
                                cfg.sharpnessArthropodSlownessLevel - 1));
                    }
                } else if (cfg.isSharpnessExcluded(living.getType())) {
                    if (world.random.nextFloat() < cfg.sharpnessExcludedListChance) {
                        living.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.SLOWNESS,
                                cfg.sharpnessExcludedListSlownessDuration * 20,
                                cfg.sharpnessExcludedListSlownessLevel - 1));
                    }
                }
            }

            // --- Smite Logic ---
            if (smiteLvl > 0 && cfg.smiteApplyBuffOnNonUndead && group != EntityGroup.UNDEAD) {
                if (world.random.nextFloat() < cfg.smiteNonUndeadHitChance) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.REGENERATION,
                            cfg.smitePlayerRegenDuration * 20,
                            cfg.smitePlayerRegenLevel - 1));
                }
            }

            // --- Bane of Arthropods Logic (Updated) ---
            if (baneLvl > 0 && cfg.baneApplyDebuffOnNonArthropod && group != EntityGroup.ARTHROPOD) {
                if (world.random.nextFloat() < cfg.baneAllSlownessChance) {
                    living.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.SLOWNESS,
                            cfg.baneAllSlownessDuration * 20,
                            cfg.baneAllSlownessLevel - 1));
                }
                if (world.random.nextFloat() < cfg.baneAllWeaknessChance) {
                    living.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.WEAKNESS,
                            cfg.baneAllWeaknessDuration * 20,
                            cfg.baneAllWeaknessLevel - 1));
                }
            }

        } else {
            AttackContext.setTarget(null);
        }
    }

    @Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    private void seo$clear(Entity entity, CallbackInfo ci) {
        // Clear the context after the attack is fully processed.
        AttackContext.clear();
    }
}