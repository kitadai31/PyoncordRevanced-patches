package app.revanced.patches.discord.branding

import app.revanced.patcher.data.ResourceContext
import app.revanced.patcher.patch.ResourcePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patcher.patch.options.PatchOption.PatchExtensions.stringPatchOption
import org.w3c.dom.Element

@Patch(
    name = "Custom branding",
    description = "Applies a custom app name and custom background color of icon. Defaults to \"Pyoncord\" and the Pyoncord blue.\n" +
            "Changing the background of icon supports Android 8.0+",
    compatiblePackages = [
        CompatiblePackage("com.discord"),
    ],
)
@Suppress("unused")
object CustomBrandingPatch : ResourcePatch() {

    private var appName by stringPatchOption(
        key = "appName",
        default = "Pyoncord",
        title = "App name",
        description = "The name of the app.",
    )

    private var iconColor by stringPatchOption(
        key = "iconColor",
        default = "#FF3AB8BA",
        title = "Icon background color",
        description = "The background color of the app icon."
    )
    override fun execute(context: ResourceContext) {
        // App name
        val manifest = context["AndroidManifest.xml", false]
        manifest.writeText(
            manifest.readText()
                .replace(
                    "android:label=\"@string/app_name",
                    "android:label=\"$appName",
                ),
        )

        // Icon color
        context.document["res/values/colors.xml"].use {
            val resourcesNode = it.getElementsByTagName("resources").item(0) as Element
            for (i in 0 until resourcesNode.childNodes.length) {
                val node = resourcesNode.childNodes.item(i) as? Element ?: continue
                if (node.getAttribute("name") == "brand") {
                    node.textContent = iconColor
                }
            }
        }
    }
}