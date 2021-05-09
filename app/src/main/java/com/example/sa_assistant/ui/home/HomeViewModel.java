package com.example.sa_assistant.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("\n Список изменений: \n"
                        + "v1.5\n"
                        + "Переработан пункт меню \"Список магазинов\" :\n"
                        + "1. Оставлена только кнопка для добавления магазина.\n"
                        + "2. Добавлено контекстное меню при длителном удержании на магазине.\n"
                        + "3. Добавлена возможность прикрепить номер резерва к магазину.\n"
                        + "4. Добавлена возможность изменять уже готовые магазины.\n\n"
                        + "При формировании отчета со временем, теперь время учитываеться для каждого конкретного магазина.\n\n"
                        + "Добавлена возможность импортировать и экспортировать список магазинов.\n\n"
                        + "Незначительные изменения и исправления багов.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}