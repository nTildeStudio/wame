package com.ntilde.wame.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.ntilde.wame.R;
import com.ntilde.wame.helpers.FontHelper;

public class PTextView extends TextView{

    public void setPHeight(int resDimen){
        TypedValue tv=new TypedValue();
        getResources().getValue(resDimen, tv, true);
        setPHeight(tv.getFloat());
    }

    public void setPHeight(float pHeight){
        if(pHeight<0){
            return;
        }
        calcHeight(pHeight);
    }

    public void setAssetFont(String assetFont){
        try{
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), assetFont);
            setTypeface(font);
        }
        catch(Exception ex){
            Log.v(getClass().getName(),"No existe la fuente asignada", ex);
            try{
                setTypeface(FontHelper.getDefaultTypeface(getContext()));
            }catch(Exception e){
                Log.v(getClass().getName(),"No existe la fuente por defecto", e);
            }
        }
    }

    public PTextView(Context context){
        super(context);
        init(null);
    }

    public PTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public PTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs==null){
            return;
        }
        TypedArray attrsArray=getContext().obtainStyledAttributes(attrs,R.styleable.PTextView);
        float pHeight=attrsArray.getFloat(R.styleable.PTextView_ptv_height, 0);
        String assetFont = attrsArray.getString(R.styleable.PTextView_ptv_font_asset);

        setAssetFont(assetFont);
        calcHeight(pHeight);
        attrsArray.recycle();
    }

    private void calcHeight(float pHeight){
        if(pHeight!=0){
            setIncludeFontPadding(false);
            WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display=wm.getDefaultDisplay();
            Point screenSize=new Point(0,0);
            display.getSize(screenSize);
            float newPixlesHeight=pHeight*screenSize.y/100;
            float scaledDensity=getContext().getResources().getDisplayMetrics().scaledDensity;
            setTextSize(newPixlesHeight/scaledDensity);
        }

    }
}