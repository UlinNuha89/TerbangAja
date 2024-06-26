package com.andc4.terbangaja.presentation.account.edit

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
        setClickListener()
        getProfile()
    }

    private fun setClickListener() {
        binding.btnSaveEditProfile.setOnClickListener {
            updateProfile()
        }
        binding.ivBackAkun.setOnClickListener {
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

    private fun updateProfile() {
        val id = viewModel.getUserID().orEmpty()
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val phoneNumber = binding.nomorHpEditText.text.toString()
        viewModel.updateProfile(id, name, email, phoneNumber, selectedImageFile).observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    showDialogCheckoutSuccess()
                },
                doOnSuccess = {
                    binding.profileContent.isVisible = true
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = false
                    Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                },
                doOnError = {
                    binding.profileContent.isVisible = false
                    binding.contentState.pbLoading.isVisible = false
                    binding.contentState.tvError.isVisible = true
                    Toast.makeText(this@EditProfileActivity, "Error updating profile", Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun showDialogCheckoutSuccess() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_profile_success, null)

        AlertDialog.Builder(this@EditProfileActivity)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.return_to_account)) { _, _ ->
                navAkun()
            }
            .create()
            .show()
    }

    private fun bindProfileData(profile: User) {
        profile.let {
            binding.nameEditText.setText(it.name)
            binding.emailEditText.setText(it.email)
            binding.nomorHpEditText.setText(it.phone)
            it.photo?.let { photo ->
                Glide.with(this)
                    .load(photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivProfileImage)
            }
        }
    }

    private fun navAkun() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateTo", "AccountFragment")
        startActivity(intent)
        finish()
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
}
