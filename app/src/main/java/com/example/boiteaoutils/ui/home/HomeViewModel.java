package com.example.boiteaoutils.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Bienvenue dans la boite à outils. \n \n La boite à outils qui va changer votre vie !");
    }

    public LiveData<String> getText() {
        return mText;
    }
}