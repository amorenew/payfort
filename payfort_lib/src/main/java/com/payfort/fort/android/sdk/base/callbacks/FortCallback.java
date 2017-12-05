/*    */
package com.payfort.fort.android.sdk.base.callbacks;
/*    */ 
/*    */

import android.content.Intent;

import com.payfort.sdk.android.dependancies.utils.Validate;

import java.util.HashMap;
import java.util.Map;

/*    */
/*    */
/*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FortCallback
/*    */ implements FortCallBackManager
/*    */ {
    /* 15 */   private static Map<Integer, Callback> callbacks = new HashMap();

    /*    */
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */
    public void registerCallback(int requestCode, Callback callback)
/*    */ {
/* 26 */
        Validate.notNull(callback, "callback");
/* 27 */
        callbacks.put(Integer.valueOf(requestCode), callback);
/*    */
    }

    /*    */
/*    */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data)
/*    */ {
/* 32 */
        Callback callback = callbacks.get(Integer.valueOf(requestCode));
/* 33 */
        if (callback != null) {
/* 34 */
            return callback.onActivityResult(requestCode, resultCode, data);
/*    */
        }
/* 36 */
        return false;
/*    */
    }

    /*    */
/*    */   public interface Callback
/*    */ {
        /*    */      boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent);
/*    */
    }
/*    */
}