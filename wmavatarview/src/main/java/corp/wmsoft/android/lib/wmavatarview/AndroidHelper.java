package corp.wmsoft.android.lib.wmavatarview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;


/**
 * Useful methods for android
 */
public class AndroidHelper {

    /**
     * Get drawable by res id, needed to support API lover than 21 for vector drawable
     * @param context context
     * @param drawableResId vector drawable id
     * @return drawable
     */
    public static Drawable getVectorDrawable(Context context, @DrawableRes int drawableResId) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getDrawable(context, drawableResId);
        } else {
            return VectorDrawableCompat.create(context.getResources(), drawableResId, context.getTheme());
        }
    }

    /**
     * get vector drawable by id, and apply tint color
     * @param context context
     * @param drawableResId vector drawable id
     * @param colorToTint color
     * @return tined vector drawable
     */
    public static Drawable getVectorDrawableAntTint(Context context, @DrawableRes int drawableResId, @ColorInt int colorToTint) {
        Drawable drawable = DrawableCompat.wrap(getVectorDrawable(context, drawableResId));
        DrawableCompat.setTint(drawable, colorToTint);
        return drawable;
    }
}