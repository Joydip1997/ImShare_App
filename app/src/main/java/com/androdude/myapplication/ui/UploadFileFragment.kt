package com.androdude.myapplication.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.androdude.myapplication.data.network.ImageUploadApi
import com.androdude.myapplication.data.ResponseData
import com.androdude.myapplication.data.network.UploadRequestBody
import com.androdude.myapplication.databinding.FragmentUploadFileBinding
import com.androdude.myapplication.utils.Constants.IMAGE_PICK_REQUEST
import com.androdude.myapplication.utils.Constants.TAG
import com.androdude.myapplication.utils.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class UploadFileFragment : Fragment(), UploadRequestBody.UploadCallback {
    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!



    private lateinit var selectedImageUri: Uri
    private lateinit var navigation: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUploadFileBinding.inflate(inflater, null, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation = Navigation.findNavController(binding.root)




        binding.bttnChooseImage.setOnClickListener {
            if (checkPermissions()) {
                openImageChooser()

            }
        }
        binding.bttnUpload.setOnClickListener {
            if (this::selectedImageUri.isInitialized) {
                uploadImage()
                binding.uploadProgressBar.visibility = View.VISIBLE
            }

        }
    }


    //Image Chooser
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, IMAGE_PICK_REQUEST)
        }

           binding.animationView.visibility = View.GONE
           binding.bttnUpload.visibility = View.VISIBLE

    }

    //Checking The Permissions
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                5000
            )

        }

        return false
    }


    //Uploading The Image
    private fun uploadImage() {
        if (selectedImageUri == null) {

            return
        }

        val parcelFileDescriptor =
            requireActivity().contentResolver.openFileDescriptor(selectedImageUri!!, "r", null)
                ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireActivity().cacheDir,
            requireActivity().contentResolver.getFileName(selectedImageUri!!)
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        val body = UploadRequestBody(
            file,
            "image",
            this
        )

        ImageUploadApi().upload(
            MultipartBody.Part.createFormData(
                "myfile",
                file.name,
                body
            ),
            "myfile"
        ).enqueue(object : Callback<ResponseData> {

            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                Log.i("TAG4", t.message!!)
                binding.uploadProgressBar.setProgress(0)
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {

                if (response.isSuccessful) {
                    val data: ResponseData = response.body()!!

                    shareFileFrag(data.file)

                }
                binding.uploadProgressBar.setProgress(100)
                response.body()?.let {


                }
            }
        })

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                5000 -> {
                    selectedImageUri = data?.data!!
                    binding.imageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.uploadProgressBar.setProgress(percentage)
    }

    //Go To Share File Fragment
    private fun shareFileFrag(file: String) {
        val action = UploadFileFragmentDirections.actionUploadFileFragmentToFileShareFragment(file)
        navigation.navigate(action)
    }


}