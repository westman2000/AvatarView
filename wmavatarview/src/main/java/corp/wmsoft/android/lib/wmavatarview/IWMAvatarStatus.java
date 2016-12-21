package corp.wmsoft.android.lib.wmavatarview;

import android.support.annotation.IntDef;
import android.support.annotation.Keep;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Status constants
 */
@Keep
@Documented
@IntDef({
        IWMAvatarStatus.OFFLINE,
        IWMAvatarStatus.ONLINE,
        IWMAvatarStatus.AWAY,
        IWMAvatarStatus.BUSY
})
@Retention(RetentionPolicy.SOURCE)
public @interface IWMAvatarStatus {

    /**
     *
     */
    int OFFLINE = 0;

    /**
     *
     */
    int ONLINE = 1;

    /**
     *
     */
    int AWAY = 2;

    /**
     *
     */
    int BUSY = 3;
}
