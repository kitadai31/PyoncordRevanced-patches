package app.revanced.patches.discord.pyoncord.fingerprints

import app.revanced.patcher.fingerprint.MethodFingerprint

object LoadScriptFromFileFingerprint : MethodFingerprint(
    customFingerprint = { methodDef, classDef ->
        classDef.type == "Lcom/facebook/react/bridge/CatalystInstanceImpl;"
                && methodDef.name == "loadScriptFromFile"
    }
)