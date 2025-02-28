package com.imronreviady.simplestore.viewmodel.common;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import com.imronreviady.simplestore.repository.aboutus.AboutUsRepository;
import com.imronreviady.simplestore.ui.common.BackgroundTaskHandler;
import com.imronreviady.simplestore.ui.common.NotificationTaskHandler;
import com.imronreviady.simplestore.utils.Utils;

import javax.inject.Inject;

/**
 * Created by Panacea-Soft on 1/4/18.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class NotificationViewModel extends ViewModel {

    private final NotificationTaskHandler backgroundTaskHandler;

    public boolean pushNotificationSetting = false;
    public boolean isLoading = false;

    @Inject
    NotificationViewModel(AboutUsRepository repository) {
        Utils.psLog("Inside NewsViewModel");

        backgroundTaskHandler = new NotificationTaskHandler(repository);
    }

    public void registerNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.registerNotification(context, platform, token);
    }

    public void unregisterNotification(Context context, String platform, String token) {

        if(token == null || platform == null) return;

        if(platform.equals("")) return;

        backgroundTaskHandler.unregisterNotification(context, platform, token);
    }

    public LiveData<BackgroundTaskHandler.LoadingState> getLoadingStatus() {
        return backgroundTaskHandler.getLoadingState();
    }



}
