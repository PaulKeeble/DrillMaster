package models

abstract class Rank(val picture:String) {
  def displayName = "Tier1 " + name
  
  //objects finish with $ so we drop that from the name so it would be Recruit instead of Recruit$
  def name = getClass.getSimpleName.dropRight(1) 
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
}

object Recruit extends Rank("recruit.paa")
object Regular extends Rank("regular.paa")
object Veteran extends Rank("veteran.paa")
object Admin extends Rank("admin.paa")
object Instructor extends Rank("instructor.paa")