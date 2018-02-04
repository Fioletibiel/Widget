package widget.miniprojekt5.lab6.pawelkaminski.pjwstk.edu.pl.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class AktywnoscWidgeta extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.aktywnosc_widgeta);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


public class MainActivity extends Activity {

    ImageView imageView;

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    SeekBar seekBar;

    final int[] playlist = {R.raw.anger, R.raw.frogs, R.raw.gong, R.raw.movie_projector, R.raw.radio_tuning};
    static int currentTrack;

    final int[] slides = {R.raw.lake, R.raw.mountains, R.raw.peacock};
    static int currentSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aktywnosc_widgeta);

        imageView = findViewById(R.id.imageView);
        currentSlide = 0;
        imageView.setImageResource(slides[currentSlide]);

        mediaPlayer = MediaPlayer.create(this, R.raw.anger);
        currentTrack = 0;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        seekBar = findViewById(R.id.seekBar);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1500);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void checkWeather(View view) {
        try {
            final String siteUrl = "http://if.pw.edu.pl/~meteo/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteUrl));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Brak zainstalowanej przeglądarki.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void nextImage(View view) {
        if (currentSlide < slides.length-1) {
            imageView.setImageResource(slides[++currentSlide]);
        }
        else {
            currentSlide = 0;
            imageView.setImageResource(slides[currentSlide]);
        }
    }

    public void start(View view) {
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
    }

    public void pause(View view) {
        mediaPlayer.pause();
    }

    public void stop(View view) {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer = MediaPlayer.create(this, R.raw.anger);
        seekBar.setProgress(0);
    }

    public void next(View view) {
        nextTrack();
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void nextTrack() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        if (currentTrack < playlist.length-1) {
            mediaPlayer = MediaPlayer.create(this, playlist[++currentTrack]);
        }
        else {
            currentTrack = 0;
            mediaPlayer = MediaPlayer.create(this, playlist[0]);
        }
        seekBar.setProgress(0);
        mediaPlayer.start();
    }
}
