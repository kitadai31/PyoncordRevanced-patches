package app.revanced.patches.discord.packagename

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.PatchException
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch

@Patch(
    name = "Change package name",
    description = "Change the package name. Default is \"com.pyoncord.revanced\".",
    compatiblePackages = [
        CompatiblePackage("com.discord"),
    ],
    dependencies = [ChangePackageNameBytecodePatch::class],
)
@Suppress("unused")
object ChangePackageNamePatch : ResourcePatch() {
    override fun execute(context: ResourceContext) {
        throw PatchException("Not yet implemented")
    }
}