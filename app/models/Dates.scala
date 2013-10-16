package models

import java.util.Calendar
import java.util.Date

import java.util.Calendar._

object Dates {
	def oneMonthAgo:Date = {
	  val cal = Calendar.getInstance
      cal.add(MONTH,-1)
      cal.getTime
	}
	
	def descending(lhs:Date,rhs:Date) = lhs.compareTo(rhs)>0
}