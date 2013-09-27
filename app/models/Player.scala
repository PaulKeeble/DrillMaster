package models

import scala.language.postfixOps
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class Player(name: String, bisId: String,remark:String,rank:Rank=Recruit,joined:Date=new Date) {
  
  def updateRank(newRank:Rank) = copy(rank = newRank)
  
  def limitedUpdate(other: Player) = copy(bisId=other.bisId, remark=other.remark)
}

object Player {
  
  def applyWithoutRank(name:String, bisId:String, remark:String) : Player = new Player(name,bisId,remark,Recruit)

  def unapplyWithoutRank(player:Player) =Some((player.name,player.bisId,player.remark))
  
  def applyWithDate(name:String,bisId:String,remark:String,joined:Date) : Player = new Player(name,bisId,remark,Recruit,joined)
  
  def unapplyWithDate(player:Player) = Some((player.name,player.bisId,player.remark,player.joined))
  
  val rowParser = {
    get[String]("players.bisId") ~ 
    get[String]("players.name") ~
    get[String]("players.remark") ~
    get[String]("players.rank") ~
    get[Date]("players.joined") map {
        case bisId ~ name ~ remark ~ rank ~ joined => Player(name, bisId,remark,Rank(rank),joined)
      }
  }

  def all(): List[Player] = DB.withConnection { implicit c =>
    SQL("select * from players order by name").as(rowParser.*)
  }
  
  def create(player:Player) = DB.withConnection { implicit c =>
      SQL("insert into players (name,bisId,remark,rank,joined) values ({name},{bisId},{remark},{rank},{joined})").on(
        'name -> player.name, 'bisId -> player.bisId, 'rank -> player.rank.name, 'remark->player.remark,'joined -> player.joined).executeUpdate()
  }
  
  def update(player:Player) = DB.withConnection { implicit c =>
    SQL("update players SET name={name},bisId={bisId},remark={remark},rank={rank},joined={joined} WHERE name={name2}").on(
        'name -> player.name, 'bisId -> player.bisId, 'rank -> player.rank.name, 'remark->player.remark,'name2->player.name,'joined->player.joined).executeUpdate()
  }

  def createOrUpdate(player:Player) {
    find(player.name) match {
      case Some(found) => update(player)
      case None => create(player)
    }
  }

  def delete(name: String) {
    DB.withConnection { implicit c =>
      SQL("delete from players where name = {name}").on(
        'name -> name).executeUpdate()
    }
  }
  
  def find(name:String): Option[Player] = DB.withConnection { implicit c =>
    SQL("select * from players where name={name}").on(
        'name->name).as(rowParser.singleOpt)
  }
  
  def allPlayersSortedByRank = {
    all.groupBy(_.rank).toList.sortWith((lhs,rhs) => lhs._1.compareTo(rhs._1) < 0)
  }
  
  def recruits = {
    all.filter(_.rank == Recruit)
  }
}