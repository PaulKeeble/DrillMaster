package models

import scala.language.postfixOps
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.util.Date


case class Player(name: String, bisId: String,remark:String,rank:Rank=Recruit,joined:Date=new Date) {
  
  def updateRank(newRank:Rank) = copy(rank = newRank)
  
  def updateJoined(newJoinDate:Date) = copy(joined= newJoinDate)
  
  def limitedUpdate(other: Player) = copy(bisId=other.bisId, remark=other.remark)
}

object Player {
  
  def applyWithoutRank(name:String, bisId:String, remark:String) : Player = new Player(name,bisId,remark,Recruit)

  def unapplyWithoutRank(player:Player) =Some((player.name,player.bisId,player.remark))
  
  def applyWithDate(name:String,bisId:String,remark:String,joined:Date) : Player = new Player(name,bisId,remark,Recruit,joined)
  
  def unapplyWithDate(player:Player) = Some((player.name,player.bisId,player.remark,player.joined))
  
  def parser(tableName:String) = {
    get[String](s"$tableName.bisId") ~ 
    get[String](s"$tableName.name") ~
    get[String](s"$tableName.remark") ~
    get[String](s"$tableName.rank") ~
    get[Date](s"$tableName.joined") map {
        case bisId ~ name ~ remark ~ rank ~ joined => Player(name, bisId,remark,Rank(rank),joined)
      }
  }
  
  val rowParser = parser("players")
  
  val archivedParser = parser("archived_players")

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
    all.groupBy(_.rank).toList.sortWith( (lhs,rhs) => lhs._1 < (rhs._1) )
  }
  
  def recruits = {
    all.filter(_.rank == Recruit)
  }
  
  def archive(name:String) = DB.withConnection { implicit c =>
    val p = find(name)
    p match {
      case Some(player) => {
        SQL("insert into archived_players (name,bisId,remark,rank,joined) values ({name},{bisId},{remark},{rank},{joined})").on(
        'name -> player.name, 'bisId -> player.bisId, 'rank -> player.rank.name, 'remark->player.remark,'joined -> player.joined).executeUpdate()
        
        delete(player.name)
      }
      case None => Unit
    }
  }
  
  def deleteArchivedPlayer(name:String) = DB.withConnection { implicit c =>
    SQL("delete from archived_players where name = {name}").on('name -> name).executeUpdate()
  }
  
  def restore(playerName:String) = DB.withConnection { implicit c =>
    val player = SQL("select * from archived_players where name={name}").on(
        'name->playerName).as(archivedParser.singleOpt)
        
    player match {
      case Some(p) => {
        create(p)
        deleteArchivedPlayer(playerName)
      }
      case None => 0
    }
  }
  
  def allArchived: List[Player] = DB.withConnection { implicit c =>
    SQL("select * from archived_players").as(archivedParser.*)
  }
}