package models

import scala.language.postfixOps
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Training(name:String)

object Training {
  
  val training = {
    get[String]("name") map {
        case name => Training(name)
      }
  }
  
  def all(): List[Training] = DB.withConnection { implicit c =>
    SQL("select * from trainings order by name").as(training *)
  }
  
  def find(name:String): Option[Training] = DB.withConnection { implicit c =>
    SQL("select * from trainings where name={name}").on(
        'name->name).as(training.singleOpt)
  }
  
  def create(name:String) = find(name) match {
      case None => DB.withConnection { implicit c =>
        SQL("insert into trainings (name) values ({name})").on(
          'name -> name).executeUpdate()
        }
      case Some(t) => 0
  }
  
  def delete(name: String) {
    DB.withConnection { implicit c =>
      SQL("delete from trainings where name = {name}").on(
        'name -> name).executeUpdate()
    }
  }
}