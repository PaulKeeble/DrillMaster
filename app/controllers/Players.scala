package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

import models.Player

object Players extends Controller with Secured {
  val createForm = Form(mapping(
    "name" -> nonEmptyText,
    "bisId" -> nonEmptyText,
    "remark" -> text
    )(Player.applyWithoutRank)(Player.unapplyWithoutRank)
  )
  
  val editForm = Form(mapping(
    "name" -> nonEmptyText,
    "bisId" -> nonEmptyText,
    "remark" -> text,
    "joined" -> date
    )(Player.applyWithDate)(Player.unapplyWithDate)
  )
  
  def form = IsAuthenticated { implicit request =>
    Ok(views.html.players.create(createForm))
  }

  def create = IsAuthenticated { implicit request =>
    createForm.bindFromRequest.fold(
      errors => BadRequest(views.html.players.create(errors)),
      player => {
      	Player.find(player.name) match {
      		case Some(existingPlayer) => {
      			val updated = existingPlayer.limitedUpdate(player)
      			Player.update(updated)
      		}
      		case None => Player.createOrUpdate(player)
      	}
        Redirect(routes.Players.show(player.name))
      }
    )
  }
  
  def update(name:String) = IsAuthenticated { implicit request =>
    Player.find(name) match {
      case Some(player) => editForm.bindFromRequest.fold(
        errors => BadRequest(views.html.players.show(name,errors)),
        submitPlayer =>  {
          val updated = player.limitedUpdate(submitPlayer)
          Player.update(updated)
          Ok(views.html.players.show(name,createForm.fill(updated)))
        }
      )
      
      case None => Status(503)
    }
  }
  
  def delete(name:String) = isAdmin { implicit request => 
    Player.delete(name)
    Ok("done")
  }
  
  def show(name:String) = IsAuthenticated { implicit request =>
    Player.find(name) match {
      case Some(player) =>  {
       val filledForm = editForm.fill(player)
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