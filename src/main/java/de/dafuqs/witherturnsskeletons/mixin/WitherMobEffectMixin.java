package de.dafuqs.witherturnsskeletons.mixin;

import net.minecraft.server.level.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.skeleton.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WitherMobEffect.class)
public abstract class WitherMobEffectMixin {
	
	@Inject(method = "applyEffectTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;I)Z",
			at = @At("HEAD"),
			cancellable = true)
	public void isInvulnerableTo(ServerLevel world, LivingEntity entity, int amplifier, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof Skeleton) {
			cir.setReturnValue(true);
		}
	}
	
}
