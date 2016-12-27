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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;


/**
 *
 */
public class WMAvatarView extends ImageView {

    /**/
    @SuppressWarnings("unused")
    private static final String TAG = "wm::WMAvatarView";

    public static ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

    /**/
    private static final ScaleType SCALE_TYPE           = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG    = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMENSION   = 2;
    private static final int STATUS_ICON_ANGLE          = 45;
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
    private int          mStatusMaxRadius;
    private int          mStatusCircleRadius; // радиус внешнего круга
    private int          mStatusIconRadius; // радиус кружка статуса
    private static final Paint  mStatusCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
    private Drawable mStatusDrawableOffline;
    private Drawable mStatusDrawableOnline;
    private Drawable mStatusDrawableAway;
    private Drawable mStatusDrawableBusy;

    /**
     * Text variables
     */
    private String       mText = "";
    @ColorInt
    private int          mTextBackgroundColor   = Color.BLACK;
    private final Paint  textBackgroundPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint  textPaint              = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * состояния вью: офлайн, оналайн, away, busy
     */
    @IWMAvatarStatus
    private int mStatus;

    /**
     * Меню выбора статуса (away/busy/...)
     */
    private boolean isPopupMenuVisible;
    private boolean isPopupMenuOfflineItemVisible;
    private IWMAvatarStatusChangedListener statusChangedListener;
    private PopupMenu popup;


    @Keep
    public WMAvatarView(Context context) {
        super(context);
        init();
    }

