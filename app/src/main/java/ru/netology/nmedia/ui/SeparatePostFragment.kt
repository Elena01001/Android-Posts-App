package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.SeparatePostViewBinding
import ru.netology.nmedia.viewModel.SeparatePostViewModel


class SeparatePostFragment : Fragment() {

    private val args by navArgs<SeparatePostFragmentArgs>()

    private val separatePostViewModel: SeparatePostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SeparatePostViewBinding.inflate(layoutInflater, container, false).also { binding ->
        separatePostViewModel.data.observe(viewLifecycleOwner) {
            separatePostViewModel.getPostById(args.postCardId)
        }

        //организация перехода к фрагменту postContentFragment
        separatePostViewModel.navigateToPostContentScreenEvent.observe(viewLifecycleOwner) { textToEdit ->
            val direction = SeparatePostFragmentDirections.actionSeparatePostFragmentToPostContentFragment(textToEdit)
            findNavController().navigate(direction)
        }
        separatePostViewModel.videoPlayEvent.observe(viewLifecycleOwner) { videoLink ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoLink))
            startActivity(intent)
        }
        separatePostViewModel.sharePostContent.observe(viewLifecycleOwner) {
                postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND  // создаем пустой интент и заполняем его через apply
                putExtra(Intent.EXTRA_TEXT, postContent) // кладем некоторые данные, кот будем share
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(
                    intent,
                    getString(R.string.description_post_share) // создаем меню выбора
                )
            startActivity(shareIntent)
        }

      //  separatePostViewModel.onRemoveButtonClicked().observe(viewLifecycleOwner)

        // показываем новый экран в нашем приложении
        // данная ф-ция будет вызвана при завершении PostContentActivity
        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            separatePostViewModel.onSaveButtonClicked(newPostContent)
        }
    }.root


}
