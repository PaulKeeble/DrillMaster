package models

import java.util.Date
import scala.language.postfixOps

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current



case class PlayerTraining(player:Player,training:Training,date:Date)

object PlayerTraining {
  val dateParser = get[Date]("player_training.date")
  
  val rowParser = Player.rowParser ~ Training.rowParser ~ dateParser map {
    case player ~ training ~ date => PlayerTraining(player,training,date)
  }
  
  def all(): List[PlayerTraining] = DB.withConnection { implicit c =>
    SQL(
        """select * from trainings t
        join player_trainings pt on pt.training = t.name
        join players p on p.name = pt.player
         """)
         .as(rowParser.*)
  }
  
  def add(aTraining:PlayerTraining) = DB.withConnection { implicit c =>
        SQL("insert into player_trainings (player,training,date) values ({player},{training},{date})").on(
          'player -> aTraining.player.name, 'training -> aTraining.training.name, 'date -> aTraining.date).executeUpdate()
  }
//  
//  def delete(name: String) {
//    DB.withConnection { implicit c =>
//      SQL("delete from trainings where name = {name}").on(
//        'name -> name).executeUpdate()
//    }
//  }
  
//  implicit val reader = (
//	  (__ \ "player").read[String] ~
//	  (__ \ "training").read[String] ~
//	  (__ \ "date").read[Date]
//	)((String,String,Date)  
}