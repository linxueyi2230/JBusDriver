package jbusdriver.me.jbusdriver

import io.reactivex.Flowable
import io.reactivex.rxkotlin.subscribeBy
import org.junit.Test
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    val TAG = ExampleUnitTest::class.java.simpleName
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        Flowable.concat<Int>(Flowable.empty() , Flowable.timer(1,TimeUnit.SECONDS).map { 1 } )
                .firstOrError().subscribeBy(onSuccess = { println(it)})

    }
}