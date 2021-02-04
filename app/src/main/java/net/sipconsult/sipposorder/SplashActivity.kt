package net.sipconsult.sipposorder

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import net.sipconsult.sipposorder.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        val anim = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.fade_in
        ) // Create the animation.

        anim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                // HomeActivity.class is the activity to go after showing the splash screen.
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        binding.imageSplash.startAnimation(anim)
    }
}