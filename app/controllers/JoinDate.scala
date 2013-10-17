package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.Player

object JoinDate extends Controller with Secured {
  val joinForm = Form(
    "joined" -> date
  )
  
  def update(playerName:String) = isAdmin { implicit request =>
    joinForm.bindFromRequest.fold(
      errors => Status(503),
      date => {
        val player = for( player <- Player.find(playerName)) yield  player.updateJoined(date)
        
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