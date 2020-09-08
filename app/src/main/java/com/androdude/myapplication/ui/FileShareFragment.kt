package com.androdude.myapplication.ui

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androdude.myapplication.R
import com.androdude.myapplication.databinding.FragmentFileShareBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.about_me_sheet.*
import kotlinx.android.synthetic.main.fragment_file_share.*


class FileShareFragment : Fragment() {
    private var _binding: FragmentFileShareBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private val args: FileShareFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFileShareBinding.inflate(inflater, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fileUrl.text = args.message

        bottomSheetBehavior = BottomSheetBehavior.from(about_me_sheet)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

        })

        binding.copyUrl.setOnClickListener{
            if(!binding.fileUrl.text.isEmpty())
            {
                copyToClipboard(binding.fileUrl.text.toString())
            }
        }

        binding.shareFile.setOnClickListener {
            if(!binding.fileUrl.text.isEmpty())
            {
                shareUrl(binding.fileUrl.text.toString())
            }

        }

        binding.downloadFile.setOnClickListener {
            if(!binding.fileUrl.text.isEmpty())
            {
                download(binding.fileUrl.text.toString())
            }
        }

        google_play.setOnClickListener {
            redirectUrl(resources.getString(R.string.googlePlayStore))
        }
        github.setOnClickListener {
            redirectUrl(resources.getString(R.string.githubUrl))
        }
        linkdean.setOnClickListener {
            redirectUrl(resources.getString(R.string.linkdean))
        }
        website.setOnClickListener {
            redirectUrl(resources.getString(R.string.website))
        }


    }

    private fun copyToClipboard(link : String)
    {
        val clipManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("FILE_URL", link)
        clipManager.setPrimaryClip(clip)
    }

    private fun shareUrl(link : String)
    {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody = link
        intent.putExtra(Intent.EXTRA_SUBJECT,"FILE_URL")
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(intent, "FILE_URL"))
    }

    private fun download(link : String)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    }

    private fun redirectUrl(updateUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }





}