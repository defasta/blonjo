package com.apdef.mentari.views.fragments

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import com.apdef.mentari.R
import com.apdef.mentari.storage.SharedPref
import com.apdef.mentari.views.activities.auth.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class ProfileFragment : Fragment(), PermissionListener {
    val REQUEST_IMAGE_CAPTURE = 1
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    var statusAdd : Boolean = false
    private var filePath: Uri? = null
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var sharedPref : SharedPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPref = SharedPref(this!!.context!!)
        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        Glide.with(this)
            .load(sharedPref.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(img_profile)

        tv_profile_name.text = sharedPref.getValues("username")
        tv_profile_email.text = sharedPref.getValues("email")

        btn_to_edit.setOnClickListener {
            Dexter.withActivity(this!!.context!! as Activity?)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(this)
                .check()


        }
        btn_logout.setOnClickListener {
            sharedPref.setValues("status", "0")
            val i = Intent(context, LoginActivity::class.java)
            startActivity(i)
            activity?.finishAffinity()
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also{
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(context, "Gagal menambahkan foto profil", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            var bitmap = data?.extras?.get("data") as Bitmap
            statusAdd = true
            filePath = data.data!!
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(img_profile)
        }
        if (ContextCompat.checkSelfPermission(
                this!!.context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this.context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )

        } else {
            if(filePath != null){
                val progressDialog = ProgressDialog(context)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                val ref = storageReference.child("images/"+ UUID.randomUUID().toString())
                //grantUriPermission(this, filePath, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                ref.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
                        ref.downloadUrl.addOnSuccessListener {
                            sharedPref.setValues("url", it.toString())
                        }
                    }
                    .addOnFailureListener{e ->
                        progressDialog.dismiss()
                        Toast.makeText(context, "Failed" + e.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 + taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded "+progress.toInt()+"%")
                    }
            }
        }

    }
}
