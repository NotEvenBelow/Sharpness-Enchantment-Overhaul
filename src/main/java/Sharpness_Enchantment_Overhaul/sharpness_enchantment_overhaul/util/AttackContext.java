package Sharpness_Enchantment_Overhaul.sharpness_enchantment_overhaul.util;

import net.minecraft.entity.LivingEntity;

public final class AttackContext {
    private static final ThreadLocal<LivingEntity> TL_TARGET = new ThreadLocal<>();

    public static void setTarget(LivingEntity target) { TL_TARGET.set(target); }
    public static LivingEntity getTarget() { return TL_TARGET.get(); }
    public static void clear() { TL_TARGET.remove(); }

    private AttackContext() {}
}