# Android-unused-resource-remover

![](https://i.imgur.com/GR3QWj2.png)

Remove your unused resources from a lint.xml file.

✅ res/layout\
✅ res/drawable\
✅ res/anim\
✅ res/color\
✅ res/font\
✅ res/menu\
✅ res/transition\
✅ res/sxml\
✅ res/values

Review should be taken when deciding to delete a resource identified as not being used by lint, since there are resources that lint finds unused, but are used internally by the application.

Clean and build recommended and check usages of method "resources.getIdentifier(name, type, packageName)"

### Known issues 🐛

When generating files in the res/values folder, usually in strigs.xml. Replaces special characters or ascii codes.
Some examples:

"& #160;" = "&nbsp"\
"& #8230;" = "..."

I hope to solve it soon.

### Reporting issues or improvements  🛠

Found a bug or a problem on a specific feature? Open an issue on  [Github issues](https://github.com/DavidBarbaran/Android-unused-resource-remover/issues)
