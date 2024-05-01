package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object LightThemeClassFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC.value,
    returnType = "I",
    customFingerprint = { _, classDef ->
        classDef.type == "Lcom/discord/theme/LightTheme;" // matches with any method with this class
    }
)