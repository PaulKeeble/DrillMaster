package object dates {
  implicit class ComparableDate(val date:java.util.Date) extends Ordered[ComparableDate] {
    def compare(that:ComparableDate) = date.compareTo(that.date)
  }
}