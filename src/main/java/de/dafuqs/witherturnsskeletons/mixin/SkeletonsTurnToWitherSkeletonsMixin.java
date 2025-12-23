package de.dafuqs.witherturnsskeletons.mixin;

import de.dafuqs.witherturnsskeletons.*;
import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.skeleton.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Skeleton.class)
public class SkeletonsTurnToWitherSkeletonsMixin {
	
	@Unique
	private static final EntityDataAccessor<Boolean> CONVERTING_TO_WITHER_SKELETON = EntityDataAccessor.defineId(Skeleton.class, TrackedDataHandlerRegistry.BOOLEAN);
	@Unique
	private int witherSkeletonConversionTime;
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		Skeleton thisEntity = (Skeleton) (Object) this;
		
		if (!thisEntity.getEntityWorld().isClient() && thisEntity.isAlive() && !thisEntity.isAiDisabled()) {
			if (this.witherturnsskeletons$isConvertingToWitherSkeleton()) {
				--witherSkeletonConversionTime;
				if (witherSkeletonConversionTime < 0) {
					witherturnsskeletons$convertToWitherSkeleton();
				}
			} else {
				StatusEffectInstance statusEffectInstance = thisEntity.getStatusEffect(StatusEffects.WITHER);
				if (statusEffectInstance != null) {
					this.witherturnsskeletons$setWitherSkeletonConversionTime(100);
				}
			}
		}
	}
	
	@Inject(method = "initDataTracker(Lnet/minecraft/entity/data/DataTracker$Builder;)V", at = @At("TAIL"))
	protected void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(CONVERTING_TO_WITHER_SKELETON, false);
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
		return thisEntity.getDataTracker().get(CONVERTING_TO_WITHER_SKELETON);
	}
	
	@Unique
	public void witherturnsskeletons$setWitherSkeletonConversionTime(int time) {
		this.witherSkeletonConversionTime = time;
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.getDataTracker().set(CONVERTING_TO_WITHER_SKELETON, true);
	}
	
	@Unique
	private void witherturnsskeletons$convertToWitherSkeleton() {
		Skeleton thisEntity = (Skeleton) (Object) this;
		thisEntity.convertTo(EntityType.WITHER_SKELETON, EntityConversionContext.create(thisEntity, true, true), (witherSkeleton) -> {
			witherSkeleton.playSound(WitherTurnsSkeletons.CONVERTING_SOUND);
		});
	}
	
	@Inject(method = "writeCustomData(Lnet/minecraft/storage/WriteView;)V", at = @At("HEAD"))
	public void writeCustomDataToNbt(WriteView view, CallbackInfo ci) {
		view.putInt("WitherSkeletonConversionTime", this.witherturnsskeletons$isConvertingToWitherSkeleton() ? this.witherSkeletonConversionTime : -1);
	}
	
	@Inject(method = "readCustomData(Lnet/minecraft/storage/ReadView;)V", at = @At("HEAD"))
	public void readCustomDataFromNbt(ReadView view, CallbackInfo ci) {
		int witherConversionTime = view.getInt("WitherSkeletonConversionTime", -1);
		if (witherConversionTime > -1) {
			this.witherturnsskeletons$setWitherSkeletonConversionTime(witherConversionTime);
		}
	}
	
}
