package de.dafuqs.witherturnsskeletons.mixin;

import de.dafuqs.witherturnsskeletons.*;
import net.minecraft.entity.*;
import net.minecraft.entity.conversion.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.network.syncher.*;
import net.minecraft.storage.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.skeleton.*;
import net.minecraft.world.level.storage.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Skeleton.class)
public class SkeletonsTurnToWitherSkeletonsMixin {
	
	@Unique
	private static final TrackedData<Boolean> CONVERTING_TO_WITHER_SKELETON = DataTracker.registerData(SkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	@Unique
	private int witherSkeletonConversionTime;
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		Skeleton thisEntity = (Skeleton) (Object) this;
		
		if (!thisEntity.level().isClientSide() && thisEntity.isAlive() && !thisEntity.isNoAi()) {
			if (this.witherturnsskeletons$isConvertingToWitherSkeleton()) {
				--witherSkeletonConversionTime;
				if (witherSkeletonConversionTime < 0) {
					witherturnsskeletons$convertToWitherSkeleton();
				}
			} else {
				MobEffectInstance statusEffectInstance = thisEntity.getEffect(MobEffects.WITHER);
				if (statusEffectInstance != null) {
					this.witherturnsskeletons$setWitherSkeletonConversionTime(100);
				}
			}
		}
	}
	
	@Inject(method = "defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V", at = @At("TAIL"))
	protected void initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
		builder.define(CONVERTING_TO_WITHER_SKELETON, false);
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
		return thisEntity.getEntityData().get(CONVERTING_TO_WITHER_SKELETON);
	}
	
	@Unique
	public void witherturnsskeletons$setWitherSkeletonConversionTime(int time) {
		this.witherSkeletonConversionTime = time;
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.getEntityData().set(CONVERTING_TO_WITHER_SKELETON, true);
	}
	
	@Unique
	private void witherturnsskeletons$convertToWitherSkeleton() {
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.convertTo(EntityType.WITHER_SKELETON, ConversionParams.single(thisEntity, true, true), (witherSkeleton) -> {
			witherSkeleton.makeSound(WitherTurnsSkeletons.CONVERTING_SOUND);
		});
	}
	
	@Inject(method = "addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V", at = @At("HEAD"))
	public void writeCustomDataToNbt(ValueOutput view, CallbackInfo ci) {
		view.putInt("WitherSkeletonConversionTime", this.witherturnsskeletons$isConvertingToWitherSkeleton() ? this.witherSkeletonConversionTime : -1);
	}
	
	@Inject(method = "readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V", at = @At("HEAD"))
	public void readCustomDataFromNbt(ValueInput view, CallbackInfo ci) {
		int witherConversionTime = view.getIntOr("WitherSkeletonConversionTime", -1);
		if (witherConversionTime > -1) {
			this.witherturnsskeletons$setWitherSkeletonConversionTime(witherConversionTime);
		}
	}
	
}
