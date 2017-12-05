package com.payfort.sdk.android.dependancies.commons;

public class Constants {
    public class INDICATORS {
        public static final String CARD_MASKED_STAR = "*";
        public static final String REMEMBER_ME_ON = "on";
        public static final String REMEMBER_ME_OFF = "off";

        public INDICATORS() {
        }
    }

    public class LOCAL_BROADCAST_EVENTS {
        public static final String RESPONSE_EVENT = "responseEvent";

        public LOCAL_BROADCAST_EVENTS() {
        }
    }

    public class FORT_URI {
        public static final String WV_CHECKER_3DS_PARAMS_URL = "/FortAPI/sdk/process3DsMobile";
        public static final String VALIDATE_URL = "/FortAPI/sdk/validate";
        public static final String PROCESS_TNX_URL = "/FortAPI/sdk/processSdkTnx";
        public static final String AUTH_FAILED_URL = "/FortAPI/sdk/authenticationFailed";

        public FORT_URI() {
        }
    }

    public class CREDIT_CARDS_TYPES {
        public static final String VISA = "VISA";
        public static final String MASTERCARD = "MASTERCARD";
        public static final String AMEX = "AMEX";

        public CREDIT_CARDS_TYPES() {
        }
    }

    public class FORT_CODE {
        public static final String TECHNICAL_PROBLEM = "006";
        public static final String INIT_CONNECTION_FAILED = "071";
        public static final String CANCELED_BY_USER = "072";

        public FORT_CODE() {
        }
    }

    public class FORT_STATUS {
        public static final String INVALID_REQUEST = "00";

        public FORT_STATUS() {
        }
    }

    public class FORT_PARAMS {
        public static final String PAYMENT_OPTION = "payment_option";
        public static final String LANGUAGE = "language";
        public static final String STATUS = "status";
        public static final String RESPONSE_MSG = "response_message";
        public static final String RESPONSE_CODE = "response_code";
        public static final String ORDER_DESCRIPTION = "order_description";
        public static final String AMOUNT = "amount";
        public static final String CURRENCY = "currency";
        public static final String CARD_SECURITY_CODE = "card_security_code";
        public static final String CARD_NUMBER = "card_number";
        public static final String CUSTOMER_NAME = "customer_name";
        public static final String EXPIRY_DATE = "expiry_date";
        public static final String SDK_TOKEN = "sdk_token";
        public static final String FORT_ID = "fort_id";
        public static final String REMEMBER_ME = "remember_me";
        public static final String MERCHSNT_REFERENCE = "merchant_reference";
        public static final String DEVICE_FINGERPRINT = "device_fingerprint";
        public static final String CART_DETAILS = "cart_details";

        public FORT_PARAMS() {
        }
    }

    public class LANGUAGES {
        public static final String ARABIC = "ar";
        public static final String ENGLISH = "en";

        public LANGUAGES() {
        }
    }

    public class EXTRAS {
        public static final String SDK_MERCHANT_REQUEST = "merchantReq";
        public static final String SDK_RESPONSE = "sdkResp";
        public static final String SDK_CURRENCY_DP = "currencyDecimalPoints";
        public static final String SDK_MERCHANT_TOKEN = "merchantToken";
        public static final String SDK_ENVIRONMENT = "environment";
        public static final String SDK_SHOW_LOADING = "showLoading";
        public static final String SDK_DEFAULT_LOCALE = "defaultLocale";

        public EXTRAS() {
        }
    }
}