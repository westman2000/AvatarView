package corp.wmsoft.android.lib.wmavatarview;


import android.support.annotation.Keep;

/**
 *
 */
@Keep
public interface IWMAvatarStatusChangedListener {

    void onWMAvatarStatusChanged(@IWMAvatarStatus int newStatus);

}
