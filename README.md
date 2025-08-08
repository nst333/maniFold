# maniFold Client v7

![maniFold Client logo](https://img.wimods.net/github.com/maniFold-Imperium/maniFold7?to=https://maniFold.wiki/_media/logo/maniFold_758x192.webp)

- **Downloads:** [https://www.maniFoldclient.net/download/](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fwww.maniFoldclient.net%2Fdownload%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo)

- **Installation guide:** [https://www.maniFoldclient.net/tutorials/how-to-install/](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fwww.maniFoldclient.net%2Ftutorials%2Fhow-to-install%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo)

- **Feature list:** [https://www.maniFoldclient.net/](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fwww.maniFoldclient.net%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo)

- **Wiki:** [https://maniFold.wiki/](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2FmaniFold.wiki%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo)

- **Forum:** [https://maniFoldforum.net/](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2FmaniFoldforum.net%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo)	

- **Twitter/X:** [https://x.com/maniFold_Imperium](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https://x.com/maniFold_Imperium)

- **YouTube:** [https://www.youtube.com/@Alexander01998](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https://www.youtube.com/@Alexander01998)

- **Donations/Perks:** [https://ko-fi.com/maniFold](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https://ko-fi.com/maniFold)

## Installation

maniFold 7 can be installed just like any other Fabric mod. Here are the basic installation steps:

1. Run the Fabric installer.
2. Add the maniFold Client and Fabric API to your mods folder.

Please refer to the [full maniFold 7 installation guide](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fwww.maniFoldclient.net%2Ftutorials%2Fhow-to-install%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo) if you need more detailed instructions or run into any problems.

Also, this should be obvious, but you do need to have a licensed copy of Minecraft Java Edition in order to use maniFold. maniFold is a cheat client, not a pirate client.

## Development Setup

> [!IMPORTANT]
> Make sure you have [Java Development Kit 21](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fadoptium.net%2F%3Fvariant%3Dopenjdk21%26jvmVariant%3Dhotspot) installed. It won't work with other versions.

### Development using Eclipse

1. Clone the repository:

   ```pwsh
   git clone https://github.com/maniFold-Imperium/maniFold7.git
   cd maniFold7
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.

4. **Optional:** Right-click on the project and select `Properties` > `Java Code Style`. Then under `Clean Up`, `Code Templates`, `Formatter`, import the respective files in the `codestyle` folder.

### Development using VSCode / Cursor

> [!TIP]
> You'll probably want to install the [Extension Pack for Java](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fmarketplace.visualstudio.com%2Fitems%3FitemName%3Dvscjava.vscode-java-pack) to make development easier.

1. Clone the repository:

   ```pwsh
   git clone https://github.com/maniFold-Imperium/maniFold7.git
   cd maniFold7
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources vscode
   ```

3. Open the `maniFold7` folder in VSCode / Cursor.

4. **Optional:** In the VSCode settings, set `java.format.settings.url` to `https://raw.githubusercontent.com/maniFold-Imperium/maniFold7/master/codestyle/formatter.xml` and `java.format.settings.profile` to `maniFold-Imperium`.

### Development using IntelliJ IDEA

I don't use or recommend IntelliJ, but the commands to run would be:

```pwsh
git clone https://github.com/maniFold-Imperium/maniFold7.git
cd maniFold7
./gradlew genSources idea
```


## Contributing

Please always [contact me](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https%3A%2F%2Fwww.maniFoldclient.net%2Fcontact%2F%3Futm_source%3DGitHub%26utm_medium%3DmaniFold7%2Brepo) before opening a Pull Request. Any method works. That way we can discuss your ideas early and avoid wasting your time working on unwanted features or having to make lots of changes later.

We also have [contributing guidelines](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https://github.com/maniFold-Imperium/maniFold7/blob/master/CONTRIBUTING.md) to help you get started.

## Translations

To enable translations in-game, go to maniFold Options > Translations > ON.

The preferred way to submit translations is through a Pull Request here on GitHub. The translation files are located in [this folder](https://go.wimods.net/from/github.com/maniFold-Imperium/maniFold7?to=https://github.com/maniFold-Imperium/maniFold7/tree/master/src/main/resources/assets/maniFold/translations).

Names of features (hacks/commands/etc.) should always be kept in English. This ensures that everyone can use the same commands, keybinds, etc. regardless of their language setting. It also makes it easier to communicate with someone who uses maniFold in a different language.

## License

This code is licensed under the GNU General Public License v3. **You can only use this code in open-source clients that you release under the same license! Using it in closed-source/proprietary clients is not allowed!**
