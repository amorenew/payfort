/*     */
package com.payfort.fort.android.sdk.service.impl;
/*     */ 
/*     */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.payfort.fort.android.sdk.R;
import com.payfort.fort.android.sdk.activities.CreditCardResponseActivity;
import com.payfort.fort.android.sdk.service.CreditPaymentService;
import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.utils.Utils;
import com.payfort.sdk.android.dependancies.utils.Validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */

/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreditPaymentServiceImpl
/*     */ implements CreditPaymentService
/*     */ {
    /*     */
    public String formatAmount(Map<String, String> merchantRequestMap, int decimalPoints)
/*     */ {
/*  46 */
        String amount = Utils.getParamValue(merchantRequestMap, "amount");
/*  47 */
        String currency = Utils.getParamValue(merchantRequestMap, "currency");
/*  48 */
        BigInteger amountAsBigInteger = new BigInteger(amount);
/*  49 */
        BigDecimal amountAsBigDecimal = new BigDecimal(amountAsBigInteger, decimalPoints);
/*  50 */
        String amountText = amountAsBigDecimal.toString() + " " + currency.toUpperCase();
/*  51 */
        return amountText;
/*     */
    }

    /*     */
/*     */
    public boolean displayRememberMe(Map<String, String> merchantRequestMap)
/*     */ {
/*  56 */
        String rememberMe = merchantRequestMap.get("remember_me");
/*  57 */
        if ((rememberMe == null) || (rememberMe.equals("YES"))) {
/*  58 */
            return true;
/*     */
        }
/*  60 */
        return false;
/*     */
    }

    /*     */
/*     */
    public String getCardTypeIcon(String paymentOption, Context context)
/*     */ {
/*  65 */
        String icon = null;
/*  66 */
        switch (paymentOption) {
/*     */
            case "VISA":
/*  68 */
                icon = context.getResources().getString(R.string.icon_cc_visa);
/*  69 */
                break;
/*     */
            case "MASTERCARD":
/*  71 */
                icon = context.getResources().getString(R.string.icon_cc_mastercard);
/*  72 */
                break;
/*     */
            case "AMEX":
/*  74 */
                icon = context.getResources().getString(R.string.icon_cc_amex);
/*     */
        }
/*     */     
/*  77 */
        return icon;
/*     */
    }

    /*     */
/*     */
    public void addErrorTheme(TextView errorValueTV, TextInputLayout errorTIL, String msg)
/*     */ {
/*  82 */
        errorValueTV.setText(msg);
/*  83 */
        errorValueTV.setVisibility(View.VISIBLE);
/*  84 */
        errorTIL.setError(" ");
/*     */
    }

    /*     */
/*     */
    public void removeErrorTheme(TextView errorValueTV, TextInputLayout errorTIL)
/*     */ {
/*  89 */
        errorValueTV.setVisibility(View.GONE);
/*  90 */
        if (errorTIL.getError() != null) {
/*  91 */
            errorTIL.setError(null);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public void displayConnectionAlert(final Activity activity)
/*     */ {
/*  97 */
        SnackbarManager.show(
/*     */     
/*     */ 
/*     */ 
/* 101 */       Snackbar.with(activity.getApplicationContext()).text(activity.getResources().getString(R.string.pf_no_connection)).type(SnackbarType.MULTI_LINE).actionLabel(activity.getResources().getString(R.string.pf_wifi_settings)).actionListener(new ActionClickListener()
/*     */ {
                    /*     */
/* 104 */
                    public void onActionClicked(Snackbar snackbar) {
                        activity.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
                    }
                }), activity);
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    public String getEditTextValue(EditText editText, boolean doTrim)
/*     */ {
/* 112 */
        if (doTrim) {
/* 113 */
            return "" + editText.getText().toString().trim();
/*     */
        }
/* 115 */
        return "" + editText.getText().toString();
/*     */
    }

    /*     */
/*     */
    public void formatExpiryDate(EditText expiryDateET, String lastInput, String currentInput)
/*     */ {
/* 120 */
        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.US);
/* 121 */
        Calendar expiryDateDate = Calendar.getInstance();
/*     */
        try {
/* 123 */
            expiryDateDate.setTime(formatter.parse(currentInput));
/*     */
        } catch (ParseException e) {
/* 125 */
            if ((currentInput.length() == 2) && (!lastInput.endsWith("/"))) {
/* 126 */
                int month = Integer.parseInt(currentInput.replace("/", ""));
/* 127 */
                if (month <= 12) {
/* 128 */
                    expiryDateET.setText(expiryDateET.getText().toString() + "/");
/* 129 */
                    expiryDateET.setSelection(expiryDateET.getText().toString().length());
/*     */
                } else {
/* 131 */
                    expiryDateET.setText("0" + expiryDateET.getText().toString().charAt(0) + "/" + expiryDateET.getText().toString().charAt(1));
/* 132 */
                    expiryDateET.setSelection(expiryDateET.getText().toString().length());
/*     */
                }
/* 134 */
            } else if ((currentInput.length() == 2) && (lastInput.endsWith("/"))) {
/* 135 */
                int month = Integer.parseInt(currentInput.replace("/", ""));
/* 136 */
                if (month <= 12) {
/* 137 */
                    expiryDateET.setText(expiryDateET.getText().toString().substring(0, 1));
/* 138 */
                    expiryDateET.setSelection(expiryDateET.getText().toString().length());
/*     */
                } else {
/* 140 */
                    expiryDateET.setText("");
/* 141 */
                    expiryDateET.setSelection(expiryDateET.getText().toString().length());
/*     */
                }
/* 143 */
            } else if (currentInput.length() == 1) {
/* 144 */
                int month = Integer.parseInt(currentInput.replace("/", ""));
/* 145 */
                if (month > 1) {
/* 146 */
                    expiryDateET.setText("0" + expiryDateET.getText().toString() + "/");
/* 147 */
                    expiryDateET.setSelection(expiryDateET.getText().toString().length());
/*     */
                }
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public void displayCancelPaymentDialog(final Activity activity, AlertDialog.Builder dialogBuilder, AlertDialog cancelDialog)
/*     */ {
/* 155 */
        if ((dialogBuilder == null) || (cancelDialog == null)) {
/* 156 */
            dialogBuilder = new AlertDialog.Builder(activity);
/* 157 */
            cancelDialog = dialogBuilder.create();
/* 158 */
            cancelDialog.requestWindowFeature(1);
/* 159 */
            dialogBuilder.setTitle(null);
/* 160 */
            dialogBuilder.setMessage(activity.getResources().getString(R.string.pf_cancel_payment_msg));
/* 161 */
            dialogBuilder.setPositiveButton(activity.getResources().getString(R.string.pf_cancel_payment_btn_yes), new DialogInterface.OnClickListener()
/*     */ {
                /*     */
                public void onClick(DialogInterface dialog, int which) {
/* 164 */
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent("responseEvent"));
/* 165 */
                    dialog.dismiss();
/* 166 */
                    activity.finish();
/*     */
                }
/* 168 */
            });
/* 169 */
            dialogBuilder.setNegativeButton(activity.getResources().getString(R.string.pf_cancel_payment_btn_no), new DialogInterface.OnClickListener()
/*     */ {
                /*     */
                public void onClick(DialogInterface dialog, int which) {
/* 172 */
                    dialog.dismiss();
/*     */
                }
/*     */
            });
/*     */
        }
/* 176 */
        dialogBuilder.show();
/*     */
    }

    /*     */
/*     */
    public void setup3DsWebView(WebView webView)
/*     */ {
/* 181 */
        webView.getSettings().setJavaScriptEnabled(true);
/* 182 */
        webView.getSettings().setAppCacheEnabled(false);
/* 183 */
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
/* 184 */
        webView.getSettings().setDisplayZoomControls(true);
/* 185 */
        webView.getSettings().setSupportZoom(true);
/*     */     
/* 187 */
        webView.setVerticalScrollBarEnabled(true);
/* 188 */
        webView.setHorizontalScrollBarEnabled(true);
/*     */
    }

    /*     */
/*     */
    public void backToMerchant(Activity activity, SdkResponse sdkResponse)
/*     */ {
/* 193 */
        Intent broadcastIntent = new Intent("responseEvent");
/* 194 */
        broadcastIntent.putExtra("sdkResp", sdkResponse);
/* 195 */
        LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcastIntent);
/* 196 */
        activity.finish();
/*     */
    }

    /*     */
/*     */
    public void showResponseActivity(Activity activity, SdkResponse sdkResponse, FortRequest fortRequest, String defaultLocalLanguage)
/*     */ {
/* 201 */
        Intent intent = new Intent(activity, CreditCardResponseActivity.class);
/* 202 */
        intent.putExtra("merchantReq", fortRequest);
/* 203 */
        intent.putExtra("sdkResp", sdkResponse);
/* 204 */
        intent.putExtra("defaultLocale", defaultLocalLanguage);
/* 205 */
        activity.startActivity(intent);
/* 206 */
        activity.finish();
/*     */
    }

    /*     */
/*     */
    public void validateCardNumber(Activity activity, Map<String, String> merchantRequestMap, EditText cardNumberET, TextView cardNumberErrorTV, TextInputLayout cardNumberIL, boolean hasSpecificPaymentOption, boolean hasFocus)
/*     */ {
/* 211 */
        String currentCardNumber = getEditTextValue(cardNumberET, true);
/* 212 */
        if ((currentCardNumber.length() == 16) && (currentCardNumber.contains("*"))) {
/* 213 */
            return;
/*     */
        }
/*     */     
/* 216 */
        int validLength = 16;
/* 217 */
        String paymentOptionName = null;
/* 218 */
        if (hasSpecificPaymentOption) {
/* 219 */
            paymentOptionName = Utils.getPaymentOptionValue(merchantRequestMap);
/*     */
        }
/* 221 */
        else if (currentCardNumber.length() >= 4) {
/* 222 */
            paymentOptionName = Validate.getPaymentMethodOptionName(currentCardNumber);
/*     */
        }
/*     */     
/* 225 */
        if ((paymentOptionName != null) && (paymentOptionName.equals("AMEX"))) {
/* 226 */
            validLength = 15;
/*     */
        }
/*     */     
/* 229 */
        if ((!hasFocus) && (currentCardNumber.length() != validLength) && (currentCardNumber.length() != 0)) {
/* 230 */
            String message = activity.getResources().getString(R.string.pf_errors_card_number_length);
/* 231 */
            if (validLength == 15) {
/* 232 */
                message = activity.getResources().getString(R.string.pf_errors_card_number_amex_length);
/*     */
            }
/* 234 */
            addErrorTheme(cardNumberErrorTV, cardNumberIL, message);
/* 235 */
        } else if ((!hasFocus) && (currentCardNumber.length() == validLength)) {
/* 236 */
            String cardPaymentOptionName = Validate.getPaymentMethodOptionName(currentCardNumber.substring(0, 4));
/* 237 */
            if ((hasSpecificPaymentOption) && ((cardPaymentOptionName == null) || (cardPaymentOptionName.trim().isEmpty()) || (!cardPaymentOptionName.equals(Utils.getPaymentOptionValue(merchantRequestMap))))) {
/* 238 */
                addErrorTheme(cardNumberErrorTV, cardNumberIL, activity.getResources().getString(R.string.pf_errors_card_number_mismatch_po));
/* 239 */
            } else if (!Validate.luhnValidation(currentCardNumber)) {
/* 240 */
                addErrorTheme(cardNumberErrorTV, cardNumberIL, activity.getResources().getString(R.string.pf_errors_card_number_invalid));
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */
    public void validateCVV(Activity activity, Map<String, String> merchantRequestMap, EditText cvvET, TextView cvvErrorTV, TextInputLayout cvvIL, EditText cardNumberET, boolean hasSpecificPaymentOption, boolean hasFocus)
/*     */ {
/* 248 */
        String currentCVV = getEditTextValue(cvvET, true);
/* 249 */
        int validLength = 3;
/* 250 */
        String currentCardNumber = getEditTextValue(cardNumberET, true);
/* 251 */
        if (currentCardNumber.length() >= 4) {
/*     */
            String paymentOptionName;
/* 253 */
            if (hasSpecificPaymentOption) {
/* 254 */
                paymentOptionName = Utils.getPaymentOptionValue(merchantRequestMap);
/*     */
            } else {
/* 256 */
                paymentOptionName = Validate.getPaymentMethodOptionName(currentCardNumber);
/*     */
            }
/*     */       
/* 259 */
            if ((paymentOptionName != null) && (paymentOptionName.equals("AMEX"))) {
/* 260 */
                validLength = 4;
/*     */
            }
/*     */
        } else {
/* 263 */
            return;
/*     */
        }
/*     */     
/* 266 */
        if ((!hasFocus) && (currentCVV.length() != validLength) && (currentCVV.length() != 0))
/*     */ {
/* 268 */
            String message = activity.getResources().getString(R.string.pf_cancel_cvv_length);
/* 269 */
            if (validLength == 4) {
/* 270 */
                message = activity.getResources().getString(R.string.pf_cancel_cvv_amex_length);
/*     */
            }
/* 272 */
            addErrorTheme(cvvErrorTV, cvvIL, message);
/*     */
        }
/*     */
    }

    /*     */
/*     */ 
/*     */
    public boolean validateExpiryDate(Activity activity, EditText expiryDateET, TextView expDateErrorTV, TextInputLayout expiryDateIL, boolean hasFocus)
/*     */ {
/* 279 */
        if (hasFocus) {
/* 280 */
            expiryDateIL.setHint(activity.getResources().getText(R.string.pf_expiry_date_value));
/*     */
        } else {
/* 282 */
            String currentValue = getEditTextValue(expiryDateET, true);
/* 283 */
            expiryDateIL.setHint(activity.getResources().getText(R.string.pf_expiry_date_hint));
/* 284 */
            if ((currentValue.length() != 0) && (currentValue.length() != 5)) {
/* 285 */
                addErrorTheme(expDateErrorTV, expiryDateIL, activity.getResources().getString(R.string.pf_cancel_exp_date_invalid));
/* 286 */
                return false;
            }
/* 287 */
            if (currentValue.length() == 5) {
/* 288 */
                Calendar expiryDateDate = Calendar.getInstance();
/* 289 */
                int currentMonth = expiryDateDate.get(Calendar.MONTH) + 1;
/* 290 */
                String currentYear = String.valueOf(expiryDateDate.get(Calendar.YEAR));
/*     */         
/* 292 */
                if ((!currentValue.contains("/")) || (currentValue.substring(0, 2).replace("/", "").equals("00"))) {
/* 293 */
                    addErrorTheme(expDateErrorTV, expiryDateIL, activity.getResources().getString(R.string.pf_cancel_exp_date_invalid));
/* 294 */
                    return false;
                }
/* 295 */
                if (Integer.valueOf(currentYear.substring(2).replace("/", "")).intValue() > Integer.valueOf(currentValue.substring(3).replace("/", "")).intValue()) {
/* 296 */
                    addErrorTheme(expDateErrorTV, expiryDateIL, activity.getResources().getString(R.string.pf_cancel_exp_date_in_past));
/* 297 */
                    return false;
                }
/* 298 */
                if ((Integer.valueOf(currentYear.substring(2).replace("/", "")) == Integer.valueOf(currentValue.substring(3).replace("/", ""))) &&
/* 299 */           (currentMonth > Integer.valueOf(currentValue.substring(0, 2).replace("/", "")).intValue())) {
/* 300 */
                    addErrorTheme(expDateErrorTV, expiryDateIL, activity.getResources().getString(R.string.pf_cancel_exp_date_in_past));
/* 301 */
                    return false;
/*     */
                }
/*     */
            }
/*     */
        }
/*     */     
/* 306 */
        return true;
/*     */
    }
/*     */
}


