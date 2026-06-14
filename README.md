# SognoDiGhast - by Kaeternn
A simple yet configurable plugin to boost happy ghast speed.
## Installation
This plugin requires [Paper](https://papermc.io/downloads/paper).

To install, place this plugin JAR in your server's ``/plugins`` folder.
## Configuration
### General settings
- ``debug``, add debug messages in console, I don't recommend enabling it if not necessary.
- ``only_when_ridden``, limit the faster flying speed to ridden ghasts or happy ghasts (if somehow your server allows to ride regular ghast this setting apply to them too).
- ``only_happy_ghast``, limit the faster flying speed to happy ghasts.
- ``speed_multiplier``, ghast's flying speed will be multiplied by this value.
### Dimensions settings
Please note that you should not add or remove dimension entries here, if you use custom worlds or custom dimensions please use the worlds settings below. Also note that a ghast is ~3 blocks higher than the player who rides it, so if you set 150, the ghast will likely become faster at 153.

If you want to disable one of these limits, set it to infinity.
- ``min``, minimum height at which the multiplier takes effect.
- ``max``, maximum height at which the multiplier takes effect.
### Worlds settings
These settings use world names instead of dimensions; they also override the dimension settings for worlds listed here.

If you want to disable one of these limit, set it to infinity.
- ``min``, minimum height at which the multiplier takes effect.
- ``max``, maximum height at which the multiplier takes effect.
## Usage
Just let your players fly away with their faster ghasts.

There are two admin commands too :
- ``/sdg version`` tells you which version is installed on your server.
- ``/sdg reload`` reload the configuration if you want to update values without restarting the server.
## Getting help
Need help or want to make a suggestion ? Join my [Discord](https://discord.com/invite/sPX8AMxbBG) !
## Information
### Code efficiency
I'm still learning JAVA and Minecraft plugin development, so my code can be inefficient sometimes, if you spot a problem in my code please feel free to explain it to me.
### Generative AI
I used it only to get information about things I didn't know how to implement or for advice on resolving problems I couldn't solve.

I didn't directly use AI‑generated code, documentation, or text.