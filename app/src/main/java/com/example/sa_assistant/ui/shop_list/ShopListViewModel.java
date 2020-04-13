package com.example.sa_assistant.ui.shop_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShopListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShopListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is shop list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}