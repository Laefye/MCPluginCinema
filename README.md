# MCPluginCinema
## [Cinema](https://github.com/Laefye/MCCinemaMod) plugin for Minecraft 1.19.2
> This edition is critically cut!!!
## Installation
### Preparation.
To work the plugin need to have the `ffprobe` program installed, and install the plugin "WorldEdit" (compatible)
### Configurations
#### cinema/config.yml
```yaml
api:
  # path to ffprobe
  ffprobe: plugins/cinema/ffprobe
cinema:
  # warning | kick
  # What to do if the protocol of client cinema mod is older
  on_old_version: warning
```
#### cinema/lobbies.yml
```yaml
lobbies:
  # lobby name
  default:
    # screen parameters
    screen:
      width: 4.0
      height: 3.0
      position:
        x: 0.0
        y: 0.0
        z: 0.0
        rotation: 0.0
      # distance is volume
      distance: 50.0
    # Don't use it, it's leftovers from the original plugin
    permmanager:
      id: cinema:default
      params:
        custom: true
```
### How to use (commands)
#### /join (lobby name)
Join to lobby
#### /play (direct url to media, https://.../foo.mp4) (name & title or media)
Add to order media
#### /exit
Exit from lobby
#### /rewind (seconds)
Rewind a video
#### /cinema cinema:getConfig
This command uses WorldEdit to simplify lobby making

Select an area with the axe, write a command and the chat will reply with the parameters for the lobby
#### /cinema cinema:setPerm player (player) false (true|false|reset)
Prohibits, allows to add videos to the queue
### Permissions
- `cinema.(lobby-name).join`
- `cinema.(lobby-name).skip`
- `cinema.(lobby-name).vote.skip`
- `cinema.(lobby-name).rewind`
