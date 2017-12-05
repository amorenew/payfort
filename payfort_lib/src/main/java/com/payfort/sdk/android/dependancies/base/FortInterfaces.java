package com.payfort.sdk.android.dependancies.base;

import java.util.Map;

public class FortInterfaces {
    public interface OnTnxProcessed {
        void onCancel(Map<String, String> paramMap1, Map<String, String> paramMap2);

        void onSuccess(Map<String, String> paramMap1, Map<String, String> paramMap2);

        void onFailure(Map<String, String> paramMap1, Map<String, String> paramMap2);
    }

    public interface InitializeCallback {
        void afterInitialized();
    }
}