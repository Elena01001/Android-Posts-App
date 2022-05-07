package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel

const val THOUSAND = 1000
const val MILLION = THOUSAND * THOUSAND

class MainActivity : AppCompatActivity() {

    private val viewModel: PostViewModel by viewModels() // делегирование

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // повесили наблюдателя за отрисовкой поста, как только в посте изменятся данные,
        // то вызовется лямбда с ф-цией рендера, кот создана ниже
        viewModel.data.observe(this) { post -> binding.render(post) }

        // повесили слушателя за нажатием лайка, те нажали лайк, во вьюмодели вызовется метод
        // onLikeClicked, кот в свое время вызовет из репозитория метод like, like получит
        // текущий пост, лайкнет/дизлайкнет его и обновит данные в liveData
        // liveData - это живой поток, в кот только одни какие-то данные, самая актуальная последняя инфа.
        // Вызывая метод value - мы закидываем данные в поток, на кот кто-то где-то подписывается, напр наша viewModel
        // на поле data подписалась наша activity, поэтому данные обновились в liveData и вызвался перерендеринг
        binding.postLayout.likeButton.setOnClickListener {
            viewModel.onLikeButtonClicked()
        }

        binding.postLayout.shareButton.setOnClickListener {
            viewModel.onShareButtonClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        postLayout.authorName.text = post.author
        postLayout.content.text = post.content
        postLayout.published.text = post.published
        postLayout.likeButton.setImageResource(getLikeIconResId(post.likedByMe))
        postLayout.shareButton.setImageResource(R.drawable.ic_baseline_share_24)
        postLayout.likeNumbers.text = showNumberView(post.likes)
        postLayout.shareNumbers.text = showNumberView(post.shares)
        postLayout.seenNumbers.text = showNumberView(post.viewings)
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_baseline_favorite_border_24

    private fun showNumberView(currentNumber: Int): String {
        return when (currentNumber) {
            in 0..999 -> currentNumber.toString()
            in THOUSAND..9999 -> {
                var numberQuantity = String.format("%.1f", (currentNumber).toDouble() / THOUSAND)
                if (numberQuantity.endsWith(",0")) {
                    numberQuantity = numberQuantity.substring(
                        0,
                        numberQuantity.length - 2
                    )
                }
                numberQuantity + "K"
            }
            in 10 * THOUSAND..999999 -> {
                val numberQuantity = currentNumber / THOUSAND
                "$numberQuantity" + "K"
            }
            else -> {
                var numberQuantity = String.format("%.1f", (currentNumber).toDouble() / MILLION)
                if (numberQuantity.endsWith(",0")) {
                    numberQuantity = numberQuantity.substring(
                        0,
                        numberQuantity.length - 2
                    )
                }
                numberQuantity + "M"
            }
        }
    }
}
