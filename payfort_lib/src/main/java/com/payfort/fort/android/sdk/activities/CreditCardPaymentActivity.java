/*     */
package com.payfort.fort.android.sdk.activities;
/*     */ 
/*     */

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.R;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.SdkUtils;
import com.payfort.fort.android.sdk.service.CreditPaymentService;
import com.payfort.fort.android.sdk.service.impl.CreditPaymentServiceImpl;
import com.payfort.sdk.android.dependancies.connection.ConnectionAdapter;
import com.payfort.sdk.android.dependancies.exceptions.FortException;
import com.payfort.sdk.android.dependancies.models.MerchantToken;
import com.payfort.sdk.android.dependancies.models.SdkRequest;
import com.payfort.sdk.android.dependancies.models.SdkResponse;
import com.payfort.sdk.android.dependancies.security.DataSecurityService;
import com.payfort.sdk.android.dependancies.security.aes.AESCipherManager;
import com.payfort.sdk.android.dependancies.utils.Utils;
import com.payfort.sdk.android.dependancies.utils.Validate;
import com.shamanland.fonticon.FontIconView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;


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
/*     */ public class CreditCardPaymentActivity extends FortActivity
/*     */ {
    /*     */   private EditText expiryDateET;
    /*     */   private EditText cvvET;
    /*     */   private EditText cardNumberET;
    /*  61 */   private EditText holderNameET = null;
    /*  62 */   private TextInputLayout expiryDateIL;
    private TextInputLayout cvvIL;
    private TextInputLayout cardNumberIL;
    private TextInputLayout holderNameIL = null;
    /*  63 */   private FontIconView cardTypeIV = null;
    /*  64 */   private ProgressBar payLoadingPB = null;
    /*  65 */   private Button payBtn = null;
    /*  66 */   private WebView threeDsWebView = null;
    /*     */   private TextView amountTV;
    /*  68 */   private TextView cardNumberErrorTV;
    private TextView expDateErrorTV;
    private TextView cvvErrorTV;
    private RelativeLayout rememberMeRL = null;
    /*  69 */   private ToggleButton rememberMeTB = null;
    /*     */
/*  71 */   private String lastInput = "";
    /*  72 */   private boolean hasSpecificPaymentOption = false;
    /*  73 */   private AlertDialog.Builder dialogBuilder = null;
    /*  74 */   private AlertDialog cancelDialog = null;
    /*  75 */   private int currencyDecimalPoints = 0;
    /*  76 */   private MerchantToken merchantToken = null;
    /*     */
/*     */ 
/*     */ 
/*     */   private CreditPaymentService service;

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    protected void onCreate(Bundle savedInstanceState)
/*     */ {
/*  87 */
        super.onCreate(savedInstanceState);
/*  88 */
        setContentView(R.layout.activity_cc_payment);
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */
    protected void onResume()
/*     */ {
/*  95 */
        super.onResume();
/*  96 */
        if (getIntent().hasExtra("currencyDecimalPoints")) {
/*  97 */
            this.currencyDecimalPoints = getIntent().getExtras().getInt("currencyDecimalPoints");
/*     */
        }
/*     */     
/* 100 */
        if (getIntent().hasExtra("merchantToken")) {
/* 101 */
            this.merchantToken = ((MerchantToken) getIntent().getSerializableExtra("merchantToken"));
/*     */
        }
/*     */     
/* 104 */
        if (this.service == null) {
/* 105 */
            this.service = new CreditPaymentServiceImpl();
/*     */
        }
/*     */     
/* 108 */
        if (this.expiryDateET == null) {
/* 109 */
            initActivity();
/* 110 */
            String paymentOption = Utils.getPaymentOptionValue(this.merchantFortRequest.getRequestMap());
/* 111 */
            if (paymentOption != null)
/*     */ {
/*     */ 
/* 114 */
                updateCardTypeIndicator(paymentOption);
/* 115 */
                this.hasSpecificPaymentOption = true;
/*     */
            } else {
/* 117 */
                lengthController(null);
/*     */
            }
/* 119 */
            setupListeners();
/* 120 */
            this.lastInput = "";
/* 121 */
            this.expiryDateET.setText("");
/* 122 */
            fillViews();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    protected void onStart()
/*     */ {
/* 128 */
        super.onStart();
/*     */
    }

    /*     */
/*     */
    private void initActivity() {
/* 132 */
        this.expiryDateET = findViewById(R.id.expiryDateET);
/* 133 */
        this.cardNumberET = findViewById(R.id.cardNumberET);
/* 134 */
        this.holderNameET = findViewById(R.id.holderNameET);
/* 135 */
        this.cvvET = findViewById(R.id.cvvET);
/*     */     
/* 137 */
        this.expiryDateIL = findViewById(R.id.expiryDateIL);
/* 138 */
        this.cardNumberIL = findViewById(R.id.cardNumberIL);
/* 139 */
        this.holderNameIL = findViewById(R.id.holderNameIL);
/* 140 */
        this.cvvIL = findViewById(R.id.cvvIL);
/*     */     
/* 142 */
        this.cardTypeIV = findViewById(R.id.cartTypeIV);
/*     */     
/* 144 */
        this.payLoadingPB = findViewById(R.id.payLoadingPB);
/* 145 */
        this.payBtn = findViewById(R.id.payBtn);
/*     */     
/* 147 */
        this.threeDsWebView = findViewById(R.id.threeDsWV);
/* 148 */
        this.amountTV = findViewById(R.id.amountTV);
/* 149 */
        this.cardNumberErrorTV = findViewById(R.id.cardNumErrorTV);
/* 150 */
        this.expDateErrorTV = findViewById(R.id.expiryDateErrorTV);
/* 151 */
        this.cvvErrorTV = findViewById(R.id.cvvErrorTV);
/*     */     
/* 153 */
        this.rememberMeRL = findViewById(R.id.rememberMeRL);
/* 154 */
        this.rememberMeTB = findViewById(R.id.rememberMeTB);
/*     */
    }

    /*     */
/*     */
    private void fillViews()
/*     */ {
/* 159 */
        this.amountTV.setText(this.service.formatAmount(this.merchantFortRequest.getRequestMap(), this.currencyDecimalPoints));
/*     */     
/*     */ 
/* 162 */
        if (this.merchantToken != null) {
/* 163 */
            if (this.merchantToken.isRememberMe())
/*     */ {
/* 165 */
                if (this.service.displayRememberMe(this.merchantFortRequest.getRequestMap()))
/*     */ {
/* 167 */
                    this.rememberMeRL.setVisibility(View.VISIBLE);
/*     */
                }
/*     */
                else {
/* 170 */
                    this.rememberMeRL.setVisibility(View.GONE);
/*     */
                }
/*     */
            }
/*     */
            else {
/* 174 */
                this.rememberMeRL.setVisibility(View.GONE);
/* 175 */
                this.cardNumberET.setText(this.merchantToken.getMaskedCardNumber());
/* 176 */
                if ((this.merchantToken.getExpDate() != null) && (!this.merchantToken.getExpDate().isEmpty()) && (this.merchantToken.getExpDate().length() >= 4)) {
/* 177 */
                    String expDate = this.merchantToken.getExpDate().substring(0, 2) + "/" + this.merchantToken.getExpDate().substring(2);
/* 178 */
                    this.expiryDateET.setText(expDate);
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
/*     */
    private void setupListeners()
/*     */ {
/* 187 */
        this.cardNumberET.setOnFocusChangeListener(new View.OnFocusChangeListener()
/*     */ {
            /*     */
            public void onFocusChange(View v, boolean hasFocus) {
/* 190 */
                CreditCardPaymentActivity.this.service.validateCardNumber(CreditCardPaymentActivity.this, CreditCardPaymentActivity.this.merchantFortRequest.getRequestMap(), CreditCardPaymentActivity.this.cardNumberET, CreditCardPaymentActivity.this.cardNumberErrorTV, CreditCardPaymentActivity.this.cardNumberIL, CreditCardPaymentActivity.this.hasSpecificPaymentOption, hasFocus);
/*     */
            }
/* 192 */
        });
/* 193 */
        TextWatcher cardNumberTW = new TextWatcher()
/*     */ {
            /*     */
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
/*     */ {
/* 197 */
                CreditCardPaymentActivity.this.service.removeErrorTheme(CreditCardPaymentActivity.this.cardNumberErrorTV, CreditCardPaymentActivity.this.cardNumberIL);
/*     */
            }

            /*     */
/*     */ 
/*     */ 
/*     */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            /*     */
/*     */ 
/*     */
            public void afterTextChanged(Editable editable)
/*     */ {
/* 207 */
                String input = editable.toString();
/* 208 */
                int count = editable.length();
/*     */         
/* 210 */
                if ((CreditCardPaymentActivity.this.merchantToken != null) && (CreditCardPaymentActivity.this.merchantToken.getMaskedCardNumber() != null) && (input.contains("*"))) {
/* 211 */
                    if (input.equals(CreditCardPaymentActivity.this.merchantToken.getMaskedCardNumber())) {
/* 212 */
                        String paymentOptionName = Validate.getPaymentMethodOptionName(input.substring(0, 4));
/* 213 */
                        CreditCardPaymentActivity.this.updateCardTypeIndicator(paymentOptionName);
/*     */
                    }
/*     */
                    else
/*     */ {
/* 217 */
                        CreditCardPaymentActivity.this.resetForm();
/* 218 */
                        return;
/*     */
                    }
/*     */
                }
/* 221 */
                if (!CreditCardPaymentActivity.this.hasSpecificPaymentOption) {
/* 222 */
                    if (count >= 4) {
/* 223 */
                        if (CreditCardPaymentActivity.this.cardTypeIV.getVisibility() != View.VISIBLE) {
/* 224 */
                            String paymentOptionName = Validate.getPaymentMethodOptionName(input);
/* 225 */
                            CreditCardPaymentActivity.this.updateCardTypeIndicator(paymentOptionName);
/*     */
                        }
/*     */
                    } else {
/* 228 */
                        CreditCardPaymentActivity.this.updateCardTypeIndicator(null);
/*     */
                    }
/*     */
                }
/*     */
            }
/* 232 */
        };
/* 233 */
        this.cardNumberET.addTextChangedListener(cardNumberTW);
/*     */     
/*     */ 
/*     */ 
/* 237 */
        this.cvvET.setOnFocusChangeListener(new View.OnFocusChangeListener()
/*     */ {
            /*     */
            public void onFocusChange(View v, boolean hasFocus) {
/* 240 */
                CreditCardPaymentActivity.this.service.validateCVV(CreditCardPaymentActivity.this, CreditCardPaymentActivity.this.merchantFortRequest.getRequestMap(), CreditCardPaymentActivity.this.cvvET, CreditCardPaymentActivity.this.cvvErrorTV, CreditCardPaymentActivity.this.cvvIL, CreditCardPaymentActivity.this.cardNumberET, CreditCardPaymentActivity.this.hasSpecificPaymentOption, hasFocus);
/*     */
            }
/*     */       
/* 243 */
        });
/* 244 */
        this.cvvET.setOnEditorActionListener(new TextView.OnEditorActionListener()
/*     */ {
            /*     */
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
/* 247 */
                if (actionId == 6) {
/* 248 */
                    InputMethodManager imm = (InputMethodManager) CreditCardPaymentActivity.this.getSystemService(INPUT_METHOD_SERVICE);
/* 249 */
                    imm.hideSoftInputFromWindow(CreditCardPaymentActivity.this.cvvET.getWindowToken(), 0);
/*     */
                }
/* 251 */
                return false;
/*     */
            }
/*     */       
/* 254 */
        });
/* 255 */
        TextWatcher cvvTW = new TextWatcher()
/*     */ {
            /*     */
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
            public void afterTextChanged(Editable s)
/*     */ {
/* 269 */
                CreditCardPaymentActivity.this.service.removeErrorTheme(CreditCardPaymentActivity.this.cvvErrorTV, CreditCardPaymentActivity.this.cvvIL);
/*     */
            }
/* 271 */
        };
/* 272 */
        this.cvvET.addTextChangedListener(cvvTW);
/*     */     
/*     */ 
/*     */ 
/* 276 */
        this.expiryDateET.setOnFocusChangeListener(new View.OnFocusChangeListener()
/*     */ {
            /*     */
            public void onFocusChange(View v, boolean hasFocus) {
/* 279 */
                CreditCardPaymentActivity.this.service.validateExpiryDate(CreditCardPaymentActivity.this, CreditCardPaymentActivity.this.expiryDateET, CreditCardPaymentActivity.this.expDateErrorTV, CreditCardPaymentActivity.this.expiryDateIL, hasFocus);
/*     */
            }
/* 281 */
        });
/* 282 */
        TextWatcher expiryDateTW = new TextWatcher()
/*     */ {
            /*     */
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
/* 285 */
                CreditCardPaymentActivity.this.service.removeErrorTheme(CreditCardPaymentActivity.this.expDateErrorTV, CreditCardPaymentActivity.this.expiryDateIL);
/*     */
            }

            /*     */
/*     */ 
/*     */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            /*     */
/*     */ 
/*     */
            public void afterTextChanged(Editable s)
/*     */ {
/* 294 */
                CreditCardPaymentActivity.this.service.formatExpiryDate(CreditCardPaymentActivity.this.expiryDateET, CreditCardPaymentActivity.this.lastInput, s.toString());
/* 295 */
                CreditCardPaymentActivity.this.lastInput = CreditCardPaymentActivity.this.expiryDateET.getText().toString();
/*     */
            }
/* 297 */
        };
/* 298 */
        this.expiryDateET.addTextChangedListener(expiryDateTW);
/*     */
    }

    /*     */
/*     */
    public void onBackPressed(View v) {
/* 302 */
        onBackPressed();
/*     */
    }

    /*     */
/*     */
    public void onBackPressed()
/*     */ {
/* 307 */
        if ((this.threeDsWebView.canGoBack()) && (this.threeDsWebView.getVisibility() == View.VISIBLE)) {
/* 308 */
            this.threeDsWebView.goBack();
/* 309 */
            return;
/*     */
        }
/*     */     
/* 312 */
        if ((this.payLoadingPB.getVisibility() != View.VISIBLE) || (this.threeDsWebView.getVisibility() == View.VISIBLE)) {
/* 313 */
            this.service.displayCancelPaymentDialog(this, this.dialogBuilder, this.cancelDialog);
/*     */
        } else {
/* 315 */
            Toast.makeText(this, getResources().getString(R.string.pf_no_back_tnx_processing), Toast.LENGTH_LONG).show();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public void onPayPressed(View v) {
/* 320 */
        if (SdkUtils.haveNetworkConnection(this)) {
/* 321 */
            String expiryDate = this.service.getEditTextValue(this.expiryDateET, true);
/* 322 */
            String cvv = this.service.getEditTextValue(this.cvvET, true);
/* 323 */
            String cardNumber = this.service.getEditTextValue(this.cardNumberET, true);
/* 324 */
            String holderName = this.service.getEditTextValue(this.holderNameET, false);
/* 325 */
            if ((validateForm(expiryDate, cvv, cardNumber)) && (this.expiryDateIL.getError() == null) && (this.cardNumberIL.getError() == null) && (this.cvvIL.getError() == null)) {
/* 326 */
                new Pay(cardNumber, holderName, cvv, expiryDate).execute();
/*     */
            }
/*     */
        } else {
/* 329 */
            this.service.displayConnectionAlert(this);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    protected void onDestroy()
/*     */ {
/* 335 */
        super.onDestroy();
/*     */
    }

    /*     */
/*     */
    private void redirectTo3DsCheck(String urlToRedirect)
/*     */ {
/* 340 */
        CookieManager.getInstance().setAcceptCookie(true);
/* 341 */
        this.service.setup3DsWebView(this.threeDsWebView);
/* 342 */
        this.threeDsWebView.setWebViewClient(new WebViewClient()
/*     */ {
            /*     */
            public void onPageFinished(WebView view, String url)
/*     */ {
/* 346 */
                super.onPageFinished(view, url);
/* 347 */
                if (!url.contains("/FortAPI/sdk/process3DsMobile")) {
/* 348 */
                    if (CreditCardPaymentActivity.this.threeDsWebView.getVisibility() != View.VISIBLE)
/* 349 */ CreditCardPaymentActivity.this.threeDsWebView.setVisibility(View.VISIBLE);
/*     */
                } else {
/* 351 */
                    if (CreditCardPaymentActivity.this.merchantFortRequest.isShowResponsePage()) {
/* 352 */
                        CreditCardPaymentActivity.this.service.showResponseActivity(CreditCardPaymentActivity.this, Utils.collectResponseFromURL(url), CreditCardPaymentActivity.this.merchantFortRequest, CreditCardPaymentActivity.this.systemDefaultLanguage);
/* 353 */
                        return;
/*     */
                    }
/* 355 */
                    CreditCardPaymentActivity.this.service.backToMerchant(CreditCardPaymentActivity.this, Utils.collectResponseFromURL(url));
/*     */
                }
/*     */         
/*     */
            }
/* 359 */
        });
/* 360 */
        this.threeDsWebView.loadUrl(urlToRedirect);
/*     */
    }

    /*     */
/*     */
    private boolean validateForm(String expiryDate, String cvv, String cardNumber) {
/* 364 */
        if (cardNumber.isEmpty()) {
/* 365 */
            this.service.addErrorTheme(this.cardNumberErrorTV, this.cardNumberIL, getResources().getString(R.string.pf_cancel_required_field));
/* 366 */
            this.cardNumberET.setFocusable(true);
/*     */
        }
/*     */     
/* 369 */
        if (expiryDate.isEmpty()) {
/* 370 */
            this.service.addErrorTheme(this.expDateErrorTV, this.expiryDateIL, getResources().getString(R.string.pf_cancel_required_field));
/* 371 */
            this.expiryDateET.setFocusable(true);
/*     */
        }
/*     */     
/* 374 */
        if (cvv.isEmpty()) {
/* 375 */
            this.service.addErrorTheme(this.cvvErrorTV, this.cvvIL, getResources().getString(R.string.pf_cancel_required_field));
/* 376 */
            this.cvvET.setFocusable(true);
/*     */
        }
/*     */     
/*     */ 
/* 380 */
        boolean isValidExpiryDate = this.service.validateExpiryDate(this, this.expiryDateET, this.expDateErrorTV, this.expiryDateIL, false);
/* 381 */
        if (!isValidExpiryDate) {
/* 382 */
            return false;
/*     */
        }
/*     */     
/* 385 */
        if (((cardNumber.length() == 16) && (cvv.length() == 3)) || ((cardNumber.length() == 15) && (cvv.length() == 4))) {
/* 386 */
            return true;
/*     */
        }
/* 388 */
        this.service.validateCVV(this, this.merchantFortRequest.getRequestMap(), this.cvvET, this.cvvErrorTV, this.cvvIL, this.cardNumberET, this.hasSpecificPaymentOption, false);
/* 389 */
        this.cvvET.setFocusable(true);
/* 390 */
        return false;
/*     */
    }

    /*     */
/*     */
    private void resetForm() {
/* 527 */
        this.cardNumberET.setText("");
/* 528 */
        this.expiryDateET.setText("");
/* 529 */
        this.cvvET.setText("");
/* 530 */
        this.holderNameET.setText("");
/* 531 */
        if ((this.service.displayRememberMe(this.merchantFortRequest.getRequestMap())) && (this.rememberMeRL.getVisibility() != View.VISIBLE)) {
/* 532 */
            this.rememberMeRL.setVisibility(View.VISIBLE);
/*     */
        }
/* 534 */
        updateCardTypeIndicator(null);
/*     */
    }

    /*     */
/*     */
    private void updateCardTypeIndicator(String paymentOption) {
/* 538 */
        if (this.hasSpecificPaymentOption) {
/* 539 */
            return;
/*     */
        }
/* 541 */
        if (paymentOption != null) {
/* 542 */
            this.cardTypeIV.setText(this.service.getCardTypeIcon(paymentOption, this));
/* 543 */
            if (this.cardTypeIV.getVisibility() != View.VISIBLE) {
/* 544 */
                lengthController(paymentOption);
/* 545 */
                this.cardTypeIV.setVisibility(View.VISIBLE);
/*     */
            }
/*     */
        } else {
/* 548 */
            this.cardTypeIV.setVisibility(View.INVISIBLE);
/* 549 */
            lengthController(null);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private void lengthController(String paymentOption) {
/* 554 */
        if (paymentOption == null)
/*     */ {
/*     */
/* 557 */
            this.cvvET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
/* 558 */
            this.cardNumberET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
/* 559 */
        } else if ("AMEX".equals(paymentOption))
/*     */ {
/*     */
/* 562 */
            this.cvvET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
/* 563 */
            this.cardNumberET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
/*     */
        }
/*     */
        else
/*     */ {
/* 567 */
            this.cvvET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
/* 568 */
            this.cardNumberET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
/*     */
        }
/*     */
/*     */
/* 572 */
        this.holderNameET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
/*     */
    }

    /*     */
/*     */   class Pay extends AsyncTask<String, Void, String>
/*     */ {
        /* 395 */     private String cardNumber = null;
        /* 396 */     private String expiryDate = null;
        /* 397 */     private String cvv = null;
        /* 398 */     private String customerName = null;
        /* 399 */     private String sdkToken = null;
        /*     */
/* 401 */     private Gson gson = null;
        /* 402 */     private SecretKeySpec secretKeySpec = null;
        /* 403 */     private AESCipherManager aesCipherManager = null;
        /*     */
/* 405 */     private String rememberMeValue = null;
        /* 406 */     private String environment = null;

        /*     */
/*     */
        public Pay(String cardNumber, String holderName, String cvv, String expiryDate) {
/* 409 */
            this.cardNumber = cardNumber;
/* 410 */
            this.expiryDate = expiryDate;
/* 411 */
            this.customerName = holderName;
/* 412 */
            this.cvv = cvv;
/*     */
        }

        /*     */
/*     */
        protected void onPreExecute()
/*     */ {
/* 417 */
            super.onPreExecute();
/* 418 */
            this.gson = new Gson();
/* 419 */
            this.aesCipherManager = new AESCipherManager();
/* 420 */
            this.sdkToken = Utils.getParamValue(CreditCardPaymentActivity.this.merchantFortRequest.getRequestMap(), "sdk_token");
/*     */
/* 422 */
            if (this.expiryDate.length() == 5) {
/* 423 */
                this.expiryDate = (this.expiryDate.substring(3) + this.expiryDate.substring(0, 2));
/*     */
            }
/*     */
/* 426 */
            CreditCardPaymentActivity.this.cardNumberET.setEnabled(false);
/* 427 */
            CreditCardPaymentActivity.this.cvvET.setEnabled(false);
/* 428 */
            CreditCardPaymentActivity.this.holderNameET.setEnabled(false);
/* 429 */
            CreditCardPaymentActivity.this.expiryDateET.setEnabled(false);
/* 430 */
            CreditCardPaymentActivity.this.payBtn.setVisibility(View.GONE);
/* 431 */
            CreditCardPaymentActivity.this.payLoadingPB.setVisibility(View.VISIBLE);
/* 432 */
            if (CreditCardPaymentActivity.this.rememberMeRL.getVisibility() == View.VISIBLE) {
/* 433 */
                if (CreditCardPaymentActivity.this.rememberMeTB.isChecked()) {
/* 434 */
                    this.rememberMeValue = "on";
/*     */
                } else {
/* 436 */
                    this.rememberMeValue = null;
/*     */
                }
/*     */
            }
/* 439 */
            CreditCardPaymentActivity.this.rememberMeTB.setEnabled(false);
/* 440 */
            this.environment = CreditCardPaymentActivity.this.getEnvironment();
/*     */
        }

        /*     */
/*     */
        protected String doInBackground(String... params)
/*     */ {
/*     */
            try {
/* 446 */
                if (this.environment == null) {
/* 447 */
                    throw new FortException("006");
/*     */
                }
/* 449 */
                HttpsURLConnection urlConnection = new ConnectionAdapter().setupConnection(this.environment, "/FortAPI/sdk/processSdkTnx");
/* 450 */
                urlConnection.connect();
/*     */
/* 452 */
                RSAPublicKey pk = DataSecurityService.getPublicKey(urlConnection);
/* 453 */
                if (pk == null) {
/* 454 */
                    return null;
/*     */
                }
/* 456 */
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
/* 457 */
                SdkRequest sdkRequest = new SdkRequest();
/* 458 */
                sdkRequest.setDeviceId(FortSdk.getDeviceId(CreditCardPaymentActivity.this));
/*     */
/* 460 */
                Map<String, String> payParamsMap = new HashMap();
/* 461 */
                payParamsMap.put("card_number", this.cardNumber);
/* 462 */
                payParamsMap.put("card_security_code", this.cvv);
/* 463 */
                payParamsMap.put("expiry_date", this.expiryDate);
/* 464 */
                payParamsMap.put("customer_name", this.customerName);
/* 465 */
                payParamsMap.put("sdk_token", this.sdkToken);
/* 466 */
                if (this.rememberMeValue != null)
/* 467 */ payParamsMap.put("remember_me", this.rememberMeValue);
/* 468 */
                sdkRequest.setRequestMap(payParamsMap);
/*     */
/* 470 */
                this.secretKeySpec = this.aesCipherManager.generateAESKey();
/* 471 */
                String encryptedData = DataSecurityService.encryptRequestData(this.gson.toJson(sdkRequest, SdkRequest.class), pk, this.secretKeySpec);
/* 472 */
                out.write(encryptedData);
/* 473 */
                out.close();
/* 474 */
                StringBuilder sb = new StringBuilder();
/* 475 */
                int HttpResult = urlConnection.getResponseCode();
/* 476 */
                if (HttpResult == 200)
/*     */ {
/* 478 */
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
/* 479 */
                    String line = null;
/* 480 */
                    while ((line = br.readLine()) != null) {
/* 481 */
                        sb.append(line + "\n");
/*     */
                    }
/* 483 */
                    br.close();
/*     */
                }
/* 485 */
                urlConnection.disconnect();
/* 486 */
                return "" + sb.toString();
/*     */
/*     */
            }
/*     */ catch (FortException fExc)
/*     */ {
/* 491 */
                SdkResponse sdkResponse = new SdkResponse();
/* 492 */
                Map<String, String> responseMap = new HashMap();
/* 493 */
                responseMap.put("status", "00");
/* 494 */
                responseMap.put("response_code", "00" + fExc.getCode());
/* 495 */
                responseMap.put("response_message", CreditCardPaymentActivity.this.getResources().getString(R.string.pf_technical_problem));
/* 496 */
                sdkResponse.setSuccess(false);
/* 497 */
                sdkResponse.setResponseMap(responseMap);
/* 498 */
                return this.gson.toJson(sdkResponse);
/*     */
            }
/*     */ catch (Exception localException) {
            }
/*     */
/* 502 */
            return null;
/*     */
        }

        /*     */
/*     */
        protected void onPostExecute(String fortResponse)
/*     */ {
/* 507 */
            super.onPostExecute(fortResponse);
/* 508 */
            fortResponse = this.aesCipherManager.decryptMsg(fortResponse, this.secretKeySpec);
/* 509 */
            SdkResponse sdkResponse = SdkUtils.collectResponse(CreditCardPaymentActivity.this, fortResponse, CreditCardPaymentActivity.this.merchantFortRequest.getRequestMap());
/*     */
/* 511 */
            if (fortResponse != null) {
/* 512 */
                if (sdkResponse.getChecker3DsURL() != null) {
/* 513 */
                    CreditCardPaymentActivity.this.redirectTo3DsCheck(sdkResponse.getChecker3DsURL());
/* 514 */
                    return;
/*     */
                }
/* 516 */
                if (CreditCardPaymentActivity.this.merchantFortRequest.isShowResponsePage()) {
/* 517 */
                    CreditCardPaymentActivity.this.service.showResponseActivity(CreditCardPaymentActivity.this, sdkResponse, CreditCardPaymentActivity.this.merchantFortRequest, CreditCardPaymentActivity.this.systemDefaultLanguage);
/* 518 */
                    return;
/*     */
                }
/*     */
            }
/*     */
/* 522 */
            CreditCardPaymentActivity.this.service.backToMerchant(CreditCardPaymentActivity.this, sdkResponse);
/*     */
        }
/*     */
    }
/*     */
}