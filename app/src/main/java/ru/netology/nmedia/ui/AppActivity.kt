package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.AppActivityBinding

class AppActivity :
    AppCompatActivity() {  // AppCompatActivity в свою очередь наследуется от FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = AppActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //чтобы не наслаивался один фрагмент на другой в активити,
        // если у нас уже есть определенная транзакция фрагмента на экране, то мы новую не вызываем
        if (supportFragmentManager.findFragmentByTag(FeedFragment.TAG) == null) {
            // чтобы достучаться до FragmentManager (реализует методы CRUD длā работы с фрагментами)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, FeedFragment(), FeedFragment.TAG)
                .commit()
        }
    }
}