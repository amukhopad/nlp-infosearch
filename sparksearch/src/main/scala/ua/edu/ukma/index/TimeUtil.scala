package ua.edu.ukma.index

import java.time.LocalTime
import java.time.temporal.ChronoUnit

object TimeUtil {
  type HoursMinSec = (Long, Long, Long)

  def printFinishTime(implicit startTime: LocalTime): Unit = {
    val (hours, mins, secs) = getFinishTime(startTime)
      println(f"\nProcess took ${hours} hours ${mins} minutes ${secs} seconds to finish\n")
  }

  def getFinishTime(implicit startTime: LocalTime): HoursMinSec = {
    val endTime = LocalTime.now()
    TimeUtil.timeTotal(startTime, endTime)
  }

  def timeTotal(start: LocalTime, end: LocalTime): HoursMinSec = {
    val hours = start.until(end, ChronoUnit.HOURS)
    val minutes = start.plusHours(hours).until(end, ChronoUnit.MINUTES)
    val seconds = start.plusMinutes(minutes).until(end, ChronoUnit.SECONDS)
    (hours, minutes, seconds)
  }
}
