package de.dafuqs.witherturnsskeletons.mixin;

import de.dafuqs.witherturnsskeletons.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.skeleton.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Skeleton.class)
public class SkeletonsTurnToWitherSkeletonsMixin {
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		Skeleton thisEntity = (Skeleton) (Object) this;
		
		if (!thisEntity.level().isClientSide() && thisEntity.isAlive() && !thisEntity.isNoAi()) {
			Optional<Integer> conversionTime = thisEntity.getExistingData(WitherTurnsSkeletons.WITHER_SKELETON_CONVERSION_TIME);
			if (conversionTime.isPresent()) {
				int newConversionTime = conversionTime.get() - 1;
				if (newConversionTime < 1) {
					witherturnsskeletons$convertToWitherSkeleton();
				} else {
					thisEntity.setData(WitherTurnsSkeletons.WITHER_SKELETON_CONVERSION_TIME, newConversionTime);
				}
			} else {
				MobEffectInstance statusEffectInstance = thisEntity.getEffect(MobEffects.WITHER);
				if (statusEffectInstance != null) {
					this.witherturnsskeletons$setWitherSkeletonConversionTime(100);
				}
			}
		}
	}
	
	@Inject(method = "isShaking()Z", at = @At("HEAD"), cancellable = true)
	public void isShaking(CallbackInfoReturnable<Boolean> cir) {
		if (witherturnsskeletons$isConvertingToWitherSkeleton()) {
			cir.setReturnValue(true);
		}
	}
	
	@Unique
	public boolean witherturnsskeletons$isConvertingToWitherSkeleton() {
		Skeleton thisEntity = (Skeleton) (Object) this;
		return thisEntity.hasData(WitherTurnsSkeletons.WITHER_SKELETON_CONVERSION_TIME);
	}
	
	@Unique
	public void witherturnsskeletons$setWitherSkeletonConversionTime(int time) {
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.setData(WitherTurnsSkeletons.WITHER_SKELETON_CONVERSION_TIME, time);
	}
	
	@Unique
	private void witherturnsskeletons$convertToWitherSkeleton() {
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.convertTo(EntityType.WITHER_SKELETON, ConversionParams.single(thisEntity, true, true), (witherSkeleton) -> {
			witherSkeleton.makeSound(WitherTurnsSkeletons.CONVERTING_SOUND);
		});
	}
	
}
