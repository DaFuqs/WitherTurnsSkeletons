package de.dafuqs.witherturnsskeletons.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonEntity.class)
public class SkeletonsTurnToWitherMixin {

    private static final TrackedData<Boolean> CONVERTING_TO_WITHER_SKELETON = DataTracker.registerData(SkeletonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int witherSkeletonConversionTime;

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        SkeletonEntity thisEntity = (SkeletonEntity)(Object) this;

        if (!thisEntity.world.isClient && thisEntity.isAlive() && !thisEntity.isAiDisabled()) {
            if (this.isConvertingToWitherSkeleton()) {
                --witherSkeletonConversionTime;
                if (witherSkeletonConversionTime < 0) {
                    convertToWitherSkeleton();
                }
            } else {
                StatusEffectInstance statusEffectInstance = thisEntity.getStatusEffect(StatusEffects.WITHER);
                if(statusEffectInstance != null) {
                    this.setWitherSkeletonConversionTime(100);
                }
            }
        }
    }

    @Inject(method = "initDataTracker()V", at = @At("TAIL"))
    protected void initDataTracker(CallbackInfo ci) {
        ((EntityInvoker) this).getDataTracker().startTracking(CONVERTING_TO_WITHER_SKELETON, false);
    }

    @Inject(method = "isShaking()Z", at = @At("HEAD"), cancellable = true)
    public void isShaking(CallbackInfoReturnable<Boolean> cir) {
        if(isConvertingToWitherSkeleton()) {
            cir.setReturnValue(true);
        }
    }

    public boolean isConvertingToWitherSkeleton() {
        return ((EntityInvoker) this).getDataTracker().get(CONVERTING_TO_WITHER_SKELETON);
    }

    public void setWitherSkeletonConversionTime(int time) {
        this.witherSkeletonConversionTime = time;
        ((EntityInvoker) this).getDataTracker().set(CONVERTING_TO_WITHER_SKELETON, true);
    }

    private void convertToWitherSkeleton() {
        SkeletonEntity thisEntity = (SkeletonEntity)(Object) this;

        thisEntity.convertTo(EntityType.WITHER_SKELETON, true);
        if (!thisEntity.isSilent()) {
            thisEntity.world.syncWorldEvent(null, 1048, thisEntity.getBlockPos(), 0);
        }
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("WitherSkeletonConversionTime", this.isConvertingToWitherSkeleton() ? this.witherSkeletonConversionTime : -1);
    }

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("HEAD"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("WitherSkeletonConversionTime", 99) && nbt.getInt("WitherSkeletonConversionTime") > -1) {
            this.setWitherSkeletonConversionTime(nbt.getInt("WitherSkeletonConversionTime"));
        }
    }

}
