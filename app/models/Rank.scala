package models

abstract class Rank(val picture:String) extends Ordered[Rank] {
  def displayName = "Tier1 " + name
  
  //objects finish with $ so we drop that from the name so it would be Recruit instead of Recruit$
  def name = getClass.getSimpleName.dropRight(1)
  
  def compare(that: Rank) =  Rank.orderedMap(this) - Rank.orderedMap(that)
}

object Rank {
  def apply(name: String) : Rank = name match {
    case "Recruit" => Recruit
    case "Regular" => Regular
    case "Veteran" => Veteran
    case "Admin" => Admin
    case "Instructor" => Instructor
  }
  
  def list = List(Recruit,Regular,Veteran,Admin,Instructor).map(r => r.name)
  
  val orderedMap = Map(Recruit->5,Regular->4,Veteran->3,Instructor->2,Admin->1)
}

object Recruit extends Rank("recruit.paa")
object Regular extends Rank("regular.paa")
object Veteran extends Rank("veteran.paa")
object Admin extends Rank("admin.paa")
object Instructor extends Rank("instructor.paa")