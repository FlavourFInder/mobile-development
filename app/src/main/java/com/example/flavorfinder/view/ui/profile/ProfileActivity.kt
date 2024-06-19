package com.example.flavorfinder.view.ui.profile

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.flavorfinder.R
import com.example.flavorfinder.databinding.ActivityProfileBinding
import com.example.flavorfinder.helper.Result
import com.example.flavorfinder.helper.ViewModelFactory
import com.example.flavorfinder.helper.uriToFile
import com.example.flavorfinder.pref.UserPreference
import com.example.flavorfinder.pref.dataStore
import com.example.flavorfinder.view.ui.signin.SigninActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileBinding

    private lateinit var userPreference: UserPreference

    private lateinit var btnConfirmLogout: Button

    private lateinit var btnSave: Button

    private lateinit var btnCancel: Button

    private var currentImageUri: Uri? = null

    private lateinit var imagePreview: CircleImageView

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.btnEditUsername.setOnClickListener {
            showEditUsernameDialog()
        }

        binding.btnChangeIvProfile.setOnClickListener {
            startGallery()

        }

        lifecycleScope.launch {
            viewModel.getUserData()
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.user.observe(this) { result ->
            when (result) {
                is Result.Succes -> {
                    val userData = result.data.data
                    binding.apply {
                        tvEmail.text = userData.email
                        tvUsername.text = userData.username
                        Glide.with(this@ProfileActivity)
                            .load(userData.imgUrl)
                            .placeholder(R.drawable.baseline_account_circle_24)
                            .into(ivProfile)
                    }
                }
                is Result.Error -> {
                    showToast(result.error)
                }
                is Result.Loading -> { }
            }
        }
    }

    private fun showEditUsernameDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_edit_username)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg))
        dialog.setCancelable(false)

        btnCancel = dialog.findViewById(R.id.btn_cancel)
        btnSave = dialog.findViewById(R.id.btn_save)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            val username = dialog.findViewById<EditText>(R.id.usernameEditText).text.toString()
            if (username.isEmpty()) {
                showToast("Username cannot be empty")
                return@setOnClickListener
            }
            lifecycleScope.launch {
                viewModel.updateUsername(username).observe(this@ProfileActivity) { result ->
                    when (result) {
                        is Result.Succes -> {
                            showToast("Username updated successfully")
                            dialog.dismiss()
                            viewModel.getUserData()
                        }
                        is Result.Error -> {
                            showToast(result.error)
                        }
                        is Result.Loading -> {
                            showToast("Updating...")
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun showLogoutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog_confirm)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg))
        dialog.setCancelable(false)

        btnCancel = dialog.findViewById(R.id.btn_cancel)
        btnConfirmLogout = dialog.findViewById(R.id.btn_confirm)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(uri: Uri) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_change_image_profile)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg))
        dialog.setCancelable(false)

        btnCancel = dialog.findViewById(R.id.btn_cancel)
        btnSave = dialog.findViewById(R.id.btn_save)
        imagePreview = dialog.findViewById(R.id.iv_profile)
        imagePreview.setImageURI(uri)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            currentImageUri?.let {
                Log.d("Image URI", "showImage: $it")
                val imageFile = uriToFile(it, this)
                imageFile?.let { file ->
                    lifecycleScope.launch {
                        viewModel.updateProfilePicture(file).observe(this@ProfileActivity) { result ->
                            when (result) {
                                is Result.Succes -> {
                                    showToast("Profile picture updated successfully")
                                    dialog.dismiss()
                                    viewModel.getUserData()
                                }
                                is Result.Error -> {
                                    showToast(result.error)
                                }
                                is Result.Loading -> {
                                    showToast("Updating...")
                                }
                            }
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}