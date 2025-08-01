package com.example.bookinglapangan.ui.payment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinglapangan.R
import com.example.bookinglapangan.data.api.ApiResponse
import com.example.bookinglapangan.data.api.RetrofitClient
import com.example.bookinglapangan.data.api.ApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.NumberFormat

class ManualPaymentActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var uploadButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var tvTotal: TextView

    private val REQUEST_IMAGE = 101
    private var selectedImageUri: Uri? = null
    private var totalBayar = 0
    private var bookingId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_payment)

        // Ambil data intent
        bookingId = intent.getIntExtra("booking_id", 0)
        totalBayar = intent.getIntExtra("total_harga", 0)

        imgPreview = findViewById(R.id.imgPreview)
        uploadButton = findViewById(R.id.btnUploadBukti)
        backButton = findViewById(R.id.btnBack)
        tvTotal = findViewById(R.id.tvTotalBayar)

        tvTotal.text = "Total: Rp ${NumberFormat.getInstance().format(totalBayar)}"

        backButton.setOnClickListener {
            finish()
        }

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            imgPreview.setImageURI(selectedImageUri)
            imgPreview.visibility = View.VISIBLE

            uploadToServer(selectedImageUri)
        }
    }

    private fun uploadToServer(uri: Uri?) {
        uri?.let { selectedUri: Uri ->
            val filePath = getRealPathFromURI(selectedUri)
            if (filePath == null) {
                Toast.makeText(this, "Gagal mengambil file", Toast.LENGTH_SHORT).show()
                return
            }

            val file = File(filePath)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("bukti_bayar", file.name, requestFile)

            val api = RetrofitClient.apiService
            api.uploadBukti(bookingId, body).enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ManualPaymentActivity, "Bukti berhasil dikirim", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ManualPaymentActivity, "Upload gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Toast.makeText(this@ManualPaymentActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    // Untuk mendapatkan path asli dari URI file
    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }
}
