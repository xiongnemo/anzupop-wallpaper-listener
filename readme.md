# Anzupop Wallpaper Listener

## Usage

Press the button to start/stop.

InputView is for port input.

![screenshot](./doc/img/Screenshot_2021_04_25_14_02_13_976_com_anzupop_wallpaperlistener.jpg)

## API

### Endpoint

`GET` `/?file_path=<your file>`

Change your wallpaper to file specified.

### Return

```json
{"msg":"file_path is needed as a query."}
```

```json
{"msg":"no such file"}
```

```json
{"msg":"good"}
```

```json
<IOException stack>
```

## Example

Wallpaper filename is `206_spvfb204.jpg`

![IMG_20210425_034242](./doc/img/IMG_20210425_034242.jpg)

## Acknowledgements

[nanohttpd](https://github.com/NanoHttpd/nanohttpd) for minimal(?) functional HTTP server on Java platform

[Developer@Android](https://developer.android.com/)
