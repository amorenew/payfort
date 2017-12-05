package com.payfort.fort.android.sdk.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.payfort.sdk.android.dependancies.models.SdkResponse;

import java.util.Map;

public interface CreditPaymentService {
    String formatAmount(Map<String, String> paramMap, int paramInt);

    boolean displayRememberMe(Map<String, String> paramMap);

    String getCardTypeIcon(String paramString, Context paramContext);

    void addErrorTheme(TextView paramTextView, TextInputLayout paramTextInputLayout, String paramString);

    void removeErrorTheme(TextView paramTextView, TextInputLayout paramTextInputLayout);

    void displayConnectionAlert(Activity paramActivity);

    String getEditTextValue(EditText paramEditText, boolean paramBoolean);

    void formatExpiryDate(EditText paramEditText, String paramString1, String paramString2);

    void displayCancelPaymentDialog(Activity paramActivity, AlertDialog.Builder paramBuilder, AlertDialog paramAlertDialog);

    void setup3DsWebView(WebView paramWebView);

    void backToMerchant(Activity paramActivity, SdkResponse paramSdkResponse);

    void showResponseActivity(Activity paramActivity, SdkResponse paramSdkResponse, FortRequest paramFortRequest, String paramString);

    void validateCardNumber(Activity paramActivity, Map<String, String> paramMap, EditText paramEditText, TextView paramTextView, TextInputLayout paramTextInputLayout, boolean paramBoolean1, boolean paramBoolean2);

    void validateCVV(Activity paramActivity, Map<String, String> paramMap, EditText paramEditText1, TextView paramTextView, TextInputLayout paramTextInputLayout, EditText paramEditText2, boolean paramBoolean1, boolean paramBoolean2);

    boolean validateExpiryDate(Activity paramActivity, EditText paramEditText, TextView paramTextView, TextInputLayout paramTextInputLayout, boolean paramBoolean);
}