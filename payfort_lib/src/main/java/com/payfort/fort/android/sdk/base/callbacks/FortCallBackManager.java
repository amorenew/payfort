/*    */
package com.payfort.fort.android.sdk.base.callbacks;
/*    */ 
/*    */

import android.content.Intent;

/*    */
/*    */ 
/*    */ public interface FortCallBackManager
/*    */ {
    /*    */    boolean onActivityResult(int paramInt1, int paramInt2, Intent paramIntent);

    /*    */
/*    */    class Factory
/*    */ {
        /*    */
        public static FortCallBackManager create()
/*    */ {
/* 14 */
            return new FortCallback();
/*    */
        }
/*    */
    }
/*    */
}