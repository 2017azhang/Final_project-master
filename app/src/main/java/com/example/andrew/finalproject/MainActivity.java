package com.example.andrew.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;


public class MainActivity extends AppCompatActivity {
    //public static Bitmap blurred;
    private SeekBar seekBar;
    private ImageView harper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bitmap = BitmapFactory.decodeFile("C:\\Users\\2017azhang\\AndroidStudioProjects\\FinalProject\\app\\src\\main\\res\\drawable\\harper.jpg");
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        harper = (ImageView) findViewById(R.id.imageView);
        //harper.setImageResource(R.drawable.harper);
        //harper.setImageBitmap(bitmap);
        //
        new CountDownTimer(1000, 1000) {
            public void onTick(long duration){

            }
            public void onFinish(){
                harper.setImageResource(R.drawable.harper);
                harper.setDrawingCacheEnabled(true);
                Bitmap bitmap = harper.getDrawingCache();
                Bitmap blurred = blurRenderScript(bitmap, 5);
                harper.setImageBitmap(blurred);
            }
        }.start();
        initBlurEffect();
        /*harper.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view){
               Toast.makeText(getApplicationContext(), "Clicked Image", Toast.LENGTH_SHORT).show();
               blurred = createBlurredImage(bitmap);
               harper.setImageBitmap(blurred);

           }
        });*/



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private void initBlurEffect(){
        seekBar.setMax(25);
        seekBar.setKeyProgressIncrement(1);
        seekBar.setProgress(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
           @Override
           public void onProgressChanged(SeekBar bar, int i, boolean b){
                harper.setImageResource(R.drawable.harper);
                harper.setDrawingCacheEnabled(true);
                Bitmap bitmap = harper.getDrawingCache();
                if(i==0){
                    harper.setImageResource(R.drawable.harper);
                }
                else{
                    Log.e("seek......", i + "");
                    Bitmap blurred = blurRenderScript(bitmap, i);
                    harper.setImageBitmap(blurred);
                    harper.invalidate();
                }
           }
            @Override
            public void onStartTrackingTouch(SeekBar bar){

            }
            @Override
            public void onStopTrackingTouch(SeekBar bar){

            }
        });
    }
    private Bitmap blurRenderScript(Bitmap smallbit, int radius){
        try{
            smallbit = RGB565toARGB888(smallbit);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(smallbit.getWidth(), smallbit.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript renderScript = RenderScript.create(MainActivity.this);
        Allocation input = Allocation.createFromBitmap(renderScript, smallbit);
        Allocation output = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        blur.setInput(input);
        blur.setRadius(radius);
        blur.forEach(output);

        output.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;
    }
    private Bitmap RGB565toARGB888(Bitmap img) throws Exception{
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        Bitmap finalImg = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        finalImg.setPixels(pixels, 0, finalImg.getWidth(), 0, 0, finalImg.getWidth(), finalImg.getHeight());
        return finalImg;
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



    /*private void BlurImageHandler(Object sender, SeekBar.stopTrackingTouchEvent e)
    private void displayBlurredImage(int radius){

    }*/


}
