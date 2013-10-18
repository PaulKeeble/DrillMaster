
import org.specs2.mutable.Specification
import models.Dates

class datesSpec extends Specification {
  "date is equal" in {
    val today = Dates.today
    
    today must be equalTo( today)
  } 
  
  "date is less than" in {
    val today = Dates.today
    val oneMonthAgo = Dates.oneMonthAgo
    
    oneMonthAgo must be lessThan(today)
  }
  
  "date is greater than" in {
    val oneMonthAgo = Dates.oneMonthAgo
    print(oneMonthAgo)
    val twoMonthsAgo = Dates.twoMonthsAgo
    print(twoMonthsAgo)
    oneMonthAgo must be greaterThan(twoMonthsAgo)
  }
}