package com.arun.event.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arun.event.R
import com.itextpdf.text.Document
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileInputStream

class ReportActivity : AppCompatActivity() {

    val REQUEST_WRITE = 101;
    lateinit var btnReport: Button
    lateinit var pdfWriter: PdfWriter
    lateinit var document: Document
    val path: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        btnReport = findViewById(R.id.btnReport)
        btnReport.setOnClickListener {
            checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage", REQUEST_WRITE)
        }
    }

    private fun checkForPermission(permission: String, name: String, requestCode: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(applicationContext, permission)
                        == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(permission, name, requestCode)
                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $name is required to use this app")
            setTitle("Permission required")
            setPositiveButton("Ok", { dialog, which ->
                    ActivityCompat.requestPermissions(this@ReportActivity, arrayOf(permission), requestCode)
            })
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "$name permission granted", Toast.LENGTH_SHORT).show()
            }
        }

        when(requestCode    ) {
            REQUEST_WRITE -> innerCheck("Storage")
        }
    }

    private fun createPDF() {
        document = Document(PageSize.A4)
        document.addCreationDate()
        document.addAuthor("Arun")
        document.addCreator("Arun raj kumar")


     //   pdfWriter = PdfWriter.getInstance(document, FileInputStream(path))


    }

    private fun isExternalStorageWritable() = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())


    private fun saveFile() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                var path: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                var file: File = File(path, "new.pdf")
                save(file)
            }

        } else {

        }
    }

    private fun save(file: File) {
        TODO("Not yet implemented")
    }

}