    @Keep
    public WMAvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Keep
    public WMAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WMAvatarView, defStyleAttr, 0);

        isStatusVisible                 = a.getBoolean(R.styleable.WMAvatarView_wm_av_status_visible,               true);
        useColorsAsStatusIcon           = a.getBoolean(R.styleable.WMAvatarView_wm_av_status_as_color,              true);
        mStatusColorOffline             = a.getColor(R.styleable.WMAvatarView_wm_av_status_offline_color,           Color.GREEN);
        mStatusColorOnline              = a.getColor(R.styleable.WMAvatarView_wm_av_status_online_color,            Color.GREEN);
        mStatusColorAway                = a.getColor(R.styleable.WMAvatarView_wm_av_status_away_color,              Color.YELLOW);
        mStatusColorBusy                = a.getColor(R.styleable.WMAvatarView_wm_av_status_busy_color,              Color.RED);
        isPopupMenuVisible              = a.getBoolean(R.styleable.WMAvatarView_wm_av_is_popup_menu_visible,        true);
        isPopupMenuOfflineItemVisible   = a.getBoolean(R.styleable.WMAvatarView_wm_av_is_offline_menu_item_visible, false);

        a.recycle();

        if (!useColorsAsStatusIcon) {
            mStatusDrawableOffline = AndroidHelper.getVectorDrawableAntTint(context, R.drawable.wm_av_status_icon_offline, mStatusColorOffline);
            mStatusDrawableOnline = AndroidHelper.getVectorDrawableAntTint(context, R.drawable.wm_av_status_icon_online, mStatusColorOnline);
            mStatusDrawableAway = AndroidHelper.getVectorDrawableAntTint(context, R.drawable.wm_av_status_icon_away, mStatusColorAway);
            mStatusDrawableBusy = AndroidHelper.getVectorDrawableAntTint(context, R.drawable.wm_av_status_icon_busy, mStatusColorBusy);
        }

        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isStatusVisible && isPopupMenuVisible) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                float x = event.getX();
                float y = event.getY();
                boolean isTouchStatus = Math.pow(x - statusIconX, 2) + Math.pow(y - statusIconY, 2) < Math.pow(mStatusCircleRadius*2, 2);
                if (isTouchStatus) {
                    showPopup();
                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.status = mStatus;
        ss.text = mText;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setStatus(ss.status);
        setText(ss.text);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int size = Math.min(heightSize, widthSize);

        //MUST CALL THIS
        setMeasuredDimension(size, size);
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
            // draw background

            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, textBackgroundPaint);

            // draw text
            textPaint.setTextSize(Math.min(mDrawableRect.width(), mDrawableRect.height()) / 2);
            canvas.drawText(mText, mDrawableRect.centerX(), mDrawableRect.centerY() - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
        } else {
            canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint);
        }

        if (isStatusVisible) {
            // hole
            canvas.drawCircle(statusIconX, statusIconY, mStatusCircleRadius, mStatusCirclePaint);
            if (useColorsAsStatusIcon) {
                canvas.drawCircle(statusIconX, statusIconY, mStatusIconRadius, mStatusIconPaint);
            } else {

                int dx = statusIconX - mStatusCircleRadius;
                int dy = statusIconY - mStatusCircleRadius;

                switch (mStatus) {

                    case IWMAvatarStatus.AWAY:
                        mStatusDrawableAway.setBounds(dx, dy, dx+mStatusCircleRadius*2, dy+mStatusCircleRadius*2);
                        mStatusDrawableAway.draw(canvas);
                        break;
                    case IWMAvatarStatus.BUSY:
                        mStatusDrawableBusy.setBounds(dx, dy, dx+mStatusCircleRadius*2, dy+mStatusCircleRadius*2);
                        mStatusDrawableBusy.draw(canvas);
                        break;
                    case IWMAvatarStatus.OFFLINE:
                        mStatusDrawableOffline.setBounds(dx, dy, dx+mStatusCircleRadius*2, dy+mStatusCircleRadius*2);
                        mStatusDrawableOffline.draw(canvas);
                        break;
                    case IWMAvatarStatus.ONLINE:
                        mStatusDrawableOnline.setBounds(dx, dy, dx+mStatusCircleRadius*2, dy+mStatusCircleRadius*2);
                        mStatusDrawableOnline.draw(canvas);
                        break;
                }


            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        statusChangedListener = null;

        if (popup != null)
            popup.dismiss();
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

    @Keep
    public void setText(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            this.mText = newText.substring(0, 1).toUpperCase();
        } else {
            this.mText = "";
        }

        mTextBackgroundColor = colorGenerator.getColor(mText);
        textBackgroundPaint.setColor(mTextBackgroundColor);

        invalidate();
    }

    @Keep
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

    public void setStatusChangedListener(IWMAvatarStatusChangedListener statusChangedListener) {

        // no listener set if menu not visible
        if (!isPopupMenuVisible)
            return;

        this.statusChangedListener = statusChangedListener;
    }

    private void init() {
        super.setScaleType(SCALE_TYPE);

        mStatusMaxRadius = getResources().getInteger(R.integer.status_icon_max_radius);
        mStatusCircleRadius    = mStatusMaxRadius; // радиус внешнего круга
        mStatusIconRadius      = mStatusMaxRadius;

        mStatusIconPaint.setStrokeWidth(STATUS_ICON_STROKE_WIDTH);
        mStatusCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //In versions > 3.0 need to define layer Type
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
        {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        mStatusIconPaint.setColor(mStatusColorOffline);
        mStatusIconPaint.setStyle(Paint.Style.STROKE);

        setup();
    }

    private void setup() {

        Log.d(TAG, "setup(" + getWidth() + ", " + getHeight() + ", " + mBitmap + ")");

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap != null) {
            mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapPaint.setAntiAlias(true);
            mBitmapPaint.setShader(mBitmapShader);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();

            Log.d(TAG, "mBitmapHeight: "+mBitmapHeight+", mBitmapWidth: "+mBitmapWidth);
        }

        mDrawableRect.set(calculateBounds());
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        statusIconX = (int) (mDrawableRadius * Math.cos(Math.toRadians(STATUS_ICON_ANGLE)) + mDrawableRect.centerX());
        statusIconY = (int) (mDrawableRadius * Math.sin(Math.toRadians(STATUS_ICON_ANGLE)) + mDrawableRect.centerY());

        mStatusCircleRadius = (int) (mDrawableRect.right - statusIconX);
        mStatusCircleRadius = Math.min(mStatusCircleRadius, mStatusMaxRadius);
        mStatusIconRadius = mStatusCircleRadius - Math.max(mStatusCircleRadius / 5, STATUS_ICON_MIN_MARGIN);

        // text paint settings
        mTextBackgroundColor = colorGenerator.getColor(mText);
        textBackgroundPaint.setColor(mTextBackgroundColor);
        textPaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        textPaint.setColor(Color.WHITE);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        if (mBitmap != null)
            updateShaderMatrix();

        invalidate();
    }

    private void initializeBitmap() {
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

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
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
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

    public void showPopup() {
        popup = new PopupMenu(getContext(), this);
        popup.inflate(R.menu.wm_av_status_popup);

        popup.getMenu().findItem(R.id.wmAvMenuItemOffline).setVisible(isPopupMenuOfflineItemVisible);

        popup.getMenu().findItem(R.id.wmAvMenuItemOnline).setIcon(mStatusDrawableOnline);
        popup.getMenu().findItem(R.id.wmAvMenuItemOffline).setIcon(mStatusDrawableOffline);
        popup.getMenu().findItem(R.id.wmAvMenuItemAway).setIcon(mStatusDrawableAway);
        popup.getMenu().findItem(R.id.wmAvMenuItemBusy).setIcon(mStatusDrawableBusy);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (statusChangedListener != null) {
                    if (id == R.id.wmAvMenuItemOnline) {
                        statusChangedListener.onWMAvatarStatusChanged(IWMAvatarStatus.ONLINE);
                        return true;
                    } else if (id == R.id.wmAvMenuItemOffline) {
                        statusChangedListener.onWMAvatarStatusChanged(IWMAvatarStatus.OFFLINE);
                        return true;
                    } else if (id == R.id.wmAvMenuItemAway) {
                        statusChangedListener.onWMAvatarStatusChanged(IWMAvatarStatus.AWAY);
                        return true;
                    } else if (id == R.id.wmAvMenuItemBusy) {
                        statusChangedListener.onWMAvatarStatusChanged(IWMAvatarStatus.BUSY);
                        return true;
                    } else
                        return false;
                } else
                    return false;
            }
        });

        popup.show();
    }

    /**
     *
     */
    static class SavedState extends BaseSavedState {

        @IWMAvatarStatus
        int status;

        String text;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            //noinspection WrongConstant
            status = in.readInt();
            text = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(status);
            out.writeString(text);
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
