package com.anzupop.wallpaperlistener;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class AndroidWallpaperWebServer extends NanoHTTPD {

    private AppCompatActivity mainActivity;

    public AndroidWallpaperWebServer(int port, AppCompatActivity activity) {
        super(port);
        this.mainActivity = activity;
    }

    public AndroidWallpaperWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        JSONObject response = new JSONObject();
        Map<String, String> parms = session.getParms();
        try {
            if (parms.get("file_path") == null) {
                response.put("msg", "file_path is needed as a query.");
            } else {
                Context context = mainActivity.getBaseContext();
                File f = new File(Objects.requireNonNull(parms.get("file_path")));
                String path = f.getAbsolutePath();
                File f1 = new File(path);

                if (f1.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    WallpaperManager wpm = WallpaperManager.getInstance(context);
                    try {
                        if ((parms.get("lock_screen") == null) || (Objects.equals(parms.get("lock_screen"), "0")))
                        {
                            wpm.setBitmap(bmp);
                            response.put("msg", "Home screen set.");
                        }
                        else
                        {
                            int result = wpm.setBitmap(bmp, null, true, WallpaperManager.FLAG_LOCK);
                            if (result == 0)
                            {
                                response.put("msg", "Lock screen set failed.");
                            }
                            else {
                                response.put("msg", "Lock screen set succeed.");
                            }
                        }
                    } catch (IOException e) {
                        return newFixedLengthResponse(Arrays.toString(e.getStackTrace()));
                    }
                } else {
                    response.put("msg", "no such file");
                }
            }
        } catch (JSONException e) {
            return newFixedLengthResponse(Arrays.toString(e.getStackTrace()));
        }

        return newFixedLengthResponse(response.toString());
    }
}