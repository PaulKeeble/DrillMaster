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
  
  def allTrainingsByPlayer = {
    all.groupBy(_.player)
  }
  
  def last30Days(): List[PlayerTraining] = DB.withConnection { implicit c =>
    val fromDate = Dates.oneMonthAgo
    
    SQL(
        """select * from trainings t
        join player_trainings pt on pt.training = t.name
        join players p on p.name = pt.player
        where pt.DATE>={fromDate}
         """).on('fromDate->fromDate)
         .as(rowParser.*)
  }
  
  def announcements() = {
    last30Days.groupBy(x => x.date).toList.sortWith( (lhs,rhs) => Dates.descending(lhs._1,rhs._1))
  }
  
  def add(aTraining:PlayerTraining) = DB.withConnection { implicit c =>
        SQL("insert into player_trainings (player,training,date,trainer) values ({player},{training},{date},{trainer})").on(
          'player -> aTraining.player.name, 'training -> aTraining.training.name, 'date -> aTraining.date, 'trainer -> aTraining.trainer).executeUpdate()
  }
  
  def delete(aTraining:PlayerTraining) = DB.withConnection { implicit c =>
    SQL("delete from player_trainings where player={player} and training={training} and trainer={trainer} and date={date}").on(
        'player -> aTraining.player.name, 'training -> aTraining.training.name, 'date -> aTraining.date, 'trainer -> aTraining.trainer).executeUpdate()
  }
  
  def delete(playerName:String,trainingName:String,date:Date,trainer:String) : Boolean = {
    val found = for(player <- Player.find(playerName); training <- Training.find(trainingName)) yield (player,training)
    found match {
      case None => false
      case Some((player,training)) => delete(PlayerTraining(player,training,date,trainer)); false
    }
  }
}