package corp.wmsoft.android.lib.wmavatarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


/**
 *
 */
public class WMAvatarView extends ImageView {

    /**/
    @SuppressWarnings("unused")
    private static final String TAG = "WMAvatarView";

    /**/
    private static final ScaleType SCALE_TYPE           = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG    = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMENSION   = 2;
    private static final int STATUS_ICON_ANGLE          = 45;
    private static final int STATUS_ICON_MAX_RADIUS     = 25;
    private static final int STATUS_ICON_MIN_MARGIN     = 3;
    private static final int STATUS_ICON_STROKE_WIDTH   = 2;

    /**
     * переменные круглой картинки
     */
    private Bitmap       mBitmap;
    private BitmapShader mBitmapShader;
    private final Paint  mBitmapPaint = new Paint();
    private int          mBitmapWidth;
    private int          mBitmapHeight;
    private final Matrix mShaderMatrix = new Matrix();
    private final RectF  mDrawableRect = new RectF();
    private float        mDrawableRadius;

    /**
     * ПЕременные кружка статуса
     */
    private int          mStatusCircleRadius    = STATUS_ICON_MAX_RADIUS; // радиус внешнего круга
    private int          mStatusIconRadius      = STATUS_ICON_MAX_RADIUS; // радиус кружка статуса
    private final Paint  mStatusCirclePaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean      isStatusVisible        = true;
    private boolean      useColorsAsStatusIcon  = true;
    private int          statusIconX;
    private int          statusIconY;
    private final Paint  mStatusIconPaint       = new Paint(Paint.ANTI_ALIAS_FLAG);
    @ColorInt
    private int          mStatusColorOffline    = Color.GREEN;
    @ColorInt
    private int          mStatusColorOnline     = Color.GREEN;
    @ColorInt
    private int          mStatusColorAway       = Color.YELLOW;
    @ColorInt
    private int          mStatusColorBusy       = Color.RED;
    // картинки для статуса
    private final Paint  mStatusBitmapPaint     = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap       mStatusBitmapOffline;
    private Bitmap       mStatusBitmapOnline;
    private Bitmap       mStatusBitmapAway;
    private Bitmap       mStatusBitmapBusy;

    /**
     *
     */
    private boolean isTextAvatar = true;
    private final Paint textBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * состояния вью: офлайн, оналайн, away, busy
     */
    @IWMAvatarStatus
    private int mStatus;


    public WMAvatarView(Context context) {
        super(context);
        init();
    }

