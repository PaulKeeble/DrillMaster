package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Player

object Players extends Controller with Secured {
  val playerForm = Form(mapping(
    "name" -> nonEmptyText,
    "bisId" -> number.verifying(min(1)),
    "remark" -> text
    )(Player.apply)(Player.unapplyWithoutRank)
  )
  
  def form = IsAuthenticated { implicit request =>
    Ok(views.html.players.create(playerForm))
  }

  def create = IsAuthenticated { implicit request =>
    playerForm.bindFromRequest.fold(
      errors => BadRequest(views.html.players.create(errors)),
      player => {
        Player.createOrUpdate(player)
        Redirect(routes.Players.show(player.name))
      }
    )
  }
  
  def update(name:String) = IsAuthenticated { implicit request =>
    Player.find(name) match {
      case Some(player) => playerForm.bindFromRequest.fold(
        errors => BadRequest(views.html.players.show(name,errors)),
        submitPlayer =>  {
          val updated = player.limitedUpdate(submitPlayer)
          Player.update(updated)
          Ok(views.html.players.show(name,playerForm.fill(updated)))
        }
      )
      
      case None => Status(503)
    }
  }
  
  def show(name:String) = IsAuthenticated { implicit request =>
    Player.find(name) match {
      case Some(player) =>  {
       val filledForm = playerForm.fill(player)
       Ok(views.html.players.show(name,filledForm)) 
      } 
      case None => Status(404)
    }
  }
  
  def xml(name:String) = Action { implicit request =>
    Player.find(name) match {
      case Some(player) => Ok(views.xml.players.squad(player))
      case None => Status(404)
    }
  }  
}