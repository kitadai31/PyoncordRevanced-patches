package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object ApplicationFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC.value,
    returnType = "V",
    customFingerprint = { methodDef, classDef ->
        classDef.type == "Lcom/discord/tti_manager/TTILoggingApplication;" && methodDef.name == "onCreate"
    }
)