package test.app.everything.core.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseActivity<E> : AppCompatActivity() {

    val mTag: String = javaClass.name
    abstract val mBinding: ViewBinding
    open val mViewModel: BaseViewModel<E>? = null

    protected abstract fun onCreateActivity(savedInstanceState: Bundle?)
    protected open fun renderEvent(event: E) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w(mTag, "onCreate:onCreate $mTag ")
        setContentView(mBinding.root)
        onCreateActivity(savedInstanceState)
        observeEvents()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mViewModel?.eventQueue?.getFor(mTag)?.collect { event ->
                    renderEvent(event)
                }
            }
        }
    }

    abstract fun setUpViews()


}