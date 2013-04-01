package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Player(name: String, bisId: String,remark:String,rank:Rank=Recruit) {
  
  def updateRank(newRank:Rank) = copy(rank = newRank)
  
  def limitedUpdate(other: Player) = copy(bisId=other.bisId, remark=other.remark)
}

object Player {
  
  def apply(name:String,bisId:String,remark:String) : Player = new Player(name,bisId,remark)

  def unapplyWithoutRank(player:Player) =Some((player.name,player.bisId,player.remark))
  
  val player = {
    get[String]("bisId") ~ 
    get[String]("name") ~
    get[String]("remark") ~
    get[String]("rank") map {
        case bisId ~ name ~ remark ~ rank => Player(name, bisId,remark,Rank(rank))
      }
  }

  def all(): List[Player] = DB.withConnection { implicit c =>
    SQL("select * from players order by name").as(player *)//.sortWith((p1,p2) => p1.name < p2.name)
  }
  
  def create(player:Player) = DB.withConnection { implicit c =>
      SQL("insert into players (name,bisId,remark,rank) values ({name},{bisId},{remark},{rank})").on(
        'name -> player.name, 'bisId -> player.bisId, 'rank -> player.rank.name, 'remark->player.remark).executeUpdate()
  }
  
  def update(player:Player) = DB.withConnection { implicit c =>
    SQL("update players SET name={name},bisId={bisId},remark={remark},rank={rank} WHERE name={name2}").on(
        'name -> player.name, 'bisId -> player.bisId, 'rank -> player.rank.name, 'remark->player.remark,'name2->player.name).executeUpdate()
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
        'name->name).as(player.singleOpt)
  }
}