package com.example.kotlincoroutinessandbox

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.kotlincoroutinessandbox.databinding.FragmentFirstBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        private val TAG_EXAMPLE_7 = "TAG_EXAMPLE_7"
        private val TAG_EXAMPLE_8 = "TAG_EXAMPLE_8"
    }

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coroutineExample7()
        coroutineExample8()
    }

    private fun coroutineExample7() {
        /*
         * Урок 7. Корутины. Scope
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/601-urok-7-korutiny-scope.html
         */

        // 1. Создаем свой Scope
        val scope = CoroutineScope(Job())

        // 2. Запуск корутин
        scope.launch {
            /*
             * this:CoroutineScope - это не тот же самый scope, который мы использовали для
             * запуска этой родительской корутины. Каждая корутина создает внутри себя свой scope,
             * чтобы иметь возможность запускать дочерние корутины.
             *
             * В любой корутине есть свой CoroutineScope. В качестве обязательного Job, этот
             * scope использует Job корутины.
             */

            Log.d(TAG_EXAMPLE_7, "first coroutine")
        }

        scope.launch {
            Log.d(TAG_EXAMPLE_7, "second coroutine")
        }

        scope.launch {
            Log.d(TAG_EXAMPLE_7, "third coroutine")
        }

        // 3. Остановка всех корутин
        // scope.cancel()

        // 4. Проверяем соответствие Scope == Job
        val job = scope.launch {
            // a. Данный Scope == Job
            Log.d(TAG_EXAMPLE_7, "scope = $this")

            launch(start = CoroutineStart.LAZY) {
                // b. Данный Scope != Job, т.к. была запущена новая Job
                Log.d(TAG_EXAMPLE_7, "scope = $this")
            }
        }
        // a.1. Данный Job == Scope
        Log.d(TAG_EXAMPLE_7, "job = $job")

        // scope.launch { ... } - сразу запускает корутину
        // scope.launch(start = CoroutineStart.LAZY) { ... } - запуск отложенный, через Job.start()
    }

    private fun coroutineExample8() {
        /*
         * Урок 8. Корутины. Отмена
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/602-urok-8-korutiny-otmena.html
         */
        lateinit var jobForCancel: Job

        val formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
        val scope = CoroutineScope(Job())

        fun log(text: String) {
            Log.d(TAG_EXAMPLE_8, "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
        }

        fun onRun() {
            log("onRun, start")

            scope.launch {
                log("coroutine1, start")

                scope.launch {
                    log("coroutine2, start")
                    TimeUnit.MILLISECONDS.sleep(1000)
                    log("coroutine2, end")
                }

                TimeUnit.MILLISECONDS.sleep(1000)
                log("coroutine1, end")
            }

            log("onRun, middle")

            scope.launch {
                log("coroutine3, start")
                TimeUnit.MILLISECONDS.sleep(1500)
                log("coroutine3, end")
            }

            log("onRun, end")
        }

        fun onRunForCancel() {
            log("onRunForCancel, start")

            jobForCancel = scope.launch {
                log("coroutine, start")
                var x = 0
                while (x < 5 && isActive) {
                    TimeUnit.MILLISECONDS.sleep(1000)
                    log("coroutine, ${x++}, isActive = ${isActive}")

                    scope.launch {
                        log("INNER coroutine, start")
                        var x1 = 0
                        while (x1 < 5 && isActive) {
                            TimeUnit.MILLISECONDS.sleep(1000)
                            log("INNER coroutine, ${x1++}, isActive = ${isActive}")


                        }
                        log("INNER coroutine, end")
                    }
                }
                log("coroutine, end")
            }

            log("onRunForCancel, end")
        }

        fun onCancel() {
            log("onCancel")
            jobForCancel.cancel()
        }

        binding.example8RunBtn.setOnClickListener {
//            onRun()
            onRunForCancel()
        }

        binding.example8CancelBtn.setOnClickListener {
            onCancel()
        }

        binding.example8DestroyBtn.setOnClickListener {
            log("onDestroy")
            scope.cancel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}