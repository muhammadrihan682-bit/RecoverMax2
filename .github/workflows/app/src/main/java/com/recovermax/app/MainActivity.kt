package com.recovermax.app

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.recovermax.app.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MediaAdapter
    private val mediaList = mutableListOf<MediaFile>()
    private var isScanning = false
    
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            startScan()
        } else {
            Toast.makeText(this, "Storage permission required!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        checkPermissions()
    }

    private fun setupUI() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = MediaAdapter(mediaList) { mediaFile ->
            recoverFile(mediaFile)
        }
        recyclerView.adapter = adapter

        binding.scanButton.setOnClickListener {
            if (!isScanning) {
                checkPermissions()
            }
        }

        binding.clearButton.setOnClickListener {
            mediaList.clear()
            adapter.notifyDataSetChanged()
            binding.statusText.text = "Ready to scan"
        }
    }

    private fun checkPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest)
        } else {
            startScan()
        }
    }

    private fun startScan() {
        if (isScanning) return
        
        isScanning = true
        mediaList.clear()
        adapter.notifyDataSetChanged()
        binding.statusText.text = "Scanning storage..."
        binding.scanButton.isEnabled = false
        binding.progressBar.progress = 0

        GlobalScope.launch(Dispatchers.Default) {
            scanStorage()
            withContext(Dispatchers.Main) {
                isScanning = false
                binding.scanButton.isEnabled = true
                binding.statusText.text = "Found ${mediaList.size} files"
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun scanStorage() {
        val paths = arrayOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            File(Environment.getExternalStorageDirectory(), "DCIM/Camera"),
            File(Environment.getExternalStorageDirectory(), "Pictures"),
            File(Environment.getExternalStorageDirectory(), "Android/media")
        )

        for (path in paths) {
            if (path != null && path.exists()) {
                scanDirectory(path)
            }
        }
    }

    private fun scanDirectory(directory: File) {
        try {
            val files = directory.listFiles() ?: return
            
            for (file in files) {
                if (file.isDirectory && mediaList.size < 500) {
                    scanDirectory(file)
                } else if (file.isFile && isMediaFile(file)) {
                    val mediaFile = MediaFile(
                        path = file.absolutePath,
                        name = file.name,
                        size = file.length(),
                        type = getFileType(file),
                        modified = file.lastModified()
                    )
                    mediaList.add(mediaFile)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isMediaFile(file: File): Boolean {
        val extension = file.extension.lowercase()
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "webp", "bmp")
        val videoExtensions = listOf("mp4", "mkv", "avi", "mov", "flv", "wmv", "3gp", "webm")
        
        return extension in imageExtensions || extension in videoExtensions
    }

    private fun getFileType(file: File): String {
        val extension = file.extension.lowercase()
        return when {
            extension in listOf("jpg", "jpeg", "png", "gif", "webp", "bmp") -> "Image"
            extension in listOf("mp4", "mkv", "avi", "mov", "flv", "wmv", "3gp", "webm") -> "Video"
            else -> "Unknown"
        }
    }

    private fun recoverFile(mediaFile: MediaFile) {
        val sourceFile = File(mediaFile.path)
        
        if (!sourceFile.exists()) {
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val recoverFolder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "RecoveredFiles"
            )
            
            if (!recoverFolder.exists()) {
                recoverFolder.mkdirs()
            }

            val destinationFile = File(recoverFolder, sourceFile.name)
            sourceFile.copyTo(destinationFile, overwrite = true)

            Toast.makeText(
                this,
                "Recovered: ${destinationFile.absolutePath}",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

data class MediaFile(
    val path: String,
    val name: String,
    val size: Long,
    val type: String,
    val modified: Long
)
