package models

import java.util.Date
import scala.language.postfixOps
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Calendar

case class PlayerTraining(player:Player,training:Training,date:Date,trainer:String)

object PlayerTraining {
  val ptParser = get[Date]("player_trainings.date") ~ 
		  get[String]("player_trainings.trainer")
  
  val rowParser = Player.rowParser ~ Training.rowParser ~ ptParser map {
    case player ~ training ~ playerTraining => PlayerTraining(player,training,playerTraining._1,playerTraining._2)
  }
  
  def all(): List[PlayerTraining] = DB.withConnection { implicit c =>
    SQL(
        """select * from trainings t
        join player_trainings pt on pt.training = t.name
        join players p on p.name = pt.player
         """)
         .as(rowParser.*)
  }
  
  def roster() = {
    val byRank = all.groupBy(x => x.player.rank).mapValues( lpt => lpt.groupBy(x => x.player).toList).toList
    
    byRank.sortWith( (lhs,rhs) => lhs._1.compareTo(rhs._1) <0)
  }
  
  def allTrainingsByPlayer = {
    all.groupBy(_.player)
  }
  
  def last30Days(): List[PlayerTraining] = DB.withConnection { implicit c =>
    val cal = Calendar.getInstance
    cal.add(Calendar.MONTH,-1)
    val fromDate = cal.getTime
    
    SQL(
        """select * from trainings t
        join player_trainings pt on pt.training = t.name
        join players p on p.name = pt.player
        where pt.DATE>={fromDate}
         """).on('fromDate->fromDate)
         .as(rowParser.*)
  }
  
  def announcements() = {
    last30Days.groupBy(x => x.date).toList.sortWith( (lhs,rhs) => lhs._1.compareTo(rhs._1)  > 0)
  }
  
  def add(aTraining:PlayerTraining) = DB.withConnection { implicit c =>
        SQL("insert into player_trainings (player,training,date,trainer) values ({player},{training},{date},{trainer})").on(
          'player -> aTraining.player.name, 'training -> aTraining.training.name, 'date -> aTraining.date, 'trainer -> aTraining.trainer).executeUpdate()
  }
  
//  
//  def delete(name: String) {
//    DB.withConnection { implicit c =>
//      SQL("delete from trainings where name = {name}").on(
//        'name -> name).executeUpdate()
//    }
//  }
}