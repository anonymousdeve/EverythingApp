package test.app.everything.features.main

import android.os.Bundle
import android.view.WindowManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import test.app.everything.core.base.BaseActivity
import test.app.everything.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<Any>() {
    override val mBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        setUpViews()
    }

    override fun setUpViews() {
        installSplashScreen()
        // prevent user from taking a screen shot or record the screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }


}