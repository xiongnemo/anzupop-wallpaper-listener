package com.anzupop.wallpaperlistener;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int DEFAULT_PORT = 63283;

    // INSTANCE OF ANDROID WEB SERVER
    private AndroidWallpaperWebServer androidWallpaperWebServer;
    private static boolean isStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStarted)
                {
                    Context context = getApplicationContext();
                    EditText portNumberInputBox = findViewById(R.id.editPortNumber);
                    int portToUse = DEFAULT_PORT;
                    try {
                        portToUse = Integer.parseInt(portNumberInputBox.getText().toString());
                        if (portToUse > 65535 || portToUse < 1024)
                        {
                            showToast("Port number not in range");
                            portToUse = DEFAULT_PORT;
                            portNumberInputBox.setText(String.valueOf(DEFAULT_PORT));
                        }
                    }
                    catch (NumberFormatException ignored)
                    {

                    }
                    startServer(portToUse);
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF08FF00")));
                    Snackbar.make(view, String.format("Wallpaper API listener: GET 127.0.0.1:%d/?file_path=<your file>", portToUse), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    stopServer();
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFF0048")));
                }
            }
        });
    }

    private void showToast(String message)
    {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public boolean startServer(int port) {
        if (!isStarted)
        {
            androidWallpaperWebServer = new AndroidWallpaperWebServer(port, this);
            try {
                androidWallpaperWebServer.start();
                isStarted = true;
                showToast(String.format("Started webserver at port %d.", port));
                return true;
            } catch (Exception e) {
                showToast(e.getMessage());
                return false;
            }
        }
        else
        {
            showToast("Server is already running.");
            return false;
        }
    }

    public boolean stopServer() {
        if (isStarted && androidWallpaperWebServer != null) {
            androidWallpaperWebServer.stop();
            isStarted = false;
            showToast("Server stopped.");
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}