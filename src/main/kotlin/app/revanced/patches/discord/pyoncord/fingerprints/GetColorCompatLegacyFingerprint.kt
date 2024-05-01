package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object GetColorCompatLegacyFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.STATIC or AccessFlags.FINAL,
    parameters = listOf("Landroid/content/Context;", "I"),
    returnType = "I",
    opcodes = listOf(Opcode.INVOKE_VIRTUAL),
    customFingerprint = { methodDef, classDef ->
        classDef.type == "Lcom/discord/theme/utils/ColorUtilsKt;" && methodDef.name == "getColorCompat"
    }
)