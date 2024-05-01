package app.revanced.patches.discord.packagename

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchException

object ChangePackageNameBytecodePatch : BytecodePatch(emptySet()) {
    override fun execute(context: BytecodeContext) {
        // Fighting the side effects of changing the package name

//        if (packageName != "com.discord") {
//            val getIdentifier = Resources::class.java.getDeclaredMethod(
//                "getIdentifier",
//                String::class.java,
//                String::class.java,
//                String::class.java
//            )
//
//            XposedBridge.hookMethod(getIdentifier, object: XC_MethodHook() {
//                override fun beforeHookedMethod(mhparam: MethodHookParam) = with(mhparam) {
//                    if (args[2] == param.packageName) args[2] = "com.discord"
//                }
//            })
//        }
        throw PatchException("Not yet implemented")
    }
}