package com.example.matchcolor

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.matchcolor.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class MainActivity : AppCompatActivity() {
    private var animator: ObjectAnimator? = null
    private var total = 0
    private var flag2 = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clmain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnStart.setOnClickListener {
            binding.easyBlocker.visibility = View.VISIBLE
            btnAndWindowsAnimationZero(binding.btnStart,binding.clstartwindow)
            alphaOneForAll(binding.clEasyCircles)
            pbAndScoresAnimateAlphaOne()
            restartPbAnimation()
            val easyCirle = clEasyRandomColors()
            easyCirle.setOnClickListener {
                total += 1
                binding.tvScore.setText("score: " + total.toString())
                alphaZeroForAll(binding.clEasyCircles)
                alphaOneForAll(binding.clMediumCircles)
                var meduimCirle = clMeduimRandomColors()
                meduimCirle.setOnClickListener {
                    total += 1
                    binding.tvScore.setText("score: " + total.toString())
                    alphaZeroForAll(binding.clMediumCircles)
                    alphaOneForAll(binding.clHardCircles)
                    lifecycleScope.launch {
                        while (flag2 == false) {
                            var a = clHardRandomColors()
                            waitForClick(a)
                            total += 1
                            binding.tvScore.setText("score: " + total.toString())
                            if (total == 20) {
                                flag2 = true
                                wellDoneWindow()
                            }
                        }
                    }
                }
            }
        }
        binding.btnTryAgain.setOnClickListener {
            btnAndWindowsAnimationZero(binding.btnTryAgain,binding.clGameOVerWindow)
            pbAndScoresAnimateAlphaOne()
            binding.pb.progress = 0
            restartPbAnimation()
            alphaOneForAll(binding.clEasyCircles)
            total = 0
            binding.tvScore.setText("SCORE: " + total.toString())
          }
      }
    private fun wellDoneWindow(){
        alphaZeroForAll(binding.clHardCircles)
        pbAndScroesAnimateAlphaZero()
        binding.clRestartWindow.visibility = View.VISIBLE
        binding.clRestartWindow.alpha = 0f
        binding.clRestartWindow.scaleX = 0.85f
        binding.clRestartWindow.scaleY = 0.85f
        binding.btnRestart.isClickable = false
        binding.clRestartWindow.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(800).withEndAction {
                binding.btnRestart.isClickable = true
            }
        binding.btnRestart.setOnClickListener {
            flag2 = false
            btnAndWindowsAnimationZero(binding.btnRestart,binding.clRestartWindow)
            pbAndScoresAnimateAlphaOne()
            binding.pb.progress = 0
            restartPbAnimation()
            alphaOneForAll(binding.clEasyCircles)
            total = 0
            binding.tvScore.setText("SCORE: " + total.toString())
        }
    }
    private fun clEasyRandomColors(): CardView {
        with(binding) {
            var (TrueColor,imposterColor) = randomColor()
            var mainBinding = listOf(cvEasyFirst,cvEasySecond,cvEasyThird).random()
            var listOfCircles = listOf(cvEasyFirst,cvEasySecond,cvEasyThird)
            for (i in listOfCircles) {
                i.setOnClickListener {
                    Toast.makeText(this@MainActivity, "wrong circle", Toast.LENGTH_SHORT).show()
                    gameOver()
                }
                i.setCardBackgroundColor(TrueColor)
            }
            mainBinding.setCardBackgroundColor(imposterColor)
            return mainBinding
        }
    }
    suspend fun  waitForClick(correctCircle: CardView) = suspendCancellableCoroutine<Unit> { continuation ->
        val listener = View.OnClickListener {
            if (continuation.isActive) {
                alphaOneForAll(binding.clHardCircles)
                correctCircle.setOnClickListener(null) //
                continuation.resumeWith(Result.success(Unit)) //
            }
        }

        correctCircle.setOnClickListener(listener)

        continuation.invokeOnCancellation {
            correctCircle.setOnClickListener(null) //
        }
    }
    private fun gameOver() {
        alphaZeroForAll(binding.clEasyCircles)
        alphaZeroForAll(binding.clMediumCircles)
        alphaZeroForAll(binding.clHardCircles)
        pbAndScroesAnimateAlphaZero()
        binding.clGameOVerWindow.visibility = View.VISIBLE
        binding.clGameOVerWindow.alpha = 0f
        binding.clGameOVerWindow.scaleX = 0.85f
        binding.clGameOVerWindow.scaleY = 0.85f
        binding.btnTryAgain.isClickable = false
        binding.clGameOVerWindow.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(800).withEndAction {
                binding.btnTryAgain.isClickable = true
            }
    }
    private fun clMeduimRandomColors(): CardView {
        with(binding) {
            val (trueColor, imposterColor) = randomColor()
            var mainBindingImposter = listOf(cvMediumFirst,cvMediumSecond,cvMeduimThird,cvMediumfourth,cvMediumfifth,cvMediumSixth).random()
            var DefaultCOlorsCircles = listOf(cvMediumFirst,cvMediumSecond,cvMeduimThird,cvMediumfourth,cvMediumfifth,cvMediumSixth)
            for (i in DefaultCOlorsCircles) {
                i.setCardBackgroundColor(trueColor)
                i.setOnClickListener {
                    gameOver()
                    Toast.makeText(this@MainActivity, "wrong circle", Toast.LENGTH_SHORT).show()
                }
            }
            mainBindingImposter.setCardBackgroundColor(imposterColor)
            return mainBindingImposter
        }

    }
    private fun clHardRandomColors(): CardView {
        with(binding) {
            var (trueColor, imposterColor) = randomColor()
            var mainBindingImposter = listOf(cvHardFirst,cvHardSecond,cvHardThird
                ,cvHardFourth,cvHardfifth,cvHardsixth,cvHardSeventh,cvHardEight,cvHardNinth,cvHardTenth
                ,cvHardEleventh,cvHardTwelfth
                ,cvHardThirtennth,cvHardFourteenth,cvHardFifteenth,cvHardSixteenth).random()
            var defaulColorCirles = listOf(cvHardFirst,cvHardSecond,cvHardThird
                ,cvHardFourth,cvHardfifth,cvHardsixth,cvHardSeventh,cvHardEight,cvHardNinth,cvHardTenth
                ,cvHardEleventh,cvHardTwelfth
                ,cvHardThirtennth,cvHardFourteenth,cvHardFifteenth,cvHardSixteenth)
            for (i in defaulColorCirles) {

                i.setCardBackgroundColor(trueColor)
                i.setOnClickListener {
                    i.isClickable = false
                    gameOver()
                    Toast.makeText(this@MainActivity, "wrong circle", Toast.LENGTH_SHORT).show()
                }
            }
            mainBindingImposter.setCardBackgroundColor(imposterColor)
            return mainBindingImposter
        }
    }
    private fun randomColor(): Pair<Int,Int> {
        var mainColorlist = listOf("ImposterLime","ImposterSteelBlue","ImposterGold","ImposterPink","ImposterTeal","ImposterDarkRed","ImposterNeonGreen","ImposterDeepPurple","ImposterOrange","ImposterMagenta","ImposterCyan","ImposterYellow","Easygrad","whitlyBlue","Easygrennly","MeduimPurple","MeduimdarkGreenly","HardBlue","hardRedly").random()
        var imposterColorFinding = mainColorlist + "2"
        var mainColorId = resources.getIdentifier(mainColorlist,"color",packageName)
        var imposterColorId = resources.getIdentifier(imposterColorFinding,"color",packageName)
        var mainFirstColor = ContextCompat.getColor(this,mainColorId)
        var imposterColor = ContextCompat.getColor(this,imposterColorId)
        return Pair(mainFirstColor,imposterColor)
    }
    private fun alphaOneForAll(
        clCiclres: ConstraintLayout,
    ){
        binding.easyBlocker.visibility = View.VISIBLE
        with(binding) {
            clCiclres.alpha = 0f
            clCiclres.visibility = View.VISIBLE
            clCiclres.scaleX = 0.95f
            clCiclres.scaleY = 0.95f
            clCiclres.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(1f)
                .setDuration(800)
                .withEndAction {
                    clCiclres.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(500)
                }
        }
    }
    private fun alphaZeroForAll(
        clCiclres: ConstraintLayout,
    ) {
        with(binding) {
            clCiclres.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .alpha(0f)
                .setDuration(50)
                .withEndAction {
                    clCiclres.visibility = View.GONE
                }
        }
    }
    private fun pbAndScoresAnimateAlphaOne() {
        with(binding) {
            var list = listOf(pb, tvScore)
            for (i in list) {
                i.alpha = 0f
                i.visibility = View.VISIBLE
                i.scaleX = 0.95f
                i.scaleY = 0.95f
                i.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .alpha(1f)
                    .setDuration(800)
                    .withEndAction {
                        i.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(500)
                    }
            }

        }
        binding.cvEasyThird.isClickable = true
    }
    private fun pbAndScroesAnimateAlphaZero() {
        animator?.cancel()
        with(binding) {
            var list = listOf(pb,tvScore)
            for (i in list) {
                i.isClickable = false
                i.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .alpha(0f)
                    .setDuration(700)
                    .withEndAction {
                        i.visibility = View.GONE
                    }
            }
        }
    }
    private fun restartPbAnimation() {
        animator = ObjectAnimator.ofInt(binding.pb,"progress",100).apply {
            duration = 60000
            interpolator = LinearInterpolator()
            start()
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                Log.d("progress value",progress.toString())
                if(progress >= 99 && animation.isRunning) {
                    gameOver()
                    animation.cancel()
                Toast.makeText(this@MainActivity, "time is out", Toast.LENGTH_SHORT).show()
            }
          }
        }
    }
    private fun btnAndWindowsAnimationZero(
        btn: Button,
        finishWindow: ConstraintLayout,
    ) {
        with(binding) {
            btn.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction{
                    btn.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                }
            btn.isClickable = false
            finishWindow.animate()
                .scaleX(0.85f)
                .scaleY(0.85f)
                .alpha(0f)
                .setDuration(700)
                .withEndAction {
                    finishWindow.visibility = View.GONE
                    btn.isClickable = true
                }
        }
    }
}
