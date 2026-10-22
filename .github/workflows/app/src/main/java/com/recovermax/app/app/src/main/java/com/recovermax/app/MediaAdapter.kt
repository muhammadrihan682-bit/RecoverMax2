package com.recovermax.app

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recovermax.app.databinding.ItemMediaBinding
import java.io.File

class MediaAdapter(
    private val items: List<MediaFile>,
    private val onItemClick: (MediaFile) -> Unit
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val binding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MediaViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class MediaViewHolder(
        private val binding: ItemMediaBinding,
        private val onItemClick: (MediaFile) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaFile: MediaFile) {
            val file = File(mediaFile.path)
            val extension = mediaFile.path.substringAfterLast(".").lowercase()

            // Load thumbnail
            if (extension in listOf("jpg", "jpeg", "png", "gif", "webp", "bmp")) {
                try {
                    val bitmap = BitmapFactory.decodeFile(mediaFile.path)
                    if (bitmap != null) {
                        binding.thumbnailImage.setImageBitmap(bitmap)
                    } else {
                        binding.thumbnailImage.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                } catch (e: Exception) {
                    binding.thumbnailImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else if (extension in listOf("mp4", "mkv", "avi", "mov", "flv", "wmv", "3gp", "webm")) {
                binding.thumbnailImage.setImageResource(android.R.drawable.ic_menu_view)
            } else {
                binding.thumbnailImage.setImageResource(android.R.drawable.ic_menu_more)
            }

            // File info
            binding.fileName.text = mediaFile.name
            binding.fileInfo.text = "${mediaFile.type} • ${formatSize(mediaFile.size)}"

            // Click listener
            binding.root.setOnClickListener {
                onItemClick(mediaFile)
            }
        }

        private fun formatSize(bytes: Long): String {
            return when {
                bytes >= 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024 * 1024)}GB"
                bytes >= 1024 * 1024 -> "${bytes / (1024 * 1024)}MB"
                bytes >= 1024 -> "${bytes / 1024}KB"
                else -> "${bytes}B"
            }
        }
    }
}
