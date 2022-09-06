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
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.selects.SelectInstance

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    companion object {
        private const val TAG_EXAMPLE_7 = "TAG_EXAMPLE_7"
        private const val TAG_EXAMPLE_8 = "TAG_EXAMPLE_8"
        private const val TAG_EXAMPLE_9 = "TAG_EXAMPLE_9"
        private const val TAG_EXAMPLE_10 = "TAG_EXAMPLE_10"
        private const val TAG_EXAMPLE_11 = "TAG_EXAMPLE_11"
        private const val TAG_EXAMPLE_12 = "TAG_EXAMPLE_12"
        private const val TAG_EXAMPLE_13 = "TAG_EXAMPLE_13"
        private const val TAG_EXAMPLE_14 = "TAG_EXAMPLE_14"
        private const val TAG_EXAMPLE_15 = "TAG_EXAMPLE_15"
        private const val TAG_EXAMPLE_16 = "TAG_EXAMPLE_16"
        private const val TAG_EXAMPLE_17 = "TAG_EXAMPLE_17"
        private const val TAG_EXAMPLE_18 = "TAG_EXAMPLE_18"
        private const val TAG_EXAMPLE_19 = "TAG_EXAMPLE_19"
        private const val TAG_EXAMPLE_20 = "TAG_EXAMPLE_20"
        private const val TAG_EXAMPLE_21 = "TAG_EXAMPLE_21"
        private const val TAG_EXAMPLE_22 = "TAG_EXAMPLE_22"
        private const val TAG_EXAMPLE_23 = "TAG_EXAMPLE_23"
        private const val TAG_EXAMPLE_24 = "TAG_EXAMPLE_24"
        private const val TAG_EXAMPLE_25 = "TAG_EXAMPLE_25"
        private const val TAG_EXAMPLE_26 = "TAG_EXAMPLE_26"
        private const val TAG_EXAMPLE_27 = "TAG_EXAMPLE_27"
        private const val TAG_EXAMPLE_28 = "TAG_EXAMPLE_28"
        private const val TAG_EXAMPLE_29 = "TAG_EXAMPLE_29"
        private const val TAG_EXAMPLE_30 = "TAG_EXAMPLE_30"
    }

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val formatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

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
        coroutineExample9()
        coroutineExample10()
        coroutineExample11()
        coroutineExample12()
        coroutineExample13()
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

        val scope = CoroutineScope(Job())

        fun onRun() {
            log(TAG_EXAMPLE_8, "onRun, start")

            scope.launch {
                log(TAG_EXAMPLE_8, "coroutine1, start")

                scope.launch {
                    log(TAG_EXAMPLE_8, "coroutine2, start")
                    TimeUnit.MILLISECONDS.sleep(1000)
                    log(TAG_EXAMPLE_8, "coroutine2, end")
                }

                TimeUnit.MILLISECONDS.sleep(1000)
                log(TAG_EXAMPLE_8, "coroutine1, end")
            }

            log(TAG_EXAMPLE_8, "onRun, middle")

            scope.launch {
                log(TAG_EXAMPLE_8, "coroutine3, start")
                TimeUnit.MILLISECONDS.sleep(1500)
                log(TAG_EXAMPLE_8, "coroutine3, end")
            }

            log(TAG_EXAMPLE_8, "onRun, end")
        }

        fun onRunForCancel() {
            log(TAG_EXAMPLE_8, "onRunForCancel, start")

            jobForCancel = scope.launch {
                log(TAG_EXAMPLE_8, "coroutine, start")
                var x = 0
                while (x < 5 && isActive) {
                    TimeUnit.MILLISECONDS.sleep(1000)
                    log(TAG_EXAMPLE_8, "coroutine, ${x++}, isActive = ${isActive}")

                    scope.launch {
                        log(TAG_EXAMPLE_8, "INNER coroutine, start")
                        var x1 = 0
                        while (x1 < 5 && isActive) {
                            TimeUnit.MILLISECONDS.sleep(1000)
                            log(TAG_EXAMPLE_8, "INNER coroutine, ${x1++}, isActive = ${isActive}")
                        }
                        log(TAG_EXAMPLE_8, "INNER coroutine, end")
                    }
                }
                log(TAG_EXAMPLE_8, "coroutine, end")
            }

            log(TAG_EXAMPLE_8, "onRunForCancel, end")
        }

        fun onCancel() {
            log(TAG_EXAMPLE_8, "onCancel")
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
            log(TAG_EXAMPLE_8, "onDestroy")
            scope.cancel()
        }
    }

    private fun coroutineExample9() {
        /*
         * Урок 9. Корутины. Билдеры launch и async.
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/604-urok-9-korutiny-bildery-launch-i-async.html
         */
        val scope = CoroutineScope(Job())

        fun onRun() {
            /* Пример 1. Ожидание одной дочерней корутины. */
//            scope.launch {
//                log(TAG_EXAMPLE_9,"parent coroutine, start")
//
//                val job = launch {
//                    log(TAG_EXAMPLE_9,"child coroutine, start")
//                    TimeUnit.MILLISECONDS.sleep(1000)
//                    log(TAG_EXAMPLE_9,"child coroutine, end")
//                }
//
//                log(TAG_EXAMPLE_9,"parent coroutine, wait until child completes")
//                job.join()
//
//                log(TAG_EXAMPLE_9,"parent coroutine, end")
//            }

            /* Пример 2. Ожидание нескольких дочерних корутин. */
            scope.launch {
                log(TAG_EXAMPLE_9,"parent coroutine, start")

                val job = launch {
                    TimeUnit.MILLISECONDS.sleep(1000)
                }

                val job2 = launch {
                    TimeUnit.MILLISECONDS.sleep(1500)
                }

                log(TAG_EXAMPLE_9, "parent coroutine, wait until children complete")
                job.join()
                job2.join()

                log(TAG_EXAMPLE_9,"parent coroutine, end")
            }
        }

        fun onRunAsync() {
            scope.launch {
                log(TAG_EXAMPLE_9,"parent coroutine, start")

                val deferred = async() {
                    log(TAG_EXAMPLE_9,"child coroutine, start")
                    TimeUnit.MILLISECONDS.sleep(1000)
                    log(TAG_EXAMPLE_9,"child coroutine, end")

                    return@async "Obtained async result"
                }

                log(TAG_EXAMPLE_9,"parent coroutine, wait until child returns result")
                val result = deferred.await()
                log(TAG_EXAMPLE_9,"parent coroutine, child returns: $result")

                log(TAG_EXAMPLE_9,"parent coroutine, end")
            }
        }

        suspend fun getData(): String {
            delay(1000)
            return "data"
        }

        suspend fun getData2(): String {
            delay(1500)
            return "data2"
        }

        fun onRunNonParallel() {
            scope.launch {
                log(TAG_EXAMPLE_9,"parent coroutine, start")

                val data = getData()
                val data2 = getData2()
                val result = "${data}, ${data2}"
                log(TAG_EXAMPLE_9,"parent coroutine, children returned: $result")

                log(TAG_EXAMPLE_9,"parent coroutine, end")
            }
        }

        fun onRunParallel() {
            scope.launch {
                log(TAG_EXAMPLE_9,"parent coroutine, start")

                val data = async { getData() }
                val data2 = async { getData2() }

                log(TAG_EXAMPLE_9,"parent coroutine, wait until children return result")
                val result = "${data.await()}, ${ data2.await()}"
                log(TAG_EXAMPLE_9,"parent coroutine, children returned: $result")

                log(TAG_EXAMPLE_9,"parent coroutine, end")
            }
        }

        fun onCancel() {

        }

        binding.example9RunBtn.setOnClickListener {
//            onRun()
//            onRunAsync()
//            onRunNonParallel()
            onRunParallel()
        }
    }

    private fun coroutineExample10() {
        /*
         * Урок 10. Корутины. Context
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/605-urok-10-korutiny-context.html
         */

        // 1. Создаем свой Context
//        val context = Job() + Dispatchers.Default
//        log(TAG_EXAMPLE_10, "context = $context")

        // 2.1. Создаем свой Scope
        // val scope = CoroutineScope(context)

        // 2.2. Создаем Scope с пустым контекстом
//        val scope = CoroutineScope(EmptyCoroutineContext)
//        log(TAG_EXAMPLE_10, "scope, ${contextToString(scope.coroutineContext)}")
//        scope.launch {
//            log(TAG_EXAMPLE_10, "coroutine, ${contextToString(coroutineContext)}")
//        }

        // 2.3. Создаем Scope с контекстом в котором кастомный диспетчер
        val scope = CoroutineScope(Job() + Dispatchers.Main + UserData(-1, "TestName1", 12))
        log(TAG_EXAMPLE_10, "scope, ${contextToString(scope.coroutineContext)}")
        scope.launch {
            log(TAG_EXAMPLE_10, "coroutine, ${contextToString(coroutineContext)}")
        }

        fun innerCoroutinesContext() {
            scope.launch {
                log(TAG_EXAMPLE_10, "coroutine, level1, ${contextToString(coroutineContext)}")

                launch(Dispatchers.Default + UserData(-2, "TestName2", 13)) {
                    log(TAG_EXAMPLE_10, "coroutine, level2, ${contextToString(coroutineContext)}")

                    launch {
                        log(TAG_EXAMPLE_10, "coroutine, level3, ${contextToString(coroutineContext)}")
                    }
                }
            }
        }

        binding.example10CreateContextBtn.setOnClickListener {
            innerCoroutinesContext()
        }
    }

    private fun coroutineExample11() {
        /*
         * Урок 11. Корутины. Dispatcher
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/606-urok-11-korutiny-dispatcher.html
         */

        /* На дефолтном диспетчере - корутины ожидают очереди, на IO - нет, т.к. там 64+ потока в пуле. */
        val scope = CoroutineScope(Dispatchers.Default)

        /* Кастомный Executor с одим потоком. */
        val scopeSingleThread = CoroutineScope(
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        )

        /* Диспетчер, который не переключает поток. */
        val scopeUnconfined = CoroutineScope(Dispatchers.Unconfined)

        binding.example11Btn.setOnClickListener {
            repeat(6) {
                scope.launch {
                    log(TAG_EXAMPLE_11, "coroutine $it, start")
                    TimeUnit.MILLISECONDS.sleep(100)
                    log(TAG_EXAMPLE_11, "coroutine $it, end")
                }
            }
        }
    }

    private fun coroutineExample12() {
        /*
         * Урок 12. Корутины. Связь между родительской и дочерней корутиной
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/607-urok-12-svyaz-mezhdu-roditelskoy-i-docherney-korutinoy.html
         */
        val scope = CoroutineScope(Dispatchers.Default)

        binding.example12Btn.setOnClickListener {
            val job = scope.launch {
                log(TAG_EXAMPLE_12,"parent start")
                launch {
                    log(TAG_EXAMPLE_12,"child start")
                    delay(1000)
                    log(TAG_EXAMPLE_12,"child end")
                }
                log(TAG_EXAMPLE_12,"parent end")
            }

            scope.launch {
                delay(500)
                log(TAG_EXAMPLE_12,"parent job is active: ${job.isActive}")
                delay(1000)
                log(TAG_EXAMPLE_12,"parent job is active: ${job.isActive}")
            }
        }
    }

    private fun coroutineExample13() {
        /*
         * Урок 13. Корутины. Обработка исключений
         *
         * https://startandroid.ru/ru/courses/kotlin/29-course/kotlin/608-urok-13-obrabotka-oshibok.html
         */
        val scope = CoroutineScope(Dispatchers.Default)

        binding.example13Btn.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun log(tag: String, text: String) {
        Log.d(tag, "${formatter.format(Date())} $text [${Thread.currentThread().name}]")
    }

    private fun contextToString(context: CoroutineContext): String =
        "Job = ${context[Job]}, Dispatcher = ${context[ContinuationInterceptor]}, UserData = ${context[UserData]}"

    private data class UserData(
        val id: Long,
        val name: String,
        val age: Int
    ): AbstractCoroutineContextElement(UserData) {
        companion object Key : CoroutineContext.Key<UserData>
    }

    private suspend fun getData(): String =
        suspendCoroutine {
            thread {
                TimeUnit.MILLISECONDS.sleep(3000)
                it.resume("Data")
            }
        }
}