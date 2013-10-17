package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.Player
import models.Rank

object Ranks extends Controller with Secured {
  val rankForm = Form(
    "rank" -> text
  )
  
  def list() = isAdmin { implicit request =>
    val players = Player.all
    
    Ok(views.html.ranks.list(players,Rank.list,rankForm,JoinDate.joinForm))
  }
  
  def update(name:String) = isAdmin { implicit request =>
    rankForm.bindFromRequest.fold(
      errors => Status(503),
      rank => {
        val player = for( player <- Player.find(name)) yield  player.updateRank(Rank(rank))
        
        player match {
          case Some(player) => { 
            Player.update(player)
            Redirect(routes.Ranks.list)
          }
          case None => Status(503)
        }
      }
    )
  }
}