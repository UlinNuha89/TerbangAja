package com.andc4.terbangaja.presentation.account.edit

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.andc4.terbangaja.R
import com.andc4.terbangaja.data.model.User
import com.andc4.terbangaja.databinding.ActivityEditProfilBinding
import com.andc4.terbangaja.presentation.main.MainActivity
import com.andc4.terbangaja.utils.proceedWhen
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {
    private val binding: ActivityEditProfilBinding by lazy {
        ActivityEditProfilBinding.inflate(layoutInflater)
    }
    private val viewModel: EditProfileViewModel by viewModel()
    private var selectedImageFile: File? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val file = uriToFile(uri)
                selectedImageFile = file
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfileImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setHeader()
        setClickListener()
        getProfile()
    }

    private fun setHeader() {
        binding.layoutHeader.tvTitle.text = getString(R.string.ubah_profil)
    }

    private fun setClickListener() {
        binding.btnSaveEditProfile.setOnClickListener {
            doUpdateProfile()
        }
        binding.layoutHeader.ivBackHeader.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.ivProfileImage.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun getProfile() {
        viewModel.getProfile().observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.contentState.pbLoading.isVisible = true
                    binding.contentState.tvError.isVisible = false
                    binding.profileCardView.isVisible = true
                },
                doOnSuccess = {
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    it.payload?.let { data ->
                        bindProfileData(data)
                    }
                },
                doOnError = {
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                },
            )
        }
    }

    private fun isFormValid(): Boolean {
        val name = binding.nameEditText.text.toString()
        val phoneNumber = binding.nomorHpEditText.text.toString()
        return validateField(name, phoneNumber) && validatePhoneNumber(phoneNumber)
    }

    private fun doUpdateProfile() {
        if (isFormValid()) {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phoneNumber = binding.nomorHpEditText.text.toString().trim()
            updateProfile(name, email, phoneNumber)
        }
    }

    private fun updateProfile(
        name: String,
        email: String,
        phoneNumber: String,
    ) {
        val pd = ProgressDialog(this)
        viewModel.updateProfile(name, email, phoneNumber, selectedImageFile)
            .observe(this) { result ->
                result.proceedWhen(
                    doOnLoading = {
                        pd.show()
                        showDialogCheckoutSuccess()
                    },
                    doOnSuccess = {
                        pd.dismiss()
                        binding.profileContent.isVisible = true
                        binding.contentState.pbLoading.isVisible = false
                        binding.contentState.tvError.isVisible = false
                        Toast.makeText(
                            this@EditProfileActivity,
                            getString(R.string.profile_updated_successfully),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                    doOnError = {
                        pd.dismiss()
                        binding.profileContent.isVisible = false
                        binding.contentState.pbLoading.isVisible = false
                        binding.contentState.tvError.isVisible = true
                        Toast.makeText(
                            this@EditProfileActivity,
                            getString(R.string.error_updating_profile),
                            Toast.LENGTH_SHORT,
                        ).show()
                    },
                )
            }
    }

    private fun showDialogCheckoutSuccess() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile_success, null)
        AlertDialog.Builder(this@EditProfileActivity)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.return_to_main)) { _, _ ->
                navToMain()
            }
            .create()
            .show()
    }

    private fun bindProfileData(profile: User) {
        profile.let {
            binding.nameEditText.setText(it.name)
            binding.emailEditText.setText(it.email)
            binding.nomorHpEditText.setText(it.phone)
            it.photo.let { photo ->
                Glide.with(this)
                    .load(photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfileImage)
            }
        }
    }

    private fun navToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("profile_image", ".jpg", cacheDir)
        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input?.copyTo(output)
            }
        }
        return tempFile
    }

    private fun validateField(
        name: String,
        telp: String,
    ): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            binding.nameInputLayout.isErrorEnabled = true
            binding.nameInputLayout.error = getString(R.string.text_error_name_empty)
            isValid = false
        } else {
            binding.nameInputLayout.isErrorEnabled = false
        }
        if (telp.isEmpty()) {
            binding.nomorHpInputLayout.isErrorEnabled = true
            binding.nomorHpInputLayout.error = getString(R.string.text_error_telp_empty)
            isValid = false
        } else {
            binding.nomorHpInputLayout.isErrorEnabled = false
        }
        return isValid
    }

    private fun validatePhoneNumber(telp: String): Boolean {
        val errorMsg =
            when {
                telp.isEmpty() -> getString(R.string.nomor_telepon_tidak_boleh_kosong)
                telp.length < 11 -> getString(R.string.nomor_telepon_tidak_boleh_kurang_dari_11_digit)
                telp.length > 13 -> getString(R.string.nomor_telepon_tidak_boleh_lebih_dari_13_digit)
                else -> null
            }
        binding.nomorHpInputLayout.isErrorEnabled = errorMsg != null
        binding.nomorHpInputLayout.error = errorMsg
        return errorMsg == null
    }
}
