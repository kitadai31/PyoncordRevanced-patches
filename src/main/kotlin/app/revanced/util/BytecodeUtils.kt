package app.revanced.util

import app.revanced.patcher.fingerprint.MethodFingerprint
import app.revanced.patcher.patch.PatchException

/**
 * The [PatchException] of failing to resolve a [MethodFingerprint].
 *
 * @return The [PatchException].
 */
val MethodFingerprint.exception
    get() = PatchException("Failed to resolve ${this.javaClass.simpleName}")