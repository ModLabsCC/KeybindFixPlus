package cc.modlabs.keybindfixplus

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import cc.modlabs.keybindfixplus.mixins.TimesPressedAccessor
import net.minecraft.client.options.KeyBinding
import net.minecraft.client.util.InputUtil

object KeybindFixer {

    private val keyFixMap: Multimap<InputUtil.KeyCode,KeyBinding> = ArrayListMultimap.create()

    fun putKey(key: InputUtil.KeyCode, keyBinding: KeyBinding)
    {
        keyFixMap.put(key, keyBinding)
    }

    fun clearMap()
    {
        keyFixMap.clear()
    }

    fun onKeyPressed(key: InputUtil.KeyCode, finalBinding: KeyBinding?, baseBinding: KeyBinding?)
    {
        if (finalBinding == null || baseBinding == null || finalBinding !== baseBinding) return
        for (theKey in keyFixMap[key])
        {
            if (theKey == null || theKey === baseBinding) continue
            (theKey as TimesPressedAccessor).timesPressed++
        }
    }

    fun setKeyPressed(key: InputUtil.KeyCode, pressed: Boolean, finalBinding: KeyBinding?, baseBinding: KeyBinding?)
    {
        if (finalBinding == null || baseBinding == null || finalBinding !== baseBinding) return
        for (theKey in keyFixMap[key])
        {
            if (theKey == null || theKey === baseBinding) continue
            theKey.pressed = pressed
        }
    }
}
