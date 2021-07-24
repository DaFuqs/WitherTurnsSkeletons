package de.dafuqs.witherturnsskeletons.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SkeletonEntity.class)
public interface SkeletonEntityInvoker {

  @Accessor("CONVERTING")
  public static TrackedData<Boolean> getConverting() {
    throw new AssertionError();
  }


  @Invoker(value = "setConversionTime")
  void invokeSetConversionTime(int time);

}