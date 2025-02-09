package com.imronreviady.simplestore.ui.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentProfileEditBinding;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.user.UserViewModel;
import com.imronreviady.simplestore.viewobject.User;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

/**
 * ProfileEditFragment
 */
public class ProfileEditFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileEditBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentProfileEditBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
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

        userViewModel.getLoginUser().observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.size() > 0) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                binding.get().setUser(listResource.get(0).user);
                userViewModel.user = listResource.get(0).user;
                Utils.psLog("Photo : " + listResource.get(0).user.userProfilePhoto);
            } else {
                //noinspection ConstantConditions
                Utils.psLog("Empty Data");

            }
        });

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (getContext() != null) {
            binding.get().userNameEditText.setHint(R.string.edit_profile__user_name);
            binding.get().userEmailEditText.setHint(R.string.edit_profile__email);
            binding.get().userPhoneEditText.setHint(R.string.edit_profile__phone);
            binding.get().userAboutMeEditText.setHint(R.string.edit_profile__about_me);
            binding.get().countryEditText.setHint(R.string.edit_profile__country);

            binding.get().nameTextView.setText(getContext().getString(R.string.edit_profile__user_name));
            binding.get().emailTextView.setText(getContext().getString(R.string.edit_profile__email));
            binding.get().phoneTextView.setText(getContext().getString(R.string.edit_profile__phone));
            binding.get().aboutMeTextView.setText(getContext().getString(R.string.edit_profile__about_me));

        }

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().profileImageView.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                try {

                    if (Utils.isStoragePermissionGranted(getActivity())) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in Image Gallery.", e);
                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();
            }

        });

        binding.get().saveButton.setOnClickListener(view -> ProfileEditFragment.this.editProfileData());

        binding.get().passwordChangeButton.setOnClickListener(view -> navigationController.navigateToPasswordChangeActivity(getActivity()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Constants.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (getActivity() != null && selectedImage != null) {
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        userViewModel.profileImagePath = cursor.getString(columnIndex);
                        cursor.close();

                        uploadImage();
                    }

                }

            }

        } catch (Exception e) {
            Utils.psErrorLog("Error in load image.", e);
        }
    }


    //endregion


    //region Private Methods


    private void editProfileData() {

        if (!connectivity.isConnected()) {

            psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String userName = binding.get().userNameEditText.getText().toString();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String userEmail = binding.get().userEmailEditText.getText().toString();
        if (userEmail.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        if(!checkToUpdateProfile())
        {
            updateUserProfile();
        }

        userViewModel.getUpdateUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
//                            if(listResource.data != null){
//                                fadeIn(binding.get().getRoot());
//                            }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

//                                userViewModel.updateUser(userViewModel.user);

                            psDialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.app__ok));
                            psDialogMsg.show();
                            psDialogMsg.okButton.setOnClickListener(view -> psDialogMsg.cancel());


                        }
                        userViewModel.setLoadingState(false);
                        prgDialog.get().cancel();

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();
                        prgDialog.get().cancel();

                        userViewModel.setLoadingState(false);
                        break;
                    default:


                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");
            }


        });
    }


    private void uploadImage() {

        prgDialog.get().show();
        Utils.psLog("Uploading Image.");

        userViewModel.uploadImage(userViewModel.profileImagePath, loginUserId).observe(this, listResource -> {
            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString());


                if (listResource.message != null && !listResource.message.equals("")) {
                    prgDialog.get().cancel();
                } else {
                    // Update the data
                    prgDialog.get().cancel();
                }

            } else if (listResource != null && listResource.message != null) {
                Utils.psLog("Message from server.");

                psDialogMsg.showInfoDialog(listResource.message, getString(R.string.app__ok));
                psDialogMsg.show();

                prgDialog.get().cancel();
            } else {
                //noinspection ConstantConditions
                Utils.psLog("Empty Data");

            }

        });
    }

    private boolean checkToUpdateProfile() {

        return binding.get().userNameEditText.getText().toString().equals(userViewModel.user.userName) &&
                binding.get().userEmailEditText.getText().toString().equals(userViewModel.user.userEmail) &&
                binding.get().userPhoneEditText.getText().toString().equals(userViewModel.user.userPhone) &&
                binding.get().userAboutMeEditText.getText().toString().equals(userViewModel.user.userAboutMe) &&
                binding.get().firstNameEditText.getText().toString().equals(userViewModel.user.shippingFirstName) &&
                binding.get().lastNameEditText.getText().toString().equals(userViewModel.user.shippingLastName) &&
                binding.get().companyEditText.getText().toString().equals(userViewModel.user.shippingCompany) &&
                binding.get().address1EditText.getText().toString().equals(userViewModel.user.shippingAddress1) &&
                binding.get().address2EditText.getText().toString().equals(userViewModel.user.shippingAddress2) &&
                binding.get().countryEditText.getText().toString().equals(userViewModel.user.shippingCountry) &&
                binding.get().stateEditText.getText().toString().equals(userViewModel.user.shippingState) &&
                binding.get().cityEditText.getText().toString().equals(userViewModel.user.shippingCity) &&
                binding.get().postalEditText.getText().toString().equals(userViewModel.user.shippingPostalCode) &&
                binding.get().emailEditText.getText().toString().equals(userViewModel.user.shippingEmail) &&
                binding.get().phoneEditText.getText().toString().equals(userViewModel.user.shippingPhone) &&
                binding.get().card2FirstNameEditText.getText().toString().equals(userViewModel.user.billingFirstName) &&
                binding.get().card2LastNameEditText.getText().toString().equals(userViewModel.user.billingLastName) &&
                binding.get().card2CompanyEditText.getText().toString().equals(userViewModel.user.billingCompany) &&
                binding.get().card2Address1EditText.getText().toString().equals(userViewModel.user.billingAddress1) &&
                binding.get().card2Address2EditText.getText().toString().equals(userViewModel.user.billingAddress2) &&
                binding.get().card2CountryEditText.getText().toString().equals(userViewModel.user.billingCountry) &&
                binding.get().card2StateEditText.getText().toString().equals(userViewModel.user.billingState) &&
                binding.get().card2CityEditText.getText().toString().equals(userViewModel.user.billingCity) &&
                binding.get().card2PostalEditText.getText().toString().equals(userViewModel.user.billingPostalCode) &&
                binding.get().card2EmailEditText.getText().toString().equals(userViewModel.user.billingEmail) &&
                binding.get().card2PhoneEditText.getText().toString().equals(userViewModel.user.billingPhone);
    }

    private void updateUserProfile() {
        User user = new User(userViewModel.user.userId,
                userViewModel.user.userIsSysAdmin,
                userViewModel.user.isShopAdmin,
                userViewModel.user.facebookId,
                userViewModel.user.googleId,
                binding.get().userNameEditText.getText().toString(),
                binding.get().userEmailEditText.getText().toString(),
                binding.get().userPhoneEditText.getText().toString(),
                userViewModel.user.userPassword,
                binding.get().userAboutMeEditText.getText().toString(),
                userViewModel.user.userCoverPhoto,
                userViewModel.user.userProfilePhoto,
                userViewModel.user.roleId,
                userViewModel.user.status,
                userViewModel.user.isBanned,
                userViewModel.user.addedDate,
                binding.get().card2FirstNameEditText.getText().toString(),
                binding.get().card2LastNameEditText.getText().toString(),
                binding.get().card2CompanyEditText.getText().toString(),
                binding.get().card2Address1EditText.getText().toString(),
                binding.get().card2Address2EditText.getText().toString(),
                binding.get().card2CountryEditText.getText().toString(),
                binding.get().card2StateEditText.getText().toString(),
                binding.get().card2CityEditText.getText().toString(),
                binding.get().card2PostalEditText.getText().toString(),
                binding.get().card2EmailEditText.getText().toString(),
                binding.get().card2PhoneEditText.getText().toString(),
                binding.get().firstNameEditText.getText().toString(),
                binding.get().lastNameEditText.getText().toString(),
                binding.get().companyEditText.getText().toString(),
                binding.get().address1EditText.getText().toString(),
                binding.get().address2EditText.getText().toString(),
                binding.get().countryEditText.getText().toString(),
                binding.get().stateEditText.getText().toString(),
                binding.get().cityEditText.getText().toString(),
                binding.get().postalEditText.getText().toString(),
                binding.get().emailEditText.getText().toString(),
                binding.get().phoneEditText.getText().toString());

        userViewModel.setUpdateUserObj(user);

        prgDialog.get().show();
    }


    //endregion
}
