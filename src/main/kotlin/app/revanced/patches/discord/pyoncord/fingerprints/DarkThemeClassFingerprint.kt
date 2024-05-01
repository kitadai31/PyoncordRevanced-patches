package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint
import com.android.tools.smali.dexlib2.AccessFlags

object DarkThemeClassFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC.value,
    returnType = "I",
    customFingerprint = { _, classDef ->
        classDef.type == "Lcom/discord/theme/DarkTheme;" // matches any method of this class
    }
)