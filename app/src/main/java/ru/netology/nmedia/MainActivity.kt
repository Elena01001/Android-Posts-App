package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

const val THOUSAND = 1000
const val MILLION = THOUSAND * THOUSAND

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            shared = false
        )

        binding.render(post)

        binding.postLayout.likeButton.setOnClickListener {
            post.likedByMe = !post.likedByMe
            binding.postLayout.likeButton.setImageResource(getLikeIconResId(post.likedByMe))
            if (post.likedByMe) post.likes++ else post.likes--
            binding.postLayout.likeNumbers.text = showNumberView(post.likes)
        }

        binding.postLayout.shareButton.setOnClickListener {
            binding.postLayout.shareButton.setImageResource(R.drawable.ic_baseline_share_24)
            binding.postLayout.shareNumbers.text = showNumberView(post.shares)
            if (post.shared) post.shares else post.shares++
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
                    numberQuantity = numberQuantity.substring(0,
                        numberQuantity.length -2)
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
                    numberQuantity = numberQuantity.substring(0,
                        numberQuantity.length -2)
                }
                numberQuantity + "M"
            }
        }
    }
}
