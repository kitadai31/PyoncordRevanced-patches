package app.revanced.patches.discord.pyoncord

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.or
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.revanced.patches.discord.pyoncord.fingerprints.ApplicationFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.DarkThemeClassFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.GetColorCompatFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.GetColorCompatLegacyFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.LightThemeClassFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.LoadScriptFromAssetsFingerprint
import app.revanced.patches.discord.pyoncord.fingerprints.LoadScriptFromFileFingerprint
import app.revanced.util.exception
import com.android.tools.smali.dexlib2.AccessFlags

@Patch(
    name = "Pyoncord",
    description = "Inject Pyoncord to the Discord app",
    compatiblePackages = [
        CompatiblePackage("com.discord"),
    ],
    requiresIntegrations = true,
)
@Suppress("unused")
object PyoncordPatch : BytecodePatch(
    setOf(
        ApplicationFingerprint,
        LoadScriptFromAssetsFingerprint,
        LoadScriptFromFileFingerprint,
        GetColorCompatFingerprint,
        GetColorCompatLegacyFingerprint,
        DarkThemeClassFingerprint,
        LightThemeClassFingerprint,
    )
) {

    private const val INTEGRATIONS_PACKAGE = "Lapp/revanced/integrations/discord/pyoncord"
    private const val PYONCORD_CLASS_DESCRIPTOR = "$INTEGRATIONS_PACKAGE/PyoncordPatch;"
    private const val THEME_CLASS_DESCRIPTOR = "$INTEGRATIONS_PACKAGE/ThemeModule;"

    override fun execute(context: BytecodeContext) {

        // Hook Application onCreate
        ApplicationFingerprint.result?.mutableMethod?.addInstruction(
            1,
            "invoke-static {p0}, $PYONCORD_CLASS_DESCRIPTOR->onCreateApplication(Landroid/app/Application;)V"
        ) ?: throw ApplicationFingerprint.exception

        // Copy the original loadScriptFromFile method in order to replicate XposedBridge.invokeOriginalMethod
        LoadScriptFromFileFingerprint.result?.let {
            it.mutableClass.methods.add(
                it.method.toMutable().apply { name = "loadScriptFromFileOriginal" }
            )
        } ?: throw LoadScriptFromFileFingerprint.exception

        // Inject vendetta.js
        arrayOf(LoadScriptFromAssetsFingerprint, LoadScriptFromFileFingerprint).forEach {
            it.result?.mutableMethod?.addInstruction(
                0,
                "invoke-static {p0}, $PYONCORD_CLASS_DESCRIPTOR->beforeLoadScript(Lcom/facebook/react/bridge/CatalystInstanceImpl;)V"
            ) ?: throw it.exception
        }


        // region ThemeModule

        arrayOf(GetColorCompatFingerprint, GetColorCompatLegacyFingerprint).forEach {
            it.result?.apply {
                mutableMethod.addInstructions(
                    scanResult.patternScanResult!!.startIndex + 1,
                    """
                        move-result v0
                        invoke-static {p0, p1, v0}, $THEME_CLASS_DESCRIPTOR->getRawColor(Ljava/lang/Object;II)I
                    """
                )
            } ?: throw it.exception
        }

        arrayOf(DarkThemeClassFingerprint, LightThemeClassFingerprint).forEachIndexed { index, it ->
            val theme = if (index == 0) "Dark" else "Light"
            it.result?.mutableClass?.methods?.forEach {
                if (it.accessFlags == AccessFlags.PUBLIC or AccessFlags.CONSTRUCTOR) return@forEach
                if (it.name.startsWith("getColor")) return@forEach

                // getTextNormal -> TEXT_NORMAL
                val colorName =
                    it.name.replace(Regex("([A-Z])"), "_$1").replace("get_", "").uppercase()

                // hack: Generate the semantic color name at patching time and embed to code
                // then pass the name to the integrations method
                // but it may be better to get the caller method name in integrations from stack trace
                it.addInstructions(
                    it.implementation!!.instructions.size - 1,
                    """
                        const-string p0, "$colorName"
                        invoke-static {v0, p0}, $THEME_CLASS_DESCRIPTOR->getSemanticColor$theme(ILjava/lang/String;)I
                        move-result v0
                    """
                )
            }
        }

        // endregion

    }

}