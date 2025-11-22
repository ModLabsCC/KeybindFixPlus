package cc.modlabs.keybindfixplus.mixins;

import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface TimesPressedAccessor {

    @Accessor(value = "timesPressed")
    int getTimesPressed();
    @Accessor(value = "timesPressed")
    void setTimesPressed(int value);
}
