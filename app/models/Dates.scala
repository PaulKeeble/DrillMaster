package models

import java.util.Calendar
import java.util.Date

import java.util.Calendar._

object Dates {
	def oneMonthAgo:Date = nMonthsAgo(1)
	
	def twoMonthsAgo:Date = nMonthsAgo(2)

	def today = {
	  val cal = Calendar.getInstance
	  cal.getTime
	}
	
	def nMonthsAgo(n:Int) = {
	  val cal = Calendar.getInstance
      cal.add(MONTH,-1*n)
      cal.getTime
	}
	
	def descending(lhs:Date,rhs:Date) = lhs.compareTo(rhs)>0
}