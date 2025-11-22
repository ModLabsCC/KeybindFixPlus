package cc.modlabs.keybindfixplus.mixins;

import cc.modlabs.keybindfixplus.KeybindFixer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = KeyBinding.class, priority = 10000)
public abstract class KeybindingMixin {

    @Final
    @Shadow private static Map<String, KeyBinding> keysById;

    @Final
    @Shadow private static Map<InputUtil.KeyCode, KeyBinding> keysByCode;

    @Shadow private InputUtil.KeyCode keyCode;

    @Inject(method = "onKeyPressed", at = @At(value = "TAIL"))
    private static void onKeyPressedFixed(InputUtil.KeyCode key, CallbackInfo ci, @Local KeyBinding original){
        KeybindFixer.INSTANCE.onKeyPressed(key, original, keysByCode.get(key));
    }

    @Inject(method = "setKeyPressed", at = @At(value = "TAIL"))
    private static void setKeyPressedFixed(InputUtil.KeyCode key, boolean pressed, CallbackInfo ci, @Local KeyBinding original){
        KeybindFixer.INSTANCE.setKeyPressed(key, pressed, original, keysByCode.get(key));
    }

    @Inject(method = "updateKeysByCode", at = @At(value = "INVOKE",target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static void updateByCodeToMultiMap(CallbackInfo ci) {
        KeybindFixer.INSTANCE.clearMap();
        for (KeyBinding keyBinding : keysById.values()) {
            KeybindFixer.INSTANCE.putKey(((BoundKeyAccessor) keyBinding).getBoundKey(),keyBinding);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "TAIL"))
    private void putToMultiMap(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci){
        KeybindFixer.INSTANCE.putKey(keyCode, (KeyBinding) (Object) this);
    }

}
