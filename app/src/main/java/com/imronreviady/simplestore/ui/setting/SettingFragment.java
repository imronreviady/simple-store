package com.imronreviady.simplestore.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentStatus;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.MainActivity;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentSettingBinding;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.GetSizeTaskForGlide;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.user.UserViewModel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;


public class SettingFragment extends PSFragment {


    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    private AutoClearedValue<FragmentSettingBinding> binding;

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;
    private ConsentForm form;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSettingBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().notificationSettingTextView.setText(binding.get().notificationSettingTextView.getText().toString());
        binding.get().editProfileTextView.setText(binding.get().editProfileTextView.getText().toString());
        binding.get().logOutTextView.setText(binding.get().logOutTextView.getText().toString());
        binding.get().appInfoTextView.setText(binding.get().appInfoTextView.getText().toString());

        binding.get().notificationSettingTextView.setOnClickListener(view -> navigationController.navigateToNotificationSettingActivity(getActivity()));
        binding.get().changeLanguageImageView.setOnClickListener(view -> navigationController.navigateToNotificationSettingActivity(getActivity()));
        binding.get().editProfileTextView.setOnClickListener(view -> navigationController.navigateToEditProfileActivity(getActivity()));
        binding.get().editProfileImageView.setOnClickListener(view -> navigationController.navigateToEditProfileActivity(getActivity()));
        binding.get().appInfoTextView.setOnClickListener(view -> navigationController.navigateToAppInfoActivity(getActivity()));
        binding.get().appInfoImageView.setOnClickListener(view -> navigationController.navigateToAppInfoActivity(getActivity()));

        binding.get().logOutTextView.setOnClickListener(v -> {

            psDialogMsg.showConfirmDialog(getString(R.string.edit_setting__logout_question), getString(R.string.app__ok), getString(R.string.app__cancel));
            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(view -> {

                psDialogMsg.cancel();

                logout();

            });

            psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());
        });

        if (loginUserId.equals("")) {
            hideUIForLogout();
        }
        if (getContext() != null) {
            new GetSizeTaskForGlide(binding.get().cacheValueTextViewDesc).execute(new File(getContext().getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        }

        binding.get().clearCacheTextView.setOnClickListener(v -> {
            psDialogMsg.showConfirmDialog(getString(R.string.setting__clear_cache_confirm), getString(R.string.app__ok), getString(R.string.message__cancel_close));
            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(v12 -> {
                new ClearCacheAsync().execute();

                if (getActivity() != null) {
                    Glide.get(getActivity()).clearMemory();
                }

                psDialogMsg.cancel();
            });

            psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());
        });

        binding.get().gdprTextView.setOnClickListener(v -> collectConsent());

    }

    @Override
    protected void initViewModels() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {
        boolean consentStatusIsReady = pref.getBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false);
        if (consentStatusIsReady) {
            binding.get().gdprTextView.setVisibility(View.VISIBLE);
        } else {
            binding.get().gdprTextView.setVisibility(View.GONE);
        }

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    userViewModel.user = data.get(0).user;
                }
            }

        });
    }

    private void hideUIForLogout() {
        binding.get().editProfileTextView.setVisibility(View.GONE);
        binding.get().editProfileImageView.setVisibility(View.GONE);
        binding.get().view16.setVisibility(View.GONE);
        binding.get().logOutTextView.setVisibility(View.GONE);
    }

    private void logout() {
        userViewModel.deleteUserLogin(userViewModel.user).observe(this, status -> {
            if (status != null) {

                FacebookSdk.sdkInitialize(getContext());
                LoginManager.getInstance().logOut();

                hideUIForLogout();

                navigationController.navigateBackToProfileFragment(this.getActivity());

                if (getActivity() != null) {
                    if (!(getActivity() instanceof MainActivity)) {
                        getActivity().finish();
                    }
                }

            }
        });
    }

    class ClearCacheAsync extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (getActivity() != null) {
                Glide glide = Glide.get(getActivity().getApplicationContext());
                glide.clearDiskCache();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (getContext() != null) {
                new GetSizeTaskForGlide(binding.get().cacheValueTextViewDesc).execute(new File(getContext().getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
            }

            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
        }
    }

    private void collectConsent() {
        URL privacyUrl = null;
        try {
            // TODO: Replace with your app's privacy policy URL.
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this.getContext(), privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if (form != null) {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }
}
