package io.bsconsulting.cosc370

import io.bsconsulting.cosc370.model.Trackable
import org.junit.Test

import org.junit.Assert.*
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.absoluteValue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun thereIsSixtySecondsBetweenTwoDatesOneMinuteApart(){
        val time1 = LocalDateTime.now()
        val time2 = LocalDateTime.now().plusMinutes(1)
        val d1: Int = (Duration.between(time1,time2).toMinutes()*60).absoluteValue.toInt()
        val d2: Int = (Duration.between(time2, time1).toMinutes()*60).absoluteValue.toInt()
        assertEquals(60, d1)
        assertEquals(60, d2)
        assertEquals(d1, d2)

        for(i in 1..60){
            Thread.sleep(1000)
            println((Duration.between(time1,LocalDateTime.now()).toMillis()/1000))
        }

    }

    @Test
    fun testTheDateFurthestIntoTheFutureIsReturned(){

        val trackable = Trackable("Food", true, 1)
        val now = LocalDateTime.now()
        val later = now.plusHours(1)

        trackable.activity.add(now)
        trackable.activity.add(later)
        trackable.activity.add(now.minusDays(1))

        val time: LocalDateTime = trackable.activity.sortedDescending().first()

        assertTrue(later.equals(time))

    }

    @Test
    fun testCalculateTimeToZeroAsInt(){
        val interval = 5
        val now = LocalDateTime.now()
        val next = now.plusMinutes(5)

        val gap: Int = (Duration.between(next, now).toMillis() /1000).toInt().absoluteValue

        assertEquals(300, gap)
    }

    @Test
    fun testPercentageFromTimeToZero(){

        //given
        val frequency = 10L; //minutes
        val maxGap: Double = frequency * 60.0 // seconds

        val firstEvent = LocalDateTime.now()
        val now = firstEvent.plusMinutes(5)
        val next = firstEvent.plusMinutes(frequency)

        val gap: Int = (Duration.between(next, now).toMillis() /1000).toInt().absoluteValue

        val calculatePercentage = percentage(maxGap)
        //when
        val progress = calculatePercentage(gap)

        assertEquals(50, progress)
    }

    fun percentage(max: Double): (Int) -> Int {
        return { actual: Int -> (((max - actual)/max)*100).toInt()}
    }


}