    public WMAvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WMAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WMAvatarView, defStyleAttr, 0);

        isStatusVisible = a.getBoolean(R.styleable.WMAvatarView_wm_av_status_visible, true);
        useColorsAsStatusIcon = a.getBoolean(R.styleable.WMAvatarView_wm_av_status_as_color, true);
        mStatusColorOffline = a.getColor(R.styleable.WMAvatarView_wm_av_status_offline_color, Color.GREEN);
        mStatusColorOnline = a.getColor(R.styleable.WMAvatarView_wm_av_status_online_color, Color.GREEN);
        mStatusColorAway = a.getColor(R.styleable.WMAvatarView_wm_av_status_away_color, Color.YELLOW);
        mStatusColorBusy = a.getColor(R.styleable.WMAvatarView_wm_av_status_busy_color, Color.RED);

        a.recycle();

        init();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.status = mStatus;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setStatus(ss.status);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setup();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        setup();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("AdjustViewBounds not supported.");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            return;
        }

        if (isTextAvatar) {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mStatusBitmapPaint);
            canvas.drawText("WW", mDrawableRect.centerX(), mDrawableRect.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        } else {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint);
        }

        if (isStatusVisible) {
            // hole
            canvas.drawCircle(statusIconX, statusIconY, mStatusCircleRadius, mStatusCirclePaint);
            if (useColorsAsStatusIcon) {
                canvas.drawCircle(statusIconX, statusIconY, mStatusIconRadius, mStatusIconPaint);
            } else {

                switch (mStatus) {

                    case IWMAvatarStatus.AWAY:
                        canvas.drawBitmap(mStatusBitmapAway, statusIconX - mStatusBitmapAway.getWidth()/2, statusIconY - mStatusBitmapAway.getHeight()/2, mStatusBitmapPaint);
                        break;
                    case IWMAvatarStatus.BUSY:
                        canvas.drawBitmap(mStatusBitmapBusy, statusIconX - mStatusBitmapBusy.getWidth()/2, statusIconY - mStatusBitmapBusy.getHeight()/2, mStatusBitmapPaint);
                        break;
                    case IWMAvatarStatus.OFFLINE:
                        canvas.drawBitmap(mStatusBitmapOffline, statusIconX - mStatusBitmapOffline.getWidth()/2, statusIconY - mStatusBitmapOffline.getHeight()/2, mStatusBitmapPaint);
                        break;
                    case IWMAvatarStatus.ONLINE:
                        canvas.drawBitmap(mStatusBitmapOnline, statusIconX - mStatusBitmapOnline.getWidth()/2, statusIconY - mStatusBitmapOnline.getHeight()/2, mStatusBitmapPaint);
                        break;
                }


            }
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        initializeBitmap();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        initializeBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        initializeBitmap();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        initializeBitmap();
    }

    public void setStatus(@IWMAvatarStatus int status) {
        this.mStatus = status;

        @ColorInt
        int color = Color.GREEN;
        Paint.Style style = Paint.Style.STROKE;

        switch (status) {

            case IWMAvatarStatus.AWAY:
                color = mStatusColorAway;
                style = Paint.Style.FILL_AND_STROKE;
                break;
            case IWMAvatarStatus.BUSY:
                color = mStatusColorBusy;
                style = Paint.Style.FILL_AND_STROKE;
                break;
            case IWMAvatarStatus.ONLINE:
                color = mStatusColorOnline;
                style = Paint.Style.FILL_AND_STROKE;
                break;
            case IWMAvatarStatus.OFFLINE:
                color = mStatusColorOffline;
                style = Paint.Style.STROKE;
                break;
        }

        mStatusIconPaint.setColor(color);
        mStatusIconPaint.setStyle(style);

        invalidate();
    }

    @IWMAvatarStatus
    public int getStatus() {
        return mStatus;
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);

        setStatus(IWMAvatarStatus.OFFLINE);

        mStatusIconPaint.setStrokeWidth(STATUS_ICON_STROKE_WIDTH);
        mStatusCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //In versions > 3.0 need to define layer Type
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        setup();
    }

    private void setup() {
        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        mDrawableRect.set(calculateBounds());
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        statusIconX = (int) (mDrawableRadius * Math.cos(Math.toRadians(STATUS_ICON_ANGLE)) + mDrawableRect.centerX());
        statusIconY = (int) (mDrawableRadius * Math.sin(Math.toRadians(STATUS_ICON_ANGLE)) + mDrawableRect.centerY());

        mStatusCircleRadius = (int) (mDrawableRect.right - statusIconX);
        mStatusCircleRadius = Math.min(mStatusCircleRadius, STATUS_ICON_MAX_RADIUS);
        mStatusIconRadius = mStatusCircleRadius - Math.max(mStatusCircleRadius / 5, STATUS_ICON_MIN_MARGIN);

        loadDrawables();

        // text paint settings
        textPaint.setColor(Color.WHITE);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        updateShaderMatrix();
        invalidate();
    }

    private void loadDrawables() {

        if (useColorsAsStatusIcon)
            return;

        mStatusBitmapOffline = getBitmapFromDrawable(AndroidHelper.getVectorDrawableAntTint(getContext(), R.drawable.wm_av_status_icon_offline, mStatusColorOffline), true);
        mStatusBitmapOnline = getBitmapFromDrawable(AndroidHelper.getVectorDrawableAntTint(getContext(), R.drawable.wm_av_status_icon_online, mStatusColorOnline), true);
        mStatusBitmapAway = getBitmapFromDrawable(AndroidHelper.getVectorDrawableAntTint(getContext(), R.drawable.wm_av_status_icon_away, mStatusColorAway), true);
        mStatusBitmapBusy = getBitmapFromDrawable(AndroidHelper.getVectorDrawableAntTint(getContext(), R.drawable.wm_av_status_icon_busy, mStatusColorBusy), true);
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable(), false);
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable, boolean isStatusIconDrawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else if (!isStatusIconDrawable){
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(mStatusCircleRadius*2, mStatusCircleRadius*2, BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private RectF calculateBounds() {
        int availableWidth  = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }




    static class SavedState extends BaseSavedState {

        @IWMAvatarStatus
        int status;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            //noinspection WrongConstant
            status = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(status);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
