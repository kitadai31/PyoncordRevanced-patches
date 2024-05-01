package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

object GetColorCompatFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC or AccessFlags.STATIC or AccessFlags.FINAL,
    parameters = listOf("L", "I", "L"),
    returnType = "I",
    opcodes = listOf(Opcode.INVOKE_VIRTUAL),
    customFingerprint = { methodDef, classDef ->
        classDef.type == "Lcom/discord/theme/utils/ColorUtilsKt;" && methodDef.name == "getColorCompat"
    }
